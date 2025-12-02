package com.kk.LineMappingProject;

import java.util.List;

// Phase 5 – Output Generation (Najiya)

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
        System.out.println("=== Testing Line Mapping with File Paths ===\n");
        System.out.println("MAPPING_START");
        System.out.println("FILE_A: " + fileAName);
        System.out.println("FILE_B: " + fileBName);
        System.out.println("TOTAL_LINES_A: " + totalLinesA);
        System.out.println("TOTAL_LINES_B: " + totalLinesB + "\n");

        System.out.println("MAPPINGS:");

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

            confidenceSum += sim;
            confidenceCount++;

            // Skip printing unchanged lines (but keep them in summary)
            if (type == ChangeType.UNCHANGED) {
                continue;
            }

            int lineA = (leftIdx >= 0 && leftIdx < totalLinesA) ? (leftIdx + 1) : -1;
            int lineB = (rightIdx >= 0 && rightIdx < totalLinesB) ? (rightIdx + 1) : -1;

            /*contentA = "";
            if (leftIdx >= 0 && leftIdx < totalLinesA) {
                contentA = leftLines.get(leftIdx);
            }

            String contentB = "";
            if (rightIdx >= 0 && rightIdx < totalLinesB) {
                contentB = rightLines.get(rightIdx);
            }*/

            String prefix = lineA + " - " + lineB + ": " +
                            type.name() + ": " +
                            String.format("%.3f", sim) + ": ";

            String mappingLine;

            switch (type) {
                case DELETED:
                    mappingLine = prefix;
                    break;
                case ADDED:
                    mappingLine = prefix;  
                    break;
                case MOVED:
                    mappingLine = prefix; 
                    break;
                case MODIFIED:
                    mappingLine = prefix; //contentA + " -> " + contentB;
                    break;
                default:
                    mappingLine = prefix; // contentA + " -> " + contentB;
                    break;
            }

            System.out.println(mappingLine);
        }

        double avgConfidence = 0.0;
        if (confidenceCount > 0) {
            avgConfidence = confidenceSum / confidenceCount;
        }

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
