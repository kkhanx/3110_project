package com.kk.LineMappingProject;

import java.util.*;
import com.kk.LineMappingProject.SimHash;
import com.kk.LineMappingProject.HammingDistance;


public class CandidateGenerator {

    private static final double CONTENT_WEIGHT = 0.7;
    private static final double CONTEXT_WEIGHT = 0.3;

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

        for (int i = 0; i < leftLines.size(); i++) {
            leftContent[i] = SimHash.computeContentHash(leftLines.get(i));
            leftContext[i] = SimHash.computeContextHash(leftLines, i, 3);
        }

        for (int j = 0; j < rightLines.size(); j++) {
            rightContent[j] = SimHash.computeContentHash(rightLines.get(j));
            rightContext[j] = SimHash.computeContextHash(rightLines, j, 3);
        }

        for (int i = 0; i < leftLines.size(); i++) {

            List<int[]> scored = new ArrayList<>();

            for (int j = 0; j < rightLines.size(); j++) {
                int contentDist = HammingDistance.of(leftContent[i], rightContent[j]);
                int contextDist = HammingDistance.of(leftContext[i], rightContext[j]);

                double combined = CONTENT_WEIGHT * contentDist +
                        CONTEXT_WEIGHT * contextDist;

                scored.add(new int[]{j, (int) combined});
            }

            scored.sort(Comparator.comparingInt(a -> a[1]));

            List<Integer> topList = new ArrayList<>();
            for (int k = 0; k < Math.min(topK, scored.size()); k++) {
                topList.add(scored.get(k)[0]);
            }

            candidateMap.put(i, topList);
        }

        return candidateMap;
    }
}
