package com.kk.LineMappingProject;

/*
To RUN:

cd 3110_project/LineMappingProject
mvn clean compile
java -cp target/classes com.kk.LineMappingProject.Phase4Validation

 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Phase4Validation {

    public static void main(String[] args) {
        System.out.println("=== Phase 4 Validation Suite ===\n");

        runTestSame();
        runTestInsert();
        runTestDelete();
        runTestMove();
        runTestModify();

        System.out.println("\n=== Validation complete ===");
    }

    // ---------- Helper: build brute-force candidate map ----------

    private static Map<Integer, List<Integer>> buildBruteForceCandidates(int leftSize, int rightSize) {
        Map<Integer, List<Integer>> candidateMap = new HashMap<Integer, List<Integer>>();
        for (int i = 0; i < leftSize; i++) {
            List<Integer> candidates = new ArrayList<Integer>();
            for (int j = 0; j < rightSize; j++) {
                candidates.add(j);
            }
            candidateMap.put(i, candidates);
        }
        return candidateMap;
    }

    static List<LineMatch> runPhase4(List<String> leftLines, List<String> rightLines) {
        SimilarityMapper sm = new SimilarityMapper();
        Map<Integer, List<Integer>> candidateMap =
                buildBruteForceCandidates(leftLines.size(), rightLines.size());

        List<LineMatch> initial = sm.computeInitialMatches(leftLines, rightLines, candidateMap);
        return sm.classifyChanges(initial, rightLines.size());
    }

    private static void printMatches(List<LineMatch> matches) {
        for (LineMatch m : matches) {
            System.out.println("  " + m);
        }
    }

    private static Counts countTypes(List<LineMatch> matches) {
        int unchanged = 0, modified = 0, added = 0, deleted = 0, moved = 0;
        for (LineMatch m : matches) {
            ChangeType t = m.getChangeType();
            if (t == ChangeType.UNCHANGED) unchanged++;
            else if (t == ChangeType.MODIFIED) modified++;
            else if (t == ChangeType.ADDED) added++;
            else if (t == ChangeType.DELETED) deleted++;
            else if (t == ChangeType.MOVED) moved++;
        }
        return new Counts(unchanged, modified, added, deleted, moved);
    }

    private static void printCounts(Counts c) {
        System.out.println("  Unchanged: " + c.unchanged);
        System.out.println("  Modified : " + c.modified);
        System.out.println("  Added    : " + c.added);
        System.out.println("  Deleted  : " + c.deleted);
        System.out.println("  Moved    : " + c.moved);
    }

    private static class Counts {
        int unchanged;
        int modified;
        int added;
        int deleted;
        int moved;

        Counts(int u, int m, int a, int d, int mv) {
            this.unchanged = u;
            this.modified = m;
            this.added = a;
            this.deleted = d;
            this.moved = mv;
        }
    }

    // ---------- Test 1: identical files ----------

    private static void runTestSame() {
        System.out.println("Test 1: Identical files");

        List<String> A = new ArrayList<String>();
        A.add("int x = 1;");
        A.add("int y = 2;");
        A.add("int z = 3;");

        List<String> B = new ArrayList<String>(A);

        List<LineMatch> matches = runPhase4(A, B);
        printMatches(matches);
        Counts c = countTypes(matches);
        printCounts(c);

        boolean pass = (c.unchanged == 3 && c.modified == 0 && c.added == 0 && c.deleted == 0);
        System.out.println("  Result: " + (pass ? "PASS" : "FAIL"));
        System.out.println();
    }

    // ---------- Test 2: insertion ----------

    private static void runTestInsert() {
        System.out.println("Test 2: Insert one new line in B");

        List<String> A = new ArrayList<String>();
        A.add("int x = 1;");
        A.add("int y = 2;");
        A.add("int z = 3;");

        List<String> B = new ArrayList<String>();
        B.add("int x = 1;");
        B.add("int y = 2;");
        B.add("int w = 4; // new line");
        B.add("int z = 3;");

        List<LineMatch> matches = runPhase4(A, B);
        printMatches(matches);
        Counts c = countTypes(matches);
        printCounts(c);

        boolean pass = (c.unchanged == 3 && c.added == 1 && c.deleted == 0);
        System.out.println("  Result: " + (pass ? "PASS" : "FAIL"));
        System.out.println();
    }

    // ---------- Test 3: deletion ----------

    private static void runTestDelete() {
        System.out.println("Test 3: Delete one line in B");

        List<String> A = new ArrayList<String>();
        A.add("int x = 1;");
        A.add("int y = 2;");
        A.add("int z = 3;");

        List<String> B = new ArrayList<String>();
        B.add("int x = 1;");
        B.add("int z = 3;");

        List<LineMatch> matches = runPhase4(A, B);
        printMatches(matches);
        Counts c = countTypes(matches);
        printCounts(c);

        boolean pass = (c.unchanged == 2 && c.deleted == 1 && c.added == 0);
        System.out.println("  Result: " + (pass ? "PASS" : "FAIL"));
        System.out.println();
    }

    // ---------- Test 4: move / reordering ----------

    private static void runTestMove() {
        System.out.println("Test 4: Reordering / move");

        List<String> A = new ArrayList<String>();
        A.add("int a = 1;");
        A.add("int b = 2;");
        A.add("int c = 3;");
        A.add("int d = 4;");

        List<String> B = new ArrayList<String>();
        B.add("int a = 1;");
        B.add("int d = 4;");
        B.add("int b = 2;");
        B.add("int c = 3;");

        List<LineMatch> matches = runPhase4(A, B);
        printMatches(matches);
        Counts c = countTypes(matches);
        printCounts(c);

        // For this test we only assert that all lines are matched (no pure adds or deletes).
        boolean pass = (c.added == 0 && c.deleted == 0);
        System.out.println("  Result: " + (pass ? "PASS" : "FAIL"));
        System.out.println();
    }

    // ---------- Test 5: small modification ----------

    private static void runTestModify() {
        System.out.println("Test 5: Small modification");

        List<String> A = new ArrayList<String>();
        A.add("int count = 10;");

        List<String> B = new ArrayList<String>();
        B.add("int count = 11; // changed");

        List<LineMatch> matches = runPhase4(A, B);
        printMatches(matches);
        Counts c = countTypes(matches);
        printCounts(c);

        boolean pass = (matches.size() == 1
                && matches.get(0).getChangeType() == ChangeType.MODIFIED
                && matches.get(0).getSimilarity() > 0.5
                && matches.get(0).getSimilarity() <= 1.0);

        System.out.println("  Result: " + (pass ? "PASS" : "FAIL"));
        System.out.println();
    }
}
