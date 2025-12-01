package com.kk.LineMappingProject;

import java.util.List;

// Phase 5 – Output Generation 
// Najiya Ahmad

public class OutputGenerator {

    public static void printMappingOutput(
            String fileAName,            // original file
            String fileBName,            // modified file
            List<String> leftLines,      // normalized lines from file A
            List<String> rightLines,     // normalized lines from file B
            List<LineMatch> matches      // final matches from Phase 4
    ) {
        int totalLinesA = (leftLines == null) ? 0 : leftLines.size();
        int totalLinesB = (rightLines == null) ? 0 : rightLines.size();

        // Header
        System.out.println("MAPPING_START");
        System.out.println("FILE_A: " + fileAName);
        System.out.println("FILE_B: " + fileBName);
        System.out.println("TOTAL_LINES_A: " + totalLinesA);
        System.out.println("TOTAL_LINES_B: " + totalLinesB + "\n");

        System.out.println("MAPPINGS:");

        // Summary counters
        int unchanged = 0;
        int modified = 0;
        int added = 0;
        int deleted = 0;
        int moved = 0;
        double confidenceSum = 0.0;
        int confidenceCount = 0;

        for (LineMatch lm : matches) {
            int leftIdx = lm.getLeftIndex();    // -1 if none
            int rightIdx = lm.getRightIndex();  // -1 if none
            ChangeType type = lm.getChangeType();
            double sim = lm.getSimilarity();

            // Update summary counters
            switch (type) {
                case UNCHANGED:
                    unchanged++;
                    break;
                case MODIFIED:
                    modified++;
                    break;
                case ADDED:
                    added++;
                    break;
                case DELETED:
                    deleted++;
                    break;
                case MOVED:
                    moved++;
                    break;
                default:
                    break;
            }

            // Track for average confidence
            confidenceSum += sim;
            confidenceCount++;

            //  Skip printing unchanged lines (but keep them in summary)
            if (type == ChangeType.UNCHANGED) {
                continue;
            }

            // Compute line numbers with -1 for "no line"
            int lineA = (leftIdx >= 0 && leftIdx < totalLinesA) ? (leftIdx + 1) : -1;
            int lineB = (rightIdx >= 0 && rightIdx < totalLinesB) ? (rightIdx + 1) : -1;

            // Safely grab content strings
            String contentA = "";
            if (leftIdx >= 0 && leftIdx < totalLinesA) {
                contentA = leftLines.get(leftIdx);
            }

            String contentB = "";
            if (rightIdx >= 0 && rightIdx < totalLinesB) {
                contentB = rightLines.get(rightIdx);
            }

            // Base prefix: "A - B: TYPE: confidence: "
            String prefix = lineA + " - " + lineB + ": " +
                            type.name() + ": " +
                            String.format("%.3f", sim) + ": ";

            String mappingLine;

            switch (type) {
                case DELETED:
                    // A had a line that was deleted
                    mappingLine = prefix + contentA;
                    break;

                case ADDED:
                    // B has a line that was added
                    mappingLine = prefix + contentB;
                    break;

                case MOVED:
                    // show the line once 
                    mappingLine = prefix + contentB;
                    break;

                case MODIFIED:
                    // Show old -> new
                    mappingLine = prefix + contentA + " -> " + contentB;
                    break;

                default:
                    // Fallback (shouldn't happen because we skip unchanged above)
                    mappingLine = prefix + contentA + " -> " + contentB;
                    break;
            }

            System.out.println(mappingLine);
        }

        // Average confidence
        double avgConfidence = 0.0;
        if (confidenceCount > 0) {
            avgConfidence = confidenceSum / confidenceCount;
        }

        // Summary
        System.out.println();
        System.out.println("SUMMARY:");
        System.out.println("UNCHANGED: " + unchanged);
        System.out.println("MODIFIED: " + modified);
        System.out.println("ADDED: " + added);
        System.out.println("DELETED: " + deleted);
        System.out.println("MOVED: " + moved);
        System.out.println("AVERAGE_CONFIDENCE: " + String.format("%.3f", avgConfidence));
        System.out.println("MAPPING_END");
    }
}


