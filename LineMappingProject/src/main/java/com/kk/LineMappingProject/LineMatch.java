package com.kk.LineMappingProject;

// Mahnoz Akhtari, 105011198
// Phase 4
public class LineMatch {
    private final int leftIndex;     // line index in A (L)
    private final int rightIndex;    // line index in B (R), -1 if none
    private final ChangeType changeType;
    private final double similarity;

    public LineMatch(int leftIndex, int rightIndex, ChangeType changeType, double similarity) {
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
        this.changeType = changeType;
        this.similarity = similarity;
    }

    public int getLeftIndex() {
        return leftIndex;
    }

    public int getRightIndex() {
        return rightIndex;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public double getSimilarity() {
        return similarity;
    }

    @Override
    public String toString() {
        return "LineMatch{" +
                "leftIndex=" + leftIndex +
                ", rightIndex=" + rightIndex +
                ", changeType=" + changeType +
                ", similarity=" + similarity +
                '}';
    }
}
