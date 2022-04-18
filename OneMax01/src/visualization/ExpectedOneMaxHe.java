package visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.util.LogFormat;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ExpectedOneMaxHe extends ApplicationFrame {
    private JFreeChart xylineChart;
    private static FileWriter myWriter;

    public ExpectedOneMaxHe(String title) throws IOException {
        super(title);

        myWriter = new FileWriter("expected time for OneMaxHe Linear Quadratic.out");

        xylineChart = ChartFactory.createXYLineChart(
                title,
                "Длина особи",
                "Ожидаемое количество вычислений",
                createDataset() ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 1000 , 800 ) );
        final XYPlot plot = xylineChart.getXYPlot( );
        //LogAxis yAxis = new LogAxis("Количество вычислений функции / длина^2");
        LogAxis yAxis = new LogAxis("Количество вычислений функции (10^)");
        yAxis.setBase(10);
        LogFormat format = new LogFormat(yAxis.getBase(), "", "", true);
        yAxis.setNumberFormatOverride(format);
        yAxis.setLabelFont(new Font("Helvetica", Font.BOLD, 16));
        yAxis.setTickLabelFont(new Font("Helvetica", Font.BOLD, 16));
        plot.setRangeAxis(yAxis);
        /*NumberAxis yAxis = new NumberAxis("Количество вычислений функции / 2^20");
        yAxis.setLabelFont(new Font("Helvetica", Font.BOLD, 16));
        yAxis.setTickLabelFont(new Font("Helvetica", Font.BOLD, 16));
        plot.setRangeAxis(yAxis);*/
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
        renderer.setSeriesStroke( 0 , new BasicStroke( 2.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 2.0f ) );
        renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
        //renderer.setSeriesLinesVisible(0, false);
        //renderer.setSeriesLinesVisible(1, false);
        //renderer.setBaseLegendShape(new Rectangle2D(-40.0D, ));
        renderer.setBaseLegendTextFont(new Font("Helvetica", Font.BOLD, 16));
        plot.setRenderer( renderer );
        setContentPane( chartPanel );
    }

    private double binomialFloat(int n, int k) {
        if (k > n - k)
            k = n - k;

        double binom = 1.0;
        for (int i = 1; i <= k; i++)
            binom = binom * (n + 1 - i) / i;
        return binom;
    }

    private BigDecimal calcP(int i, int n, BigDecimal mu) {
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        BigDecimal cur = (mu.multiply((BigDecimal.ONE.subtract(mu)).pow(2 * n - 1))).multiply((new BigDecimal(2 * n - i)).multiply(BigDecimal.TEN)), s  = BigDecimal.ZERO.add(cur);
        for (int t = 1; t <= (2* n - i - 1); t++) {
            int j = t - 1;
            cur = ((((cur.multiply(mu)).multiply(mu)).multiply(new BigDecimal(2 * n - i -j - 1))).multiply(new BigDecimal(i - n +j +1))).divide((((BigDecimal.ONE.subtract(mu)).pow(2)).multiply(new BigDecimal(j + 2))).multiply(new BigDecimal(j + 1)), mc);
            s = s.add(cur);
        }
        if (s.signum() < 0) {
            System.out.println("i = " + i + " n = " + n + " mu = " + mu);
        }
        return s;
    }

    public XYDataset createDataset( ) throws IOException {
        final XYSeries tn = new XYSeries("1/N");
        final XYSeries t2n = new XYSeries("1/2N");
        final XYSeries t05n = new XYSeries("2/N");

        myWriter.write("[{}\n");
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        for (int N = 5; N <= 11; ++N) {
            BigDecimal sn = BigDecimal.ZERO, s2n = BigDecimal.ZERO, s05n = BigDecimal.ZERO;
            final int n1 = (int) Math.ceil(Math.pow(2, N - 0.7)), n2 = (int) Math.ceil(Math.pow(2, N - 0.3)), n = 1 << N;
            BigDecimal mu1 = BigDecimal.ONE.divide(BigDecimal.valueOf(n1), mc),
                    mu2 = BigDecimal.ONE.divide(BigDecimal.valueOf(2L * n1), mc),
                    mu3 = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(n1), mc);
            for (int i = n1; i < 2*n1; i++) {
                sn = sn.add(BigDecimal.TEN.divide(calcP(i, n1, mu1), mc));
                if (sn.signum() == -1) {
                    System.out.println("1 i = " + i + " n = " + n1);
                }
                s2n = s2n.add(BigDecimal.TEN.divide(calcP(i, n1, mu2), mc));
                if (sn.signum() == -1) {
                    System.out.println("2 i = " + i + " n = " + n1);
                }
                s05n = s05n.add(BigDecimal.TEN.divide(calcP(i, n1, mu3), mc));
                if (sn.signum() == -1) {
                    System.out.println("3 i = " + i + " n = " + n1);
                }
            }
            sn = sn.divide(BigDecimal.valueOf(n1 * Math.log(n1)), mc);
            s2n = s2n.divide(BigDecimal.valueOf(n1 * Math.log(n1)), mc);
            s05n = s05n.divide(BigDecimal.valueOf(n1 * Math.log(n1)), mc);
            myWriter.write(",{\"fitness\":" + n1 + ",\"runtime\":" + sn + "}\n");
            myWriter.write(",{\"fitness\":" + n1 + ",\"runtime\":" + s2n + "}\n");
            myWriter.write(",{\"fitness\":" + n1 + ",\"runtime\":" + s05n + "}\n");
            tn.add(2*n1, sn);
            t2n.add(2*n1, s2n);
            t05n.add(2*n1, s05n);

            sn = BigDecimal.ZERO;
            s2n = BigDecimal.ZERO;
            s05n = BigDecimal.ZERO;
            mu1 = BigDecimal.ONE.divide(BigDecimal.valueOf(n), mc);
            mu2 = BigDecimal.ONE.divide(BigDecimal.valueOf(2L * n), mc);
            mu3 = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(n), mc);
            for (int i = n; i < 2*n; i++) {
                sn = sn.add(BigDecimal.TEN.divide(calcP(i, n, mu1), mc));
                s2n = s2n.add(BigDecimal.TEN.divide(calcP(i, n, mu2), mc));
                s05n = s05n.add(BigDecimal.TEN.divide(calcP(i, n, mu3), mc));
            }
            sn = sn.divide(BigDecimal.valueOf(n * Math.log(n)), mc);
            s2n = s2n.divide(BigDecimal.valueOf(n * Math.log(n)), mc);
            s05n = s05n.divide(BigDecimal.valueOf(n * Math.log(n)), mc);
            myWriter.write(",{\"fitness\":" + n + ",\"runtime\":" + sn + "}\n");
            myWriter.write(",{\"fitness\":" + n + ",\"runtime\":" + s2n + "}\n");
            myWriter.write(",{\"fitness\":" + n + ",\"runtime\":" + s05n + "}\n");
            tn.add(2*n, sn);
            t2n.add(2*n, s2n);
            t05n.add(2*n, s05n);

            sn = BigDecimal.ZERO;
            s2n = BigDecimal.ZERO;
            s05n = BigDecimal.ZERO;
            mu1 = BigDecimal.ONE.divide(BigDecimal.valueOf(n2), mc);
            mu2 = BigDecimal.ONE.divide(BigDecimal.valueOf(2L * n2), mc);
            mu3 = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(n2), mc);
            for (int i = n2; i < 2*n2; i++) {
                sn = sn.add(BigDecimal.TEN.divide(calcP(i, n2, mu1), mc));
                s2n = s2n.add(BigDecimal.TEN.divide(calcP(i, n2, mu2), mc));
                s05n = s05n.add(BigDecimal.TEN.divide(calcP(i, n2, mu3), mc));
            }
            sn = sn.divide(BigDecimal.valueOf(n2 * Math.log(n2)), mc);
            s2n = s2n.divide(BigDecimal.valueOf(n2 * Math.log(n2)), mc);
            s05n = s05n.divide(BigDecimal.valueOf(n2 * Math.log(n2)), mc);
            myWriter.write(",{\"fitness\":" + n2 + ",\"runtime\":" + sn + "}\n");
            myWriter.write(",{\"fitness\":" + n2 + ",\"runtime\":" + s2n + "}\n");
            myWriter.write(",{\"fitness\":" + n2 + ",\"runtime\":" + s05n + "}\n");
            tn.add(2*n2, sn);
            t2n.add(2*n2, s2n);
            t05n.add(2*n2, s05n);
            System.out.println(N);
        }

        myWriter.write("]");

        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(tn);
        dataset.addSeries(t2n);
        dataset.addSeries(t05n);
        return dataset;
    }

    public XYDataset createDatasetForConstLength() throws IOException {
        final XYSeries tn = new XYSeries("2^20");
        double c = 0.4;
        double d = 0.001;
        int n = 1 << 15;
        int start = 1 << 14;
        myWriter.write("[{" + n + "}\n");
        for (int j = 1; j <=1000; j++) {
            double sn = 0.;
            for (int i = start; i <= n; i++) {
                sn += 1. / calcP(i, n, BigDecimal.valueOf(1. / (c * n))).doubleValue();
            }
            sn /= 2.;
            sn /= n;
            sn /= n;
            tn.add((1. / c), sn);
            myWriter.write(",{\"prob\":" + c + ",\"time\":" + sn + "}\n");
            System.out.println(c);
            c += d;
        }
        myWriter.write("]");

        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(tn);
        return dataset;
    }

    public void savePlot() throws IOException {
        int width = 1000;   /* Width of the image */
        int height = 800;  /* Height of the image */
        File XYChart = new File( "expectedTimeForOneMaxHeLinearQuadratic.jpeg" );
        ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
        myWriter.close();
    }

    public static void main(String[] args) throws IOException {
        ExpectedOneMaxHe chart = new ExpectedOneMaxHe("Ожидаемое время работы для OneMax Hetero ");
        chart.savePlot();
    }
}
