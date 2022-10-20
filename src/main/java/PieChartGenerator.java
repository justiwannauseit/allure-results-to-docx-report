import lombok.SneakyThrows;
import metric.Metric;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.style.Styler;
import style.Colors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PieChartGenerator {

    public static PieChart getChart(Metric metric) {

        // Create Chart
        PieChart chart = new PieChartBuilder()
                .width(800)
                .height(800)
                .title(String.format("Из %d тестов успешных %d", metric.getCountAllTests(), metric.getCountPassedTests()))
                .build();

        // Customize Chart
        chart.getStyler().setSeriesColors(Colors.getSliceColors());

        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setChartTitleFont(new Font("Times New Roman", 0, 20));

        chart.getStyler().setLabelsFont((new Font("Century Gothic", 1, 20)));
        chart.getStyler().setLabelsFontColor(Colors.WHITE.getColor());

        chart.getStyler().setChartBackgroundColor(Color.white);

        // Customize Chart
        chart.getStyler().setLegendVisible(true); // Показать легенду

        chart.getStyler().setLabelsDistance(0.82);
        chart.getStyler().setPlotContentSize(.80);
        chart.getStyler().setDefaultSeriesRenderStyle(PieSeries.PieSeriesRenderStyle.Donut);
        chart.getStyler().setPlotBorderColor(Colors.EMPTY.getColor());

        chart.getStyler().setLegendPadding(15);
        chart.getStyler().setLegendFont(new Font("Century Gothic", 1, 22));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
        chart.getStyler().setLegendBorderColor(Colors.EMPTY.getColor());

        chart.getStyler().setSliceBorderWidth(3);

        // Series
        chart.addSeries(Colors.PASSED.getLabel(), metric.getCountPassedTests());
        chart.addSeries(Colors.FAILED.getLabel(), metric.getCountFailedTests());
        chart.addSeries(Colors.BROKEN.getLabel(), metric.getCountBrokenTests());
        chart.addSeries(Colors.SKIPPED.getLabel(), metric.getCountSkippedTests());
        chart.addSeries(Colors.UNKNOWN.getLabel(), metric.getCountUnknownTests());

        return chart;
    }


    @SneakyThrows
    public static void save(Metric metric) {
        var pieChart = getChart(metric);
        BufferedImage bImg = new BufferedImage(pieChart.getWidth(), pieChart.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bImg.createGraphics();
        graphics2D.setColor(Color.BLUE);
        pieChart.paint(graphics2D, pieChart.getWidth(), pieChart.getHeight());
        ImageIO.write(bImg, "png", new File("./output_image.png"));
    }

}
