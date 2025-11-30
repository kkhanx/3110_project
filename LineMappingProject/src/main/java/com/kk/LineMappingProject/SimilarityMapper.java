package com.kk.LineMappingProject;

import java.util.*;

// Mahnoz Akhtari, 105011198
// Phase 4

public class SimilarityMapper {

    private static final double SIMILARITY_THRESHOLD = 0.5;

    /**
     * Compute normalized Levenshtein similarity between two lines.
     * similarity = 1 - (distance / maxLen)
     */
    public double computeSimilarity(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";

        if (a.isEmpty() && b.isEmpty()) {
            return 1.0;
        }

        int distance = levenshteinDistance(a, b);
        int maxLen = Math.max(a.length(), b.length());

        return 1.0 - ((double) distance / (double) maxLen);
    }

    /**
     * Standard dynamic-programming Levenshtein distance.
     */
    private int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= b.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= a.length(); i++) {
            char ca = a.charAt(i - 1);
            for (int j = 1; j <= b.length(); j++) {
                char cb = b.charAt(j - 1);
                int cost = (ca == cb) ? 0 : 1;

                dp[i][j] = min3(
                        dp[i - 1][j] + 1,      // deletion
                        dp[i][j - 1] + 1,      // insertion
                        dp[i - 1][j - 1] + cost // substitution
                );
            }
        }

        return dp[a.length()][b.length()];
    }

    private int min3(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }
        /**
     * Phase 4.1: For each line in L, pick the best candidate in R based on similarity.
     * Returns a list of LineMatch (one per left line).
     */
    public List<LineMatch> computeInitialMatches(
            List<String> leftLines,
            List<String> rightLines,
            Map<Integer, List<Integer>> candidateMap
    ) {
        List<LineMatch> matches = new ArrayList<LineMatch>();

        int nLeft = leftLines.size();
        for (int i = 0; i < nLeft; i++) {
            String left = leftLines.get(i);
            List<Integer> candidates = candidateMap.get(i);

            if (candidates == null || candidates.isEmpty()) {
                // No candidates at all for this line -> DELETED (for now)
                matches.add(new LineMatch(i, -1, ChangeType.DELETED, 0.0));
                continue;
            }

            int bestRightIdx = -1;
            double bestSim = 0.0;

            for (Integer rIdx : candidates) {
                if (rIdx < 0 || rIdx >= rightLines.size()) {
                    continue;
                }
                String right = rightLines.get(rIdx);
                double sim = computeSimilarity(left, right);

                if (sim > bestSim) {
                    bestSim = sim;
                    bestRightIdx = rIdx;
                }
            }

            if (bestRightIdx == -1 || bestSim < SIMILARITY_THRESHOLD) {
                // No good candidate
                matches.add(new LineMatch(i, -1, ChangeType.DELETED, bestSim));
            } else {
                // classify as UNCHANGED vs MODIFIED vs MOVED later
                matches.add(new LineMatch(i, bestRightIdx, ChangeType.MODIFIED, bestSim));
            }
        }

        return matches;
    }
        /**
     * Refine change types: UNCHANGED, MODIFIED, MOVED, DELETED, ADDED.
     * For now, ADDED will be handled using unmatched right lines separately.
     */
    public List<LineMatch> classifyChanges(
            List<LineMatch> initialMatches,
            int rightLineCount
    ) {
        // Track which right indices are already used
        boolean[] rightUsed = new boolean[rightLineCount];
        Arrays.fill(rightUsed, false);

        List<LineMatch> refined = new ArrayList<LineMatch>();

        for (LineMatch m : initialMatches) {
            int leftIdx = m.getLeftIndex();
            int rightIdx = m.getRightIndex();
            double sim = m.getSimilarity();

            if (rightIdx == -1) {
                refined.add(new LineMatch(leftIdx, -1, ChangeType.DELETED, sim));
                continue;
            }

            if (rightUsed[rightIdx]) {
                // This right line is already taken by a better/earlier match
                refined.add(new LineMatch(leftIdx, -1, ChangeType.DELETED, sim));
                continue;
            }

            rightUsed[rightIdx] = true;

            // UNCHANGED if similarity is very high (~exact)
            if (sim >= 0.99 && Math.abs(leftIdx - rightIdx) <= 1) {
                refined.add(new LineMatch(leftIdx, rightIdx, ChangeType.UNCHANGED, sim));
            } else {
                // If positions differ a lot, mark as MOVED; otherwise MODIFIED
                if (Math.abs(leftIdx - rightIdx) > 3) {
                    refined.add(new LineMatch(leftIdx, rightIdx, ChangeType.MOVED, sim));
                } else {
                    refined.add(new LineMatch(leftIdx, rightIdx, ChangeType.MODIFIED, sim));
                }
            }
        }

        // Handle ADDED lines: any right index not used at all
        for (int r = 0; r < rightLineCount; r++) {
            if (!rightUsed[r]) {
                // This line exists only in B (Right) -> ADDED
                refined.add(new LineMatch(-1, r, ChangeType.ADDED, 0.0));
            }
        }

        return refined;
    }
}
