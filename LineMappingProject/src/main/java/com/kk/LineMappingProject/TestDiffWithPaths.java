package com.kk.LineMappingProject;

import java.util.List;
import java.util.ArrayList; 
import com.kk.LineMappingProject.FileNormalization;
import com.kk.LineMappingProject.DiffAlgorithm;
import java.util.Map;

// Yusra Ahmed 110106816, Mahnoz Akhtari 105011198, Najiya Ahmad 110110372

public class TestDiffWithPaths {
	
	// main entry point so TestDiffWithPaths can be run directly
    public static void main(String[] args) {  //
        if (args.length < 2) {               // ADDED
            System.out.println("Usage: java com.kk.LineMappingProject.TestDiffWithPaths <fileA> <fileB>");  
            return;                          
        }                                    

        String file1 = args[0];              
        String file2 = args[1];              

        DiffTester.testFiles(file1, file2);  
    }   
    
    // Separate class for the testing logic
    public static class DiffTester {
        
        public static void testFiles(String file1, String file2) {

            //System.out.println("=== Testing DiffAlgorithm with File Paths ===\n");
            
            // Read normalized files
            List<String> fileALines = FileNormalization.normalize(file1);
            List<String> fileBLines = FileNormalization.normalize(file2);
            
            //System.out.println("File A: " + file1 + " (" + fileALines.size() + " lines)");
            //System.out.println("File B: " + file2 + " (" + fileBLines.size() + " lines)");
            
            // Run DiffAlgorithm
            DiffAlgorithm diff = new DiffAlgorithm();
            DiffAlgorithm.DiffResult result = diff.computeDiff(fileALines, fileBLines);

            // Phase 4: Similarity-based mapping
            SimilarityMapper similarityMapper = new SimilarityMapper();

            // Phase 3: Build candidate sets using SimHash (top-K = 15)
            Map<Integer, List<Integer>> candidateMap =
                    CandidateGenerator.buildCandidates(fileALines, fileBLines, 15);

            // Phase 4.1 – initial matches
            List<LineMatch> initialMatches =
                    similarityMapper.computeInitialMatches(fileALines, fileBLines, candidateMap);

            // Phase 4.2 – classify changes
            List<LineMatch> finalMatches =
                    similarityMapper.classifyChanges(initialMatches, fileBLines.size());

            // For now, print them
            /*System.out.println("\n=== PHASE 4: Similarity-based Line Mapping ===");
            for (LineMatch m : finalMatches) {
                System.out.println(m);
            }
            printResults(result);
        }
        
        private static void printResults(DiffAlgorithm.DiffResult result) {
            System.out.println("\n🔧 EDIT OPERATIONS FOUND: " + result.editScript.size());
            for (DiffAlgorithm.EditOperation op : result.editScript) {
                System.out.println("   " + op.type + " - Line " + (op.rightLine + 1) + ": " + 
                    (op.content.length() > 50 ? op.content.substring(0, 50) + "..." : op.content));
            }
            
            printSummary(result);
        }*/
            OutputGenerator.printMappingOutput(      
                    file1,                           
                    file2,                            
                    fileALines,                       
                    fileBLines,                        
                    finalMatches                     
            );                                        

            //printSummary(result); //use phase2 summary? 
        }
        
       /* private static void printSummary(DiffAlgorithm.DiffResult result) {
            int unchangedLeft = 0, deleted = 0;
            int unchangedRight = 0, added = 0;
            
            // Count changes in left file (fileA)
            for (DiffAlgorithm.DiffLine line : result.leftList) {
                if (line.changeType == DiffAlgorithm.ChangeType.UNCHANGED) {
                    unchangedLeft++;
                } else if (line.changeType == DiffAlgorithm.ChangeType.DELETED) {
                    deleted++;
                }
            }
            
            // Count changes in right file (fileB)
            for (DiffAlgorithm.DiffLine line : result.rightList) {
                if (line.changeType == DiffAlgorithm.ChangeType.UNCHANGED) {
                    unchangedRight++;
                } else if (line.changeType == DiffAlgorithm.ChangeType.ADDED) {
                    added++;
                }
            }
            
            System.out.println("\n🎯 SUMMARY:");
            System.out.println("   Unchanged lines in File A: " + unchangedLeft);
            System.out.println("   Unchanged lines in File B: " + unchangedRight);
            System.out.println("   Deleted lines: " + deleted);
            System.out.println("   Added lines: " + added);
            System.out.println("   Total edit operations: " + result.editScript.size());
            
            // Verify consistency
            System.out.println("\n📊 CONSISTENCY CHECK:");
            System.out.println("   File A total: " + result.leftList.size() + " lines (" + unchangedLeft + " unchanged + " + deleted + " deleted)");
            System.out.println("   File B total: " + result.rightList.size() + " lines (" + unchangedRight + " unchanged + " + added + " added)");
        }*/
        
    }
}
