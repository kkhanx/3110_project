package com.kk.LineMappingProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestDiffWithPaths {
    
    // Separate class for the testing logic
    public static class DiffTester {
        
        public static void testFiles(String file1, String file2) {
            try {
                System.out.println("=== Testing DiffAlgorithm with File Paths ===\n");
                
                // Read files directly from provided paths
                List<String> fileALines = Files.readAllLines(Paths.get(file1));
                List<String> fileBLines = Files.readAllLines(Paths.get(file2));
                
                System.out.println("📁 File A: " + file1 + " (" + fileALines.size() + " lines)");
                System.out.println("📁 File B: " + file2 + " (" + fileBLines.size() + " lines)");
                
                // Run DiffAlgorithm
                DiffAlgorithm diff = new DiffAlgorithm();
                DiffAlgorithm.DiffResult result = diff.computeDiff(fileALines, fileBLines);
                
                printResults(result);
                
            } catch (IOException e) {
                System.err.println("❌ Error reading files: " + e.getMessage());
                System.err.println("💡 Make sure the file paths are correct and files exist!");
            }
        }
        
        private static void printResults(DiffAlgorithm.DiffResult result) {
            System.out.println("\n🔧 EDIT OPERATIONS FOUND: " + result.editScript.size());
            for (DiffAlgorithm.EditOperation op : result.editScript) {
                System.out.println("   " + op.type + " - Line " + (op.rightLine + 1) + ": " + 
                    (op.content.length() > 50 ? op.content.substring(0, 50) + "..." : op.content));
            }
            
            System.out.println("\n📊 FILE A ANALYSIS (First 20 lines):");
            for (int i = 0; i < Math.min(result.leftList.size(), 20); i++) {
                DiffAlgorithm.DiffLine line = result.leftList.get(i);
                System.out.printf("   Line %2d: [%-10s] %s%n", 
                    (i + 1), line.changeType, line.content);
            }
            if (result.leftList.size() > 20) {
                System.out.println("   ... and " + (result.leftList.size() - 20) + " more lines");
            }
            
            System.out.println("\n📊 FILE B ANALYSIS (First 20 lines):");
            for (int i = 0; i < Math.min(result.rightList.size(), 20); i++) {
                DiffAlgorithm.DiffLine line = result.rightList.get(i);
                System.out.printf("   Line %2d: [%-10s] %s%n", 
                    (i + 1), line.changeType, line.content);
            }
            if (result.rightList.size() > 20) {
                System.out.println("   ... and " + (result.rightList.size() - 20) + " more lines");
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