package visualization;

import geneticalgorithms.*;
import parentselectors.TypeSelectionParents;
import survivalselectors.TypeSelectionSurvival;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RealOneMaxHe {
    private static FileWriter myWriter;

    public RealOneMaxHe (String title) throws IOException {
        myWriter = new FileWriter(title);
    }

    public void createDataset() throws IOException, GAException {
        GeneticAlgorithm ga;

        myWriter.write("[{}\n");
        final int nRuns = 100;
        for (int N = 5; N <= 10; ++N) {
            final int n1 = (int) Math.ceil(Math.pow(2, N - 0.7)), n2 = (int) Math.ceil(Math.pow(2, N - 0.3)), n = 1 << N;
            myWriter.write(",{\"fitness\":" + (2*n1));
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n1, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n1, 0.1), 0.5 / n1), nRuns, 2 * n1, "05");
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n1, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n1, 0.1), 2. / n1), nRuns, 2 * n1, "2");
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n1, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n1, 0.1), 1. / n1), nRuns, 2 * n1, "1");
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n1, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n1, 0.1), 0.25 / n1), nRuns, 2 * n1, "025");
            myWriter.write("}\n");
            myWriter.write(",{\"fitness\":" + (2*n2));
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n2, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n2, 0.1), 0.5 / n2), nRuns, 2 * n2, "05");
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n2, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n2, 0.1), 2. / n2), nRuns, 2 * n2, "2");
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n2, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n2, 0.1), 1. / n2), nRuns, 2 * n2, "1");
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n2, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n2, 0.1), 0.25 / n2), nRuns, 2 * n2, "025");
            myWriter.write("}\n");
            myWriter.write(",{\"fitness\":" + (2*n));
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n, 0.1), 0.5 / n), nRuns, 2 * n, "05");
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n1, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n, 0.1), 2. / n), nRuns, 2 * n, "2");
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n, 0.1), 1. / n), nRuns, 2 * n, "1");
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, 
            AlgorithmType.SBM, generateVector(n, 0.1), 0.25 / n), nRuns, 2 * n, "025");
            myWriter.write("}\n");
        }
        myWriter.close();
    }

    private int[] generateVector(int length, double probabilityHe) {
        double probability0 = probabilityHe + (1 - probabilityHe) / 2;
        int[] vector = new int[length];
        for (int i = 0; i < length; ++i) {
            double p = Math.random();
            if (p <= probabilityHe) {
                vector[i] = 1;
            } else if (p <= probability0) {
                vector[i] = 0;
            } else {
                vector[i] = 2;
            }
        }
        return vector;
    }

    private static void addGenerationsToDataSet(Supplier<GeneticAlgorithm> gaSup, int nRuns, int maxValue, String algName) throws GAException, IOException {
        Duration sumGenerations = Duration.ZERO;
        List<Duration> runs = new ArrayList<>();
        for (int i = 0; i < nRuns; ++i) {
            GeneticAlgorithm ga = gaSup.get();
            ga.evalPopulation();
            Instant start = Instant.now();
            int geners =0;
            while ((geners <= 2*maxValue * maxValue) && (!ga.isTerminated(maxValue))) {
                ga.newGeneration();
                geners++;
            }
            var generations = Duration.between(start, Instant.now());
            runs.add(generations);
            sumGenerations = sumGenerations.plus(generations);
        }
        double meanRun = ((double)sumGenerations.toMillis()) / nRuns;
        double dis = 0;
        for (Duration generation : runs) {
            double gens = (double) generation.toMillis();
            dis += (gens - meanRun) * (gens - meanRun) / nRuns;
        }
        dis = Math.sqrt(dis);
        myWriter.write(",\"runtime_mean" + algName + "\":" + meanRun + ",\"runtime_disp" + algName + "\":" + dis);
        System.out.println(maxValue);
    }

    public static void main(String[] args) throws IOException, GAException {
        RealOneMaxHe chart = new RealOneMaxHe("Time for OneMaxHomo.out");
        chart.createDataset();
    }
}