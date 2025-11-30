package com.kk.LineMappingProject;
//Hajer Alchami 

import java.util.*;

public class SimHash {

    public static long computeContentHash(String line) {
        List<String> tokens = tokenize(line);
        return generateHash(tokens);
    }

    public static long computeContextHash(List<String> lines, int index, int window) {
        List<String> context = new ArrayList<>();
        int start = Math.max(0, index - window);
        int end = Math.min(lines.size() - 1, index + window);

        for (int i = start; i <= end; i++) {
            context.addAll(tokenize(lines.get(i)));
        }

        return generateHash(context);
    }

    private static List<String> tokenize(String line) {
        return Arrays.asList(line.split("\\W+"));
    }

    private static long generateHash(List<String> tokens) {
        int[] bitVector = new int[64];

        for (String t : tokens) {
            long h = t.hashCode();

            for (int i = 0; i < 64; i++) {
                long mask = 1L << i;
                if ((h & mask) != 0)
                    bitVector[i]++;
                else
                    bitVector[i]--;
            }
        }

        long result = 0L;
        for (int i = 0; i < 64; i++) {
            if (bitVector[i] > 0) result |= (1L << i);
        }

        return result;
    }
}
