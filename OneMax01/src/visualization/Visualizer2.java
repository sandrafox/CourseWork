package visualization;

import geneticalgorithms.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.util.LogFormat;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
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

public class Visualizer2 extends ApplicationFrame {
    private JFreeChart xylineChart;
    private static FileWriter myWriter;

    public Visualizer2(String title, String chartTitle) throws GAException, IOException {
        super(title);

        myWriter = new FileWriter("VectorWith0.1HeNew-i1sf.out");

        xylineChart = ChartFactory.createXYLineChart(
                title,
                "Длина особи",
                "Количество поколений",
                createDataset() ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 1000 , 800 ) );
        final XYPlot plot = xylineChart.getXYPlot( );
        LogAxis yAxis = new LogAxis("Количество вычислений функции (10^)");
        yAxis.setBase(10);
        LogFormat format = new LogFormat(yAxis.getBase(), "", "", true);
        yAxis.setNumberFormatOverride(format);
        yAxis.setLabelFont(new Font("Helvetica", Font.BOLD, 16));
        yAxis.setTickLabelFont(new Font("Helvetica", Font.BOLD, 16));
        plot.setRangeAxis(yAxis);
        LogAxis xAxis = new LogAxis("Значение фитнесс функции (2^)");
        xAxis.setBase(2);
        LogFormat format2 = new LogFormat(xAxis.getBase(), "", "", true);
        xAxis.setNumberFormatOverride(format2);
        xAxis.setLabelFont(new Font("Helvetica", Font.BOLD, 16));
        xAxis.setTickLabelFont(new Font("Helvetica", Font.BOLD, 16));
        plot.setDomainAxis(xAxis);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesPaint( 1 , Color.BLUE );
        renderer.setSeriesPaint( 2 , Color.MAGENTA );
        renderer.setSeriesPaint(3, Color.GREEN);
        renderer.setSeriesStroke( 0 , new BasicStroke( 2.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 2.0f ) );
        renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
        renderer.setSeriesStroke(3, new BasicStroke(2.0f));
        //renderer.setSeriesLinesVisible(0, false);
        //renderer.setSeriesLinesVisible(1, false);
        //renderer.setBaseLegendShape(new Rectangle2D(-40.0D, ));
        renderer.setBaseLegendTextFont(new Font("Helvetica", Font.BOLD, 16));
        plot.setRenderer( renderer );
        setContentPane( chartPanel );


    }

    public XYDataset createDataset( ) throws GAException, IOException {
        GeneticAlgorithm ga;
        //final XYSeries ean = new XYSeries( "(1+1) 1/N");
        //final XYSeries ea2n = new XYSeries( "(1+1) 1/2N" );
        //final XYSeries gan = new XYSeries("(2+1) 1/N");
        final XYSeries ga2n = new XYSeries("(2+1) 1/2N");

        myWriter.write("[{}\n");
        final int nRuns = 100;
        for (int n1 = 776; n1 <= 856; n1 += 8) { // 776 - 856
            final int n = n1;
            //final int n = 1 << N;
            //final int n1 = (int) Math.ceil(Math.pow(2, N - 0.7)), n2 = (int) Math.ceil(Math.pow(2, N - 0.3)), n = 1 << N;
            /*addGenerationsToDataSet(() -> new GADiploidWithTable(1, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.SBM, generateVector(n, 0.1), 1.0 / n),
                    ean, nRuns, 2 * n);
            //addInfoToDataSet(ga, ean, 2 * N);
            addGenerationsToDataSet(() -> new GADiploidWithTable(1, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.SBM, generateVector(n, 0.1), 0.5 / n),
                    ea2n, nRuns, 2 * n);
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDY, generateVector(n, 0.1), 1.0 / n),
                    gan, nRuns, 2 * n);*/
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDY, generateVector(n, 0.1), 0.5 / n),
                    ga2n, nRuns, 2 * n);
            //addInfoToDataSet(ga, ea2n, 2 * N);*/
            /*addGenerationsToDataSet(() -> new GADiploidWithTable(1, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.SBMMOD, generateVector(n, 0.1), 1.0 / n),
                    gan, nRuns, 2 * n);
            addGenerationsToDataSet(() -> new GADiploidWithTable(1, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.SBMMOD, generateVector(n, 0.1), 0.5 / n),
                    ga2n, nRuns, 2 * n);
            //addInfoToDataSet(ga, gan, 2 * N);*/
//            ga = new GADiploidWithTable(2, N, -1, 0.5, TypeSelectionParents.SUS,
//                    TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDYMOD, generateVector(N, 0.1),
//                    1 / (double)(N));
            //addGenerationsToDataSet(ga, ean, 2 * N);
