package com.kk.LineMappingProject;

import java.util.List;
import java.util.ArrayList; 
import com.kk.LineMappingProject.FileNormalization;
import com.kk.LineMappingProject.DiffAlgorithm;

// Yusra Ahmed 110106816

public class TestDiffWithPaths {
    
    // Separate class for the testing logic
    public static class DiffTester {
        
        public static void testFiles(String file1, String file2) {

            System.out.println("=== Testing DiffAlgorithm with File Paths ===\n");
            
            // Read normalized files
            List<String> fileALines = FileNormalization.normalize(file1);
            List<String> fileBLines = FileNormalization.normalize(file2);
            
            System.out.println("File A: " + file1 + " (" + fileALines.size() + " lines)");
            System.out.println("File B: " + file2 + " (" + fileBLines.size() + " lines)");
            
            // Run DiffAlgorithm
            DiffAlgorithm diff = new DiffAlgorithm();
            DiffAlgorithm.DiffResult result = diff.computeDiff(fileALines, fileBLines);
            
            printResults(result);
        }
        
        private static void printResults(DiffAlgorithm.DiffResult result) {
            System.out.println("\n🔧 EDIT OPERATIONS FOUND: " + result.editScript.size());
            for (DiffAlgorithm.EditOperation op : result.editScript) {
                System.out.println("   " + op.type + " - Line " + (op.rightLine + 1) + ": " + 
                    (op.content.length() > 50 ? op.content.substring(0, 50) + "..." : op.content));
            }
            
            printSummary(result);
        }
        
        private static void printSummary(DiffAlgorithm.DiffResult result) {
            int unchanged = 0, modified = 0, added = 0, deleted = 0;
            
            for (DiffAlgorithm.DiffLine line : result.leftList) {
                if (line.changeType == DiffAlgorithm.ChangeType.UNCHANGED) unchanged++;
                else if (line.changeType == DiffAlgorithm.ChangeType.MODIFIED) modified++;
                else if (line.changeType == DiffAlgorithm.ChangeType.DELETED) deleted++;
            }
            
            for (DiffAlgorithm.DiffLine line : result.rightList) {
                if (line.changeType == DiffAlgorithm.ChangeType.ADDED) added++;
            }
            
            System.out.println("\n🎯 SUMMARY:");
            System.out.println("   Unchanged: " + unchanged + " lines");
            System.out.println("   Modified:  " + modified + " lines");
            System.out.println("   Added:     " + added + " lines");
            System.out.println("   Deleted:   " + deleted + " lines");
            System.out.println("   Total changes: " + result.editScript.size() + " operations");
        }
    }
}