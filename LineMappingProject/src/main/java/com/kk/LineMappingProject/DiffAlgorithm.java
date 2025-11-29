package com.kk.LineMappingProject;

import java.util.*;

public class DiffAlgorithm {
 
    public static class DiffResult {
        public List<DiffLine> leftList;
        public List<DiffLine> rightList;
        public List<EditOperation> editScript;
        
        public DiffResult(List<DiffLine> leftList, List<DiffLine> rightList, List<EditOperation> editScript) {
            this.leftList = leftList;
            this.rightList = rightList;
            this.editScript = editScript;
        }
    }
    
    public static class DiffLine {
        public String content;
        public ChangeType changeType;
        public int originalLineNumber;
        
        public DiffLine(String content, ChangeType changeType, int originalLineNumber) {
            this.content = content;
            this.changeType = changeType;
            this.originalLineNumber = originalLineNumber;
        }
    }
    
    public enum ChangeType {
        UNCHANGED, ADDED, DELETED, MODIFIED
    }
    
    public static class EditOperation {
        public OperationType type;
        public int leftLine;
        public int rightLine;
        public String content;
        
        public EditOperation(OperationType type, int leftLine, int rightLine, String content) {
            this.type = type;
            this.leftLine = leftLine;
            this.rightLine = rightLine;
            this.content = content;
        }
    }
    
    public enum OperationType {
        ADD, DELETE, CHANGE
    }
    
    // STEP 1: Implement LCS-based Diff
    public DiffResult computeDiff(List<String> fileA, List<String> fileB) {
        int[][] lcsMatrix = computeLCSMatrix(fileA, fileB);
        List<EditOperation> editScript = computeEditScript(lcsMatrix, fileA, fileB);
        
        List<DiffLine> leftList = createAnnotatedList(fileA, editScript, true);
        List<DiffLine> rightList = createAnnotatedList(fileB, editScript, false);
        
        return new DiffResult(leftList, rightList, editScript);
    }
    
    private int[][] computeLCSMatrix(List<String> fileA, List<String> fileB) {
        int m = fileA.size();
        int n = fileB.size();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (fileA.get(i - 1).equals(fileB.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp;
    }
    
    // STEP 2: Generate Edit Script
    private List<EditOperation> computeEditScript(int[][] lcs, List<String> fileA, List<String> fileB) {
        List<EditOperation> script = new ArrayList<>();
        int i = fileA.size();
        int j = fileB.size();
        
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && fileA.get(i - 1).equals(fileB.get(j - 1))) {
                i--;
                j--;
            } else if (j > 0 && (i == 0 || lcs[i][j - 1] >= lcs[i - 1][j])) {
                // Added lines (present in B, not in A)
                script.add(0, new EditOperation(OperationType.ADD, i, j - 1, fileB.get(j - 1)));
                j--;
            } else if (i > 0 && (j == 0 || lcs[i][j - 1] < lcs[i - 1][j])) {
                // Deleted lines (present in A, not in B)
                script.add(0, new EditOperation(OperationType.DELETE, i - 1, j, fileA.get(i - 1)));
                i--;
            }
        }
        return script;
    }
    
    // STEP 3: Create Change Lists
    private List<DiffLine> createAnnotatedList(List<String> lines, List<EditOperation> script, boolean isLeft) {
        List<DiffLine> annotated = new ArrayList<>();
        Set<Integer> changedLines = new HashSet<>();
        
        for (EditOperation op : script) {
            if (isLeft && (op.type == OperationType.DELETE || op.type == OperationType.CHANGE)) {
                changedLines.add(op.leftLine);
            } else if (!isLeft && (op.type == OperationType.ADD || op.type == OperationType.CHANGE)) {
                changedLines.add(op.rightLine);
            }
        }
        
        for (int i = 0; i < lines.size(); i++) {
            // Changed lines (modified between versions)
            ChangeType changeType = changedLines.contains(i) ? ChangeType.MODIFIED : ChangeType.UNCHANGED;
            annotated.add(new DiffLine(lines.get(i), changeType, i + 1));
        }
        
        return annotated;
    }
}