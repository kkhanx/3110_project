package com.kk.LineMappingProject;
//Hajer 
import java.nio.file.*;
import java.util.*;

public class TestFullPipeline {

    public static void main(String[] args) {

        try {
            System.out.println("=== FULL PIPELINE TEST (Phase 1 → Phase 4) ===");

            // Use same files the group uses
            String fileA = "normalizedSampleVersion1";
            String fileB = "normalizedSampleVersion2";

            List<String> leftLines = Files.readAllLines(Paths.get(fileA));
            List<String> rightLines = Files.readAllLines(Paths.get(fileB));

            // PHASE 2: Diff Algorithm
            DiffAlgorithm diff = new DiffAlgorithm();
            DiffAlgorithm.DiffResult diffResult = diff.computeDiff(leftLines, rightLines);

            // Extract pure strings for Phase 3 
            List<String> leftPure = diffResult.leftList.stream()
                    .map(l -> l.content)
                    .toList();

            List<String> rightPure = diffResult.rightList.stream()
                    .map(l -> l.content)
                    .toList();

            // PHASE 3: Candidate Generator
            Map<Integer, List<Integer>> candidateMap =
                    CandidateGenerator.buildCandidates(leftPure, rightPure, 15);

            System.out.println("\n=== Phase 3: Candidate Map ===");
            for (int i = 0; i < candidateMap.size(); i++) {
                System.out.println("Left[" + i + "] -> " + candidateMap.get(i));
            }

            // PHASE 4: Similarity Mapper
            SimilarityMapper mapper = new SimilarityMapper();
            List<LineMatch> matches =
                    mapper.computeInitialMatches(leftPure, rightPure, candidateMap);

            System.out.println("\n=== Phase 4: Line Match Results ===");
            for (LineMatch m : matches) {
                System.out.println(m);
            }

            System.out.println("\n=== FULL PIPELINE COMPLETE ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
