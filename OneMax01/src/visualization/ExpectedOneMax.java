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

public class ExpectedOneMax extends ApplicationFrame {
    private JFreeChart xylineChart;
    private static FileWriter myWriter;

    public ExpectedOneMax(String title) throws IOException {
        super(title);

        myWriter = new FileWriter("expected time for OneMax Linear Quadratic.out");

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

    private double calcP(int i, int n, double mu) {
        double s = 0;
        for (int j = 0; j <= i; j++) {
            s += Math.pow(mu, 2 * j + 1) * Math.pow(1. - mu, n - 2 * j - 1) * binomialFloat(n - i, j + 1) * binomialFloat(i, j);
        }
        return s;
    }

    public XYDataset createDataset( ) throws IOException {
        final XYSeries tn = new XYSeries("1/N");
        final XYSeries t2n = new XYSeries("1/2N");
        final XYSeries t05n = new XYSeries("2/N");

        myWriter.write("[{}\n");
        for (int N = 2; N <= 14; ++N) {
            double sn = 0., s2n = 0., s05n = 0.;
            final int n1 = (int) Math.ceil(Math.pow(2, N - 0.7)), n2 = (int) Math.ceil(Math.pow(2, N - 0.3)), n = 1 << N;
            for (int i = 1; i < n1; i++) {
                sn += 1. / calcP(i, n1, 1. / n1);
                s2n += 1. / calcP(i, n1, 1. / (2 * n1));
                s05n += 1. / calcP(i, n1, 2. / n1);
            }
            sn /= 2.;
            s2n /= 2.;
            s05n /= 2.;
            sn /= n1;
            s2n /= n1;
            s05n /= n1;
            sn /= n1;
            s2n /= n1;
            s05n /= n1;
            myWriter.write(",{\"fitness\":" + n1 + ",\"runtime\":" + sn + "}\n");
            myWriter.write(",{\"fitness\":" + n1 + ",\"runtime\":" + s2n + "}\n");
            myWriter.write(",{\"fitness\":" + n1 + ",\"runtime\":" + s05n + "}\n");
            tn.add(n1, sn);
            t2n.add(n1, s2n);
            t05n.add(n1, s05n);

            sn = 0.;
            s2n = 0.;
            s05n = 0.;
            for (int i = 1; i <= n; i++) {
                sn += 1. / calcP(i, n, 1. / n);
                s2n += 1. / calcP(i, n, 1. / (2 * n));
                s05n += 1. / calcP(i, n, 2. / n);
            }
            sn /= 2.;
            s2n /= 2.;
            s05n /= 2.;
            sn /= n;
            s2n /= n;
            s05n /= n;
            sn /= n;
            s2n /= n;
            s05n /= n;
            myWriter.write(",{\"fitness\":" + n + ",\"runtime\":" + sn + "}\n");
            myWriter.write(",{\"fitness\":" + n + ",\"runtime\":" + s2n + "}\n");
            myWriter.write(",{\"fitness\":" + n + ",\"runtime\":" + s05n + "}\n");
            tn.add(n, sn);
            t2n.add(n, s2n);
            t05n.add(n, s05n);

            sn = 0.;
            s2n = 0.;
            s05n = 0.;
            for (int i = 1; i <= n2; i++) {
                sn += 1. / calcP(i, n2, 1. / n2);
                s2n += 1. / calcP(i, n2, 1. / (2 * n2));
                s05n += 1. / calcP(i, n2, 2. / n2);
            }
            sn /= 2.;
            s2n /= 2.;
            s05n /= 2.;
            sn /= n2;
            s2n /= n2;
            s05n /= n2;
            sn /= n2;
            s2n /= n2;
            s05n /= n2;
            myWriter.write(",{\"fitness\":" + n2 + ",\"runtime\":" + sn + "}\n");
            myWriter.write(",{\"fitness\":" + n2 + ",\"runtime\":" + s2n + "}\n");
            myWriter.write(",{\"fitness\":" + n2 + ",\"runtime\":" + s05n + "}\n");
            tn.add(n2, sn);
            t2n.add(n2, s2n);
            t05n.add(n2, s05n);
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
        int n = 1 << 20;
        myWriter.write("[{" + n + "}\n");
        for (int j = 1; j <=1000; j++) {
            double sn = 0.;
            for (int i = 1; i <= n; i++) {
                sn += 1. / calcP(i, n, 1. / (c * n));
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
        File XYChart = new File( "expectedTimeForOneMaxLinearQuadratic.jpeg" );
        ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
        myWriter.close();
    }

    public static void main(String[] args) throws IOException {
        ExpectedOneMax chart = new ExpectedOneMax("Ожидаемое время работы для OneMax");
        chart.savePlot();
    }
}
