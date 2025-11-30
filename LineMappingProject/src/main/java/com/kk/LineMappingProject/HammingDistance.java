package com.kk.LineMappingProject;

public class HammingDistance {
    public static int of(long a, long b) {
        return Long.bitCount(a ^ b);
    }
}