//            ga = new GADiploidWithTable(2, N, -1, 0.5, TypeSelectionParents.SUS,
//                    TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDYMOD, generateVector(N, 0.1),
//                    1 / (double)(2 * N));
            //addGenerationsToDataSet(ga, ea2n, 2 * N);*/
            /*addGenerationsToDataSet(() -> new GADiploidCycleWithAverage(1, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.SBM),
                    ean, nRuns, n);
            addGenerationsToDataSet(() -> new GADiploidCycleWithAverage(2, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDY),
                    gan, nRuns, n);
            addGenerationsToDataSet(() -> new GAMonoid(1, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.SBM),
                    ea2n, nRuns, n);
            addGenerationsToDataSet(() -> new GAMonoid(1, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.RLS),
                    ga2n, nRuns, n);
            /*addGenerationsToDataSet(() -> new GADiploidWithDominance(1, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, DominanceType.XOR, AlgorithmType.SBM),
                    ean, nRuns, n);*/
            /*addGenerationsToDataSet(() -> new GADiploidWithDominance(2, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, DominanceType.XOR, AlgorithmType.GREEDY),
                    gan, nRuns, n);*/
            /*if (n1 != n) {
                addGenerationsToDataSet(() -> new GADiploidWithTable(1, n1, -1, 0.5, TypeSelectionParents.SUS,
                                TypeSelectionSurvival.FITNESS, AlgorithmType.SBM, generateVector(n1, 0.1), 1.0 / n1),
                        ean, nRuns, 2 * n1);
                addGenerationsToDataSet(() -> new GADiploidWithTable(1, n1, -1, 0.5, TypeSelectionParents.SUS,
                                TypeSelectionSurvival.FITNESS, AlgorithmType.SBM, generateVector(n1, 0.1), 0.5 / n1),
                        ea2n, nRuns, 2 * n1);
                addGenerationsToDataSet(() -> new GADiploidWithTable(2, n1, -1, 0.5, TypeSelectionParents.SUS,
                                TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDY, generateVector(n1, 0.1), 1.0 / n1),
                        gan, nRuns, 2 * n1);
                addGenerationsToDataSet(() -> new GADiploidWithTable(2, n1, -1, 0.5, TypeSelectionParents.SUS,
                                TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDY, generateVector(n1, 0.1), 0.5 / n1),
                        ga2n, nRuns, 2 * n1);
            }
            if (n2 != n) {
                addGenerationsToDataSet(() -> new GADiploidWithTable(1, n2, -1, 0.5, TypeSelectionParents.SUS,
                                TypeSelectionSurvival.FITNESS, AlgorithmType.SBM, generateVector(n2, 0.1), 1.0 / n2),
                        ean, nRuns, 2 * n2);
                addGenerationsToDataSet(() -> new GADiploidWithTable(1, n2, -1, 0.5, TypeSelectionParents.SUS,
                                TypeSelectionSurvival.FITNESS, AlgorithmType.SBM, generateVector(n2, 0.1), 0.5 / n2),
                        ea2n, nRuns, 2 * n2);
                addGenerationsToDataSet(() -> new GADiploidWithTable(2, n2, -1, 0.5, TypeSelectionParents.SUS,
                                TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDY, generateVector(n2, 0.1), 1.0 / n2),
                        gan, nRuns, 2 * n2);
                addGenerationsToDataSet(() -> new GADiploidWithTable(2, n2, -1, 0.5, TypeSelectionParents.SUS,
                                TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDY, generateVector(n2, 0.1), 0.5 / n2),
                        ga2n, nRuns, 2 * n2);
            }
            /*addGenerationsToDataSet(() -> new GAMonoid(1, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.SBM),
                    ea2n, nRuns, n);*/
            /*addGenerationsToDataSet(() -> new GAMonoid(1, n, -1, 0.5, TypeSelectionParents.SUS,
                            TypeSelectionSurvival.FITNESS, AlgorithmType.RLS),
                    ga2n, nRuns, n);*/
        }
        myWriter.write("]");

        final XYSeriesCollection dataset = new XYSeriesCollection( );
        //dataset.addSeries(ean);
        //dataset.addSeries(ea2n);
        //dataset.addSeries(gan);
        dataset.addSeries(ga2n);
        return dataset;
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

    private void addInfoToDataSet(GeneticAlgorithm ga, XYSeries s, int maxValue) throws GAException, IOException {
        ga.evalPopulation();
        int generations = 1;
        while (!ga.isTerminated(maxValue)) {
            //System.out.println(ga.getMaximalFitness());
            s.add(generations, ga.getMaximalFitness());
            ga.newGeneration();
            generations++;
        }
        myWriter.write(generations);
        s.add(generations, ga.getMaximalFitness());
    }

    private static void addGenerationsToDataSet(Supplier<GeneticAlgorithm> gaSup, XYSeries s, int nRuns, int maxValue) throws GAException, IOException {
        double sumGenerations = 0;
        java.util.List<String> starts = new ArrayList<>();
        List<int[]> runAndFitness = new ArrayList<>();
        for (int i = 0; i < nRuns; ++i) {
            GeneticAlgorithm ga = gaSup.get();
            ga.evalPopulation();
            int generations = 1;
            int fitness = 0;
            int[] raf = {0, ga.getMaximalFitness(), starts.size()};
            runAndFitness.add(raf);
            starts.add(ga.getIndividualWithMaximalFitness().toString());
            while ((!ga.isTerminated(maxValue))) {
                ga.newGeneration();
                generations++;
            }
            if (ga instanceof GADiploidCycleWithAverage) generations *= 2;
            runAndFitness.get(runAndFitness.size() - 1)[0] = generations;
            //myWriter.write(",{\"fitness\":" + maxValue + ",\"runtime\":" + generations + ",\"start\":" + ind + "}\n");
            sumGenerations += generations;
        }
        double meanRun = sumGenerations / nRuns;
        s.add(maxValue, meanRun);
        for (int[] raf : runAndFitness.stream().filter(r -> r[0] > meanRun).collect(Collectors.toList())) {
            myWriter.write(",{\"fitness\":" + raf[1] + ",\"runtime\":" + raf[0] + ",\"start\":" + starts.get(raf[2]) + "}\n");
        }
        myWriter.write(",{\"fitness\":" + maxValue + ",\"runtime mean\":" + meanRun + "}\n");
        System.out.println(maxValue);
    }

    public void savePlot() throws IOException {
        int width = 1000;   /* Width of the image */
        int height = 800;  /* Height of the image */
        File XYChart = new File( "VectorWith0.1HeNew-i1.jpeg" );
        ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
        myWriter.close();
    }

    public static void main(String[] args) throws GAException, IOException {
        Visualizer2 chart = new Visualizer2("Прогресс генетических алгоритмов","Diploid genetic algorithm");
        chart.savePlot();
    }
}
