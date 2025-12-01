package com.kk.LineMappingProject;

import java.util.*;
import com.kk.LineMappingProject.SimHash;
import com.kk.LineMappingProject.HammingDistance;

// Mahnoz Akhtari, 105011198
public class CandidateGenerator {

    private static final double CONTENT_WEIGHT = 0.7;
    private static final double CONTEXT_WEIGHT = 0.3;

    // Helper class to keep score as double (no lossy cast)
    private static class Candidate {
        int index;
        double score;

        Candidate(int index, double score) {
            this.index = index;
            this.score = score;
        }
    }

    public static Map<Integer, List<Integer>> buildCandidates(
            List<String> leftLines,
            List<String> rightLines,
            int topK
    ) {
        Map<Integer, List<Integer>> candidateMap = new HashMap<>();

        long[] leftContent = new long[leftLines.size()];
        long[] leftContext = new long[leftLines.size()];
        long[] rightContent = new long[rightLines.size()];
        long[] rightContext = new long[rightLines.size()];

        // Precompute SimHashes for content + context
        for (int i = 0; i < leftLines.size(); i++) {
            leftContent[i] = SimHash.computeContentHash(leftLines.get(i));
            leftContext[i] = SimHash.computeContextHash(leftLines, i, 3);
        }

        for (int j = 0; j < rightLines.size(); j++) {
            rightContent[j] = SimHash.computeContentHash(rightLines.get(j));
            rightContext[j] = SimHash.computeContextHash(rightLines, j, 3);
        }

        // For each left line, score all right lines and keep top-K
        for (int i = 0; i < leftLines.size(); i++) {

            List<Candidate> scored = new ArrayList<>();

            for (int j = 0; j < rightLines.size(); j++) {
                int contentDist = HammingDistance.of(leftContent[i], rightContent[j]);
                int contextDist = HammingDistance.of(leftContext[i], rightContext[j]);

                double combined = CONTENT_WEIGHT * contentDist +
                                  CONTEXT_WEIGHT * contextDist;

                scored.add(new Candidate(j, combined));
            }

            // Sort by combined score ascending (smaller distance = better)
            scored.sort(Comparator.comparingDouble(c -> c.score));

            List<Integer> topList = new ArrayList<>();
            for (int k = 0; k < Math.min(topK, scored.size()); k++) {
                topList.add(scored.get(k).index);
            }

            candidateMap.put(i, topList);
        }

        return candidateMap;
    }
}
