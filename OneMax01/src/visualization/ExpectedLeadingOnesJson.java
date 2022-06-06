package visualization;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ExpectedLeadingOnesJson {
    private static FileWriter myWriter;

    public ExpectedLeadingOnesJson(String title) throws IOException {
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

    public void createDataset( ) throws IOException {
        myWriter.write("[{}\n");
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        for (int N = 5; N <= 14; ++N) {
            
            final int n1 = (int) Math.ceil(Math.pow(2, N - 0.7)), n2 = (int) Math.ceil(Math.pow(2, N - 0.3)), n = 1 << N;
            BigDecimal mu1 = BigDecimal.ONE.divide(BigDecimal.valueOf(n1), mc),
                    mu2 = BigDecimal.ONE.divide(BigDecimal.valueOf(2L * n1), mc),
                    mu3 = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(n1), mc);
            BigDecimal sn = BigDecimal.ZERO, s05n = BigDecimal.ZERO, s2n = BigDecimal.ZERO;
            for (int i = 1; i < n1; i++) {
                sn = sn.add(BigDecimal.ONE.divide((BigDecimal.ONE.subtract(mu1)).pow(n1 - i), mc));
                s2n = s2n.add(BigDecimal.ONE.divide((BigDecimal.ONE.subtract(mu2)).pow(n1 - i), mc));
                s05n = s05n.add(BigDecimal.ONE.divide((BigDecimal.ONE.subtract(mu3)).pow(n1 - i), mc));
            }
            sn = sn.divide(((mu1.add(mu1)).subtract(mu1.pow(2))).multiply(new BigDecimal(4)), mc); 
            s2n = s2n.divide(((mu1.add(mu2)).subtract(mu2.pow(2))).multiply(new BigDecimal(4)), mc); 
            s05n = s05n.divide(((mu3.add(mu3)).subtract(mu3.pow(2))).multiply(new BigDecimal(4)), mc);
            sn = sn.divide(BigDecimal.valueOf(n1 * n1), mc);
            s2n = s2n.divide(BigDecimal.valueOf(n1 * n1), mc);
            s05n = s05n.divide(BigDecimal.valueOf(n1 * n1), mc);
            myWriter.write(",{\"fitness\":" + n1 + ",\"runtime1\":" + sn + ",\"runtime2\":" + s2n + ",\"runtime05\":" + s05n + "}\n");

            sn = BigDecimal.ZERO;
            s2n = BigDecimal.ZERO;
            s05n = BigDecimal.ZERO;
            mu1 = BigDecimal.ONE.divide(BigDecimal.valueOf(n2), mc);
            mu2 = BigDecimal.ONE.divide(BigDecimal.valueOf(2L * n2), mc);
            mu3 = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(n2), mc);
            for (int i = 1; i < n2; i++) {
                sn = sn.add(BigDecimal.ONE.divide((BigDecimal.ONE.subtract(mu1)).pow(n2 - i), mc));
                s2n = s2n.add(BigDecimal.ONE.divide((BigDecimal.ONE.subtract(mu2)).pow(n2 - i), mc));
                s05n = s05n.add(BigDecimal.ONE.divide((BigDecimal.ONE.subtract(mu3)).pow(n2 - i), mc));
            }
            sn = sn.divide(((mu1.add(mu1)).subtract(mu1.pow(2))).multiply(new BigDecimal(4)), mc); 
            s2n = s2n.divide(((mu1.add(mu2)).subtract(mu2.pow(2))).multiply(new BigDecimal(4)), mc); 
            s05n = s05n.divide(((mu3.add(mu3)).subtract(mu3.pow(2))).multiply(new BigDecimal(4)), mc);
            sn = sn.divide(BigDecimal.valueOf(n2 * n2), mc);
            s2n = s2n.divide(BigDecimal.valueOf(n2 * n2), mc);
            s05n = s05n.divide(BigDecimal.valueOf(n2 * n2), mc);
            myWriter.write(",{\"fitness\":" + n2 + ",\"runtime1\":" + sn + ",\"runtime2\":" + s2n + ",\"runtime05\":" + s05n + "}\n");

            sn = BigDecimal.ZERO;
            s2n = BigDecimal.ZERO;
            s05n = BigDecimal.ZERO;
            mu1 = BigDecimal.ONE.divide(BigDecimal.valueOf(n), mc);
            mu2 = BigDecimal.ONE.divide(BigDecimal.valueOf(2L * n), mc);
            mu3 = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(n), mc);
            for (int i = 1; i < n; i++) {
                sn = sn.add(BigDecimal.ONE.divide((BigDecimal.ONE.subtract(mu1)).pow(n - i), mc));
                s2n = s2n.add(BigDecimal.ONE.divide((BigDecimal.ONE.subtract(mu2)).pow(n - i), mc));
                s05n = s05n.add(BigDecimal.ONE.divide((BigDecimal.ONE.subtract(mu3)).pow(n - i), mc));
            }
            sn = sn.divide(((mu1.add(mu1)).subtract(mu1.pow(2))).multiply(new BigDecimal(4)), mc); 
            s2n = s2n.divide(((mu1.add(mu2)).subtract(mu2.pow(2))).multiply(new BigDecimal(4)), mc); 
            s05n = s05n.divide(((mu3.add(mu3)).subtract(mu3.pow(2))).multiply(new BigDecimal(4)), mc);
            sn = sn.divide(BigDecimal.valueOf(n * n), mc);
            s2n = s2n.divide(BigDecimal.valueOf(n * n), mc);
            s05n = s05n.divide(BigDecimal.valueOf(n * n), mc);
            myWriter.write(",{\"fitness\":" + n + ",\"runtime1\":" + sn + ",\"runtime2\":" + s2n + ",\"runtime05\":" + s05n + "}\n");
            System.out.println(N);
        }

        myWriter.write("]");
        myWriter.close();
    }

    public void createDatasetForConstLength() throws IOException {
        BigDecimal c = new BigDecimal("1.3");
        BigDecimal d = new BigDecimal("0.001");
        int n = 1 << 9;
        int start = 1;
        myWriter.write("[{" + n + "}\n");
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        for (int j = 1; j <=600; j++) {
            BigDecimal sn = BigDecimal.ZERO;
            BigDecimal mu = c.divide(new BigDecimal(n), mc);
            for (int i = start; i <= n; i++) {
                sn = sn.add(BigDecimal.ONE.divide((BigDecimal.ONE.subtract(mu)).pow(n - i), mc));
            }
            sn = sn.divide(((mu.add(mu)).subtract(mu.pow(2))).multiply(new BigDecimal(4)), mc);
            sn = sn.divide(BigDecimal.valueOf(n*n), mc);
            myWriter.write(",{\"prob\":" + c + ",\"time\":" + sn + "}\n");
            System.out.println(c);
            c = c.add(d);
        }
        myWriter.write("]");
        myWriter.close();
    }

    public static void main(String[] args) throws IOException {
        ExpectedLeadingOnesJson chart = new ExpectedLeadingOnesJson("Expected time for LeadingOnes Quadratic with const length");
        chart.createDatasetForConstLength();
    }
}
