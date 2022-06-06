package visualization;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ExpectedOneMaxHomoJson {
    private static FileWriter myWriter;

    public ExpectedOneMaxHomoJson(String title) throws IOException {
        myWriter = new FileWriter(title);
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
        for (int t = 1; t <= Math.min(i, 2*n - i - 1); t++) {
            int j = t - 1;
            cur = ((((cur.multiply(mu)).multiply(mu)).multiply(new BigDecimal(2 * n - i -j - 1))).multiply(new BigDecimal(i - j))).divide((((BigDecimal.ONE.subtract(mu)).pow(2)).multiply(new BigDecimal(j + 2))).multiply(new BigDecimal(j + 1)), mc);
            s = s.add(cur);
        }
        if (s.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("i = " + i + " n = " + n + " mu = " + mu);
        }
        return s;
    }

    public void createDataset( ) throws IOException {
        myWriter.write("[{}\n");
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        for (int N = 5; N <= 11; ++N) {
            BigDecimal sn = BigDecimal.ZERO, s2n = BigDecimal.ZERO, s05n = BigDecimal.ZERO, s025n = BigDecimal.ZERO;
            final int n1 = (int) Math.ceil(Math.pow(2, N - 0.7)), n2 = (int) Math.ceil(Math.pow(2, N - 0.3)), n = 1 << N;
            BigDecimal mu1 = BigDecimal.ONE.divide(BigDecimal.valueOf(n1), mc),
                    mu2 = BigDecimal.ONE.divide(BigDecimal.valueOf(2L * n1), mc),
                    mu3 = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(n1), mc),
                    mu4 = BigDecimal.ONE.divide(BigDecimal.valueOf(4L * n1), mc);
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
                s025n = s025n.add(BigDecimal.TEN.divide(calcP(i, n1, mu4), mc));
            }
            sn = sn.divide(BigDecimal.valueOf(n1 * Math.log(n1)), mc);
            s2n = s2n.divide(BigDecimal.valueOf(n1 * Math.log(n1)), mc);
            s05n = s05n.divide(BigDecimal.valueOf(n1 * Math.log(n1)), mc);
            s025n = s025n.divide(BigDecimal.valueOf(n1 * Math.log(n1)), mc);
            myWriter.write(",{\"fitness\":" + 2*n1 + ",\"runtime1\":" + sn + ",\"runtime2\":" + s2n + ",\"runtime05\":" + s05n + ",\"runtime4\":" + s025n + "}\n");

            sn = BigDecimal.ZERO;
            s2n = BigDecimal.ZERO;
            s05n = BigDecimal.ZERO;
            s025n = BigDecimal.ZERO;
            mu1 = BigDecimal.ONE.divide(BigDecimal.valueOf(n2), mc);
            mu2 = BigDecimal.ONE.divide(BigDecimal.valueOf(2L * n2), mc);
            mu3 = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(n2), mc);
            mu4 = BigDecimal.ONE.divide(BigDecimal.valueOf(4L * n2), mc);
            for (int i = n2; i < 2*n2; i++) {
                sn = sn.add(BigDecimal.TEN.divide(calcP(i, n2, mu1), mc));
                s2n = s2n.add(BigDecimal.TEN.divide(calcP(i, n2, mu2), mc));
                s05n = s05n.add(BigDecimal.TEN.divide(calcP(i, n2, mu3), mc));
                s025n = s025n.add(BigDecimal.TEN.divide(calcP(i, n2, mu4), mc));
            }
            sn = sn.divide(BigDecimal.valueOf(n2 * Math.log(n2)), mc);
            s2n = s2n.divide(BigDecimal.valueOf(n2 * Math.log(n2)), mc);
            s05n = s05n.divide(BigDecimal.valueOf(n2 * Math.log(n2)), mc);
            s025n = s025n.divide(BigDecimal.valueOf(n2 * Math.log(n2)), mc);
            myWriter.write(",{\"fitness\":" + 2*n2 + ",\"runtime1\":" + sn + ",\"runtime2\":" + s2n + ",\"runtime05\":" + s05n + ",\"runtime4\":" + s025n + "}\n");

            sn = BigDecimal.ZERO;
            s2n = BigDecimal.ZERO;
            s05n = BigDecimal.ZERO;
            s025n = BigDecimal.ZERO;
            mu1 = BigDecimal.ONE.divide(BigDecimal.valueOf(n), mc);
            mu2 = BigDecimal.ONE.divide(BigDecimal.valueOf(2L * n), mc);
            mu3 = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(n), mc);
            mu4 = BigDecimal.ONE.divide(BigDecimal.valueOf(4L * n), mc);
            for (int i = n; i < 2*n; i++) {
                sn = sn.add(BigDecimal.TEN.divide(calcP(i, n, mu1), mc));
                s2n = s2n.add(BigDecimal.TEN.divide(calcP(i, n, mu2), mc));
                s05n = s05n.add(BigDecimal.TEN.divide(calcP(i, n, mu3), mc));
                s025n = s025n.add(BigDecimal.TEN.divide(calcP(i, n, mu4), mc));
            }
            sn = sn.divide(BigDecimal.valueOf(n * Math.log(n)), mc);
            s2n = s2n.divide(BigDecimal.valueOf(n * Math.log(n)), mc);
            s05n = s05n.divide(BigDecimal.valueOf(n * Math.log(n)), mc);
            s025n = s025n.divide(BigDecimal.valueOf(n * Math.log(n)), mc);
            myWriter.write(",{\"fitness\":" + 2*n + ",\"runtime1\":" + sn + ",\"runtime2\":" + s2n + ",\"runtime05\":" + s05n + ",\"runtime4\":" + s025n + "}\n");
            System.out.println(N);
        }

        myWriter.write("]");
        myWriter.close();
    }

    public void createDatasetForConstLength() throws IOException {
        BigDecimal c = new BigDecimal("0.2");
        BigDecimal d = new BigDecimal("0.001");
        int n = 1 << 9;
        int start = 0;
        myWriter.write("[{}\n");
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        for (int j = 1; j <=600; j++) {
            BigDecimal sn = BigDecimal.ZERO;
            for (int i = start; i < n; i++) {
                sn = sn.add(BigDecimal.TEN.divide(calcP(i, start, c.divide(new BigDecimal(start), mc)), mc));
            }
            sn = sn.divide(BigDecimal.valueOf(start * Math.log(start)), mc);
            myWriter.write(",{\"prob\":" + c + ",\"time\":" + sn + "}\n");
            System.out.println(c);
            c = c.add(d);
        }
        myWriter.write("]");
        myWriter.close();
    }

    public static void main(String[] args) throws IOException {
        ExpectedOneMaxHomoJson chart = new ExpectedOneMaxHomoJson("Expected time for OneMaxHomo Quadratic");
        chart.createDataset();
    }
}
