package Artem_Pupyshev;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.List;

public class ScheduleBuilder {

    public static XYSeriesCollection createDataset(int N, double I0, double tiltAngle, int nd, double n1, double n2, double dn) {
        Calculations calculations = new Calculations(N, I0, tiltAngle);
        List<Double> ys = calculations.getYData(nd, n1, n2, dn);

        XYSeries series = new XYSeries("Зависимость Y от X");
        int steps = (int) Math.round((n2 - n1) / dn);
        for (int i = 0; i <= steps && i < ys.size(); i++) {
            double x = n1 + i * dn;
            series.add(x, ys.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        return dataset;
    }

    public static JFreeChart createChart(int N, double I0, double tiltAngle, int nd, double n1, double n2, double dn) {
        XYSeriesCollection dataset = createDataset(N, I0, tiltAngle, nd, n1, n2, dn);
        JFreeChart chart = ChartFactory.createXYLineChart("Зависимость мощности множества Диполей от показателя преломления", // заголовок
                "Показатель преломления",                                       // ось X
                "Мощность",                                                     // ось Y
                dataset,                                                         // данные
                PlotOrientation.VERTICAL, true,  // легенда
                true,  // тултипы
                false  // URL
        );

        // Настройки оси Y: не включать ноль и добавить отступы
        XYPlot plot = (XYPlot) chart.getPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setLowerMargin(0.10);
        rangeAxis.setUpperMargin(0.10);

        return chart;
    }

    public static void displayChart(int N, double I0, double tiltAngle, int nd, double n1, double n2, double dn) {
        JFreeChart chart = createChart(N, I0, tiltAngle, nd, n1, n2, dn);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("График");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(chartPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
