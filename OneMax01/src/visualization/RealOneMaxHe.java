package visualization;

import geneticalgorithms.*;
import parentselectors.TypeSelectionParents;
import survivalselectors.TypeSelectionSurvival;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RealOneMaxHe {
    private static FileWriter myWriter;

    public RealOneMaxHe (String title) throws IOException {
        myWriter = new FileWriter(title);
    }

    public void createDataset() throws IOException {
        GeneticAlgorithm ga;

        myWriter.write("[{}\n");
        final int nRuns = 100;
        for (int N = 5; N <= 11; ++N) {
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

    private static void addGenerationsToDataSet(Supplier<GeneticAlgorithm> gaSup, int nRuns, int maxValue, String algName) throws GAException, IOException {
        double sumGenerations = 0;
        List<String> starts = new ArrayList<>();
        Map<Int, Int> runs = new HashMap<>();
        for (int i = 0; i < nRuns; ++i) {
            GeneticAlgorithm ga = gaSup.get();
            ga.evalPopulation();
            int generations = 1;
            int fitness = 0;
            runAndFitness.add(raf);
            starts.add(ga.getIndividualWithMaximalFitness().toString());
            while ((!ga.isTerminated(maxValue))) {
                ga.newGeneration();
                generations++;
            }
            if (ga instanceof GADiploidCycleWithAverage) generations *= 2;
            if (runs.contains(generations)) {
                runs.put(generations, runs.get(generations) + 1);
            } else {
                runs.put(generations, 1);
            }
            sumGenerations += generations;
        }
        double meanRun = sumGenerations / nRuns;
        double dis = 0;
        for (Int gens : runs.keySet()) {
            dis += (gens - meanRun) * (gens - meanRun) * runs.get(gens) / nRuns;
        }
        myWriter.write(",\"runtime_mean" + algName + "\":" + meanRun + ",\"runtime_disp" + algName + "\":" + dis + "}\n");
        System.out.println(maxValue);
    }

    public static void main(String[] args) throws IOException {
        RealOneMaxHe chart = new RealOneMaxHe("Time for OneMaxHe");
        chart.createDataset();
    }
}