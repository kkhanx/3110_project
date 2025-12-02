package com.kk.LineMappingProject;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

// Yusra Ahmed 110106816, Mahnoz Akhtari 105011198
// extra changes: Kulsum Khan 110139964

public class Main {

    /**
     * Direct entry point so you can run:
     *   java -cp target/classes com.kk.LineMappingProject.Main fileA fileB
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java com.kk.LineMappingProject.TestDiffWithPaths <file1_path> <file2_path>");
            System.out.println("Example: java com.kk.LineMappingProject.TestDiffWithPaths /path/to/version1.java /path/to/version2.java");
            return;
        }

        String file1 = args[0];
        String file2 = args[1];

        LineMapper.testFiles(file1, file2);
    }

    // Separate class for the testing logic
    public static class LineMapper {

        public static void testFiles(String file1, String file2) {

            System.out.println("=== Testing Line Mapping with File Paths ===\n");

            // -----------------------------
            // Phase 1: Read normalized files
            // -----------------------------
            List<String> fileALines = FileNormalization.normalize(file1);
            List<String> fileBLines = FileNormalization.normalize(file2);

            System.out.println("File A: " + file1 + " (" + fileALines.size() + " lines)");
            System.out.println("File B: " + file2 + " (" + fileBLines.size() + " lines)");

            // ----------------------------------------
            // Phase 2: Run LCS-based DiffAlgorithm
            // ----------------------------------------
            DiffAlgorithm diff = new DiffAlgorithm();
            DiffAlgorithm.DiffResult result = diff.computeDiff(fileALines, fileBLines);
            
            System.out.println("---------------------- left list");
            List<DiffAlgorithm.DiffLine> leftDiff  = result.leftList;
            
            for(DiffAlgorithm.DiffLine f : leftDiff) {
            	System.out.println(f.content + f.changeType);
            }
            
            System.out.println("------------------------- right list");
            List<DiffAlgorithm.DiffLine> rightDiff = result.rightList;
            
            for(DiffAlgorithm.DiffLine x : rightDiff) {
            	System.out.println(x.content + x.changeType);
            }

            // -----------------------------------------------------------------
            // Phase 3: Build candidate sets using SimHash (top-K = 15),
            // driven by Phase 2's "interesting" lines (non-UNCHANGED).
            // -----------------------------------------------------------------

            // Phase-3-specific views: only lines that are NOT UNCHANGED
            List<String> phase3LeftLines = new ArrayList<>();
            
            List<Integer> phase3LeftToOriginal = new ArrayList<>();

            for (int i = 0; i < leftDiff.size(); i++) {
                DiffAlgorithm.DiffLine dl = leftDiff.get(i);
                if (dl.changeType != ChangeType.UNCHANGED) {
                    phase3LeftLines.add(dl.content);
                    phase3LeftToOriginal.add(i);   // original index in file A
                }
            }

            List<String> phase3RightLines = new ArrayList<>();
            List<Integer> phase3RightToOriginal = new ArrayList<>();

            for (int j = 0; j < rightDiff.size(); j++) {
                DiffAlgorithm.DiffLine dl = rightDiff.get(j);
                if (dl.changeType != ChangeType.UNCHANGED) {
                    phase3RightLines.add(dl.content);
                    phase3RightToOriginal.add(j);  // original index in file B
                }
            }
            
            System.out.println("printing candidate map");
            Map<Integer, List<Integer>> candidateMap =
                    CandidateGenerator.buildCandidates(phase3LeftLines, phase3RightLines, 15);
            
            for(Entry<Integer, List<Integer>> e : candidateMap.entrySet()) {
            	System.out.println("index: " + e.getKey());
            	
            	System.out.println("possible matches");
            	for(Integer x : e.getValue()) {
            		System.out.println("       " + x);
            	}
            	
            }

            // -----------------------------------------
            // Phase 4: Similarity-based mapping
            // -----------------------------------------
            SimilarityMapper similarityMapper = new SimilarityMapper();

            // Phase 4.1 – initial matches on Phase-3 views
            List<LineMatch> initialMatchesPhase3 =
                    similarityMapper.computeInitialMatches(phase3LeftLines, phase3RightLines, candidateMap);
            
            

            // Phase 4.2 – classify changes (final labels) on Phase-3 views
            List<LineMatch> finalMatchesPhase3 =
                    similarityMapper.classifyChanges(initialMatchesPhase3, phase3RightLines.size());

            // --------------------------------------------------------
            // Map Phase-3 indices back to original file indices
            // --------------------------------------------------------
            List<LineMatch> finalMatches = new ArrayList<>();

            for (LineMatch m : finalMatchesPhase3) {
                int phase3LeftIdx = m.getLeftIndex();
                int phase3RightIdx = m.getRightIndex();

                int originalLeftIdx = -1;
                int originalRightIdx = -1;

                if (phase3LeftIdx >= 0 && phase3LeftIdx < phase3LeftToOriginal.size()) {
                    originalLeftIdx = phase3LeftToOriginal.get(phase3LeftIdx);
                }

                if (phase3RightIdx >= 0 && phase3RightIdx < phase3RightToOriginal.size()) {
                    originalRightIdx = phase3RightToOriginal.get(phase3RightIdx);
                }

                finalMatches.add(
                        new LineMatch(
                                originalLeftIdx,
                                originalRightIdx,
                                m.getChangeType(),
                                m.getSimilarity()
                        )
                );
            }

            // --------------------------------------------------------
            // Bring back UNCHANGED lines from Phase 2 (LCS)
            // --------------------------------------------------------
            for (int i = 0; i < leftDiff.size(); i++) {
                DiffAlgorithm.DiffLine dl = leftDiff.get(i);
                if (dl.changeType == ChangeType.UNCHANGED) {
                    // assume unchanged line i in A maps to i in B
                    finalMatches.add(new LineMatch(i, i, ChangeType.UNCHANGED, 1.0));
                }
            }

            // --------------------------------------------------------
            // FIX: Re-label UNCHANGED as MOVED when indices differ
            // but similarity is very high.
            // --------------------------------------------------------
            for (int k = 0; k < finalMatches.size(); k++) {
                LineMatch lm = finalMatches.get(k);
                int li = lm.getLeftIndex();
                int ri = lm.getRightIndex();

                if (li >= 0 && ri >= 0 &&
                    lm.getChangeType() == ChangeType.UNCHANGED &&
                    li != ri &&
                    lm.getSimilarity() >= 0.95) {

                    // Re-label as MOVED using original indices
                    finalMatches.set(
                        k,
                        new LineMatch(li, ri, ChangeType.MOVED, lm.getSimilarity())
                    );
                }
            }

            // -----------------------------
            // Print Phase 4 matches
            // -----------------------------
//            System.out.println("\n=== PHASE 4: Similarity-based Line Mapping ===");
//            for (LineMatch m : finalMatches) {
//                System.out.println(m);
//            }

            // Optional: still print structural edit script from LCS for reference
            //printEditScript(result);

            // Summary from Phase 4 labels
            printSummaryFromMatches(finalMatches);

            // -----------------------------------------
            // Phase 5: Final mapping-style output
            // -----------------------------------------
            OutputGenerator.printMappingOutput(
                    file1,
                    file2,
                    fileALines,
                    fileBLines,
                    finalMatches
            );
        }

        private static void printEditScript(DiffAlgorithm.DiffResult result) {
            System.out.println("\n EDIT OPERATIONS (LCS-based) FOUND: " + result.editScript.size());
            for (DiffAlgorithm.EditOperation op : result.editScript) {
                String content = op.content;
                if (content.length() > 50) {
                    content = content.substring(0, 50) + "...";
                }
                // Note: op.rightLine is used here to anchor to B's context
                System.out.println("   " + op.type + " - Line " + (op.rightLine + 1) + ": " + content);
            }
        }

        private static void printSummaryFromMatches(List<LineMatch> matches) {
            int modified = 0, moved = 0, added = 0, deleted = 0, unchanged = 0;

            for (LineMatch lm : matches) {
                switch (lm.getChangeType()) {
                    case MODIFIED:
                        modified++;
                        break;
                    case MOVED:
                        moved++;
                        break;
                    case ADDED:
                        added++;
                        break;
                    case DELETED:
                        deleted++;
                        break;
                    case UNCHANGED:
                        unchanged++;
                        break;
                    default:
                        break;
                }
            }

            System.out.println("\n SUMMARY (Phase 4 – Similarity Mapping):");
            System.out.println("   Unchanged: " + unchanged);
            System.out.println("   Modified : " + modified);
            System.out.println("   Moved    : " + moved);
            System.out.println("   Added    : " + added);
            System.out.println("   Deleted  : " + deleted);
            System.out.println("   Total labelled lines: " + matches.size());
        }
    }
}