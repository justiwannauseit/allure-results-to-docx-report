import lombok.SneakyThrows;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.style.Styler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class PieChartGenerator {

    public static PieChart getChart(long countPassedTests,
                                    long countFailedTests,
                                    long countBrokenTests,
                                    long countSkippedTests,
                                    long countUnknownTests) {

        // Create Chart
        PieChart chart = new PieChartBuilder().width(800).height(800).title("Результаты").build();

        // Customize Chart
        Color[] sliceColors = new Color[]{
                new Color(34, 197, 94),
                new Color(239, 68, 68),
                new Color(250, 204, 21),
                new Color(148, 163, 184),
                new Color(168, 85, 247)};

        var emptyColor = new Color(255, 255, 255, 0);

        chart.getStyler().setSeriesColors(sliceColors);

        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setChartTitleFont(new Font("Century Gothic", 1, 20));
        chart.getStyler().setLabelsFont((new Font("Century Gothic", 1, 20)));
        chart.getStyler().setLabelsFontColor(new Color(232, 229, 229));

        chart.getStyler().setChartBackgroundColor(Color.white);

        // Customize Chart
        chart.getStyler().setLegendVisible(true); // Показать легенду

        chart.getStyler().setLabelsDistance(0.82);
        chart.getStyler().setPlotContentSize(.80);
        chart.getStyler().setDefaultSeriesRenderStyle(PieSeries.PieSeriesRenderStyle.Donut);
        chart.getStyler().setPlotBorderColor(emptyColor);

        chart.getStyler().setLegendPadding(15);
        chart.getStyler().setLegendFont(new Font("Century Gothic", 1, 15));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
        chart.getStyler().setLegendBorderColor(emptyColor);

        chart.getStyler().setSliceBorderWidth(3);

        // Series
        chart.addSeries("Успешные", countPassedTests);
        chart.addSeries("Не успешные", countFailedTests);
        chart.addSeries("Ошибка теста", countBrokenTests);
        chart.addSeries("Пропущенные", countSkippedTests);
        chart.addSeries("Неизвестные", countUnknownTests);

        return chart;
    }


    @SneakyThrows
    public static void save(long countPassedTests,
                            long countFailedTests,
                            long countBrokenTests,
                            long countSkippedTests,
                            long countUnknownTests) {
        var pieChart = getChart(countPassedTests, countFailedTests, countBrokenTests, countSkippedTests, countUnknownTests);

        BufferedImage bImg = new BufferedImage(pieChart.getWidth(), pieChart.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bImg.createGraphics();
        graphics2D.setColor(Color.BLUE);
        pieChart.paint(graphics2D, pieChart.getWidth(), pieChart.getHeight());
        ImageIO.write(bImg, "png", new File("./output_image.png"));
    }

}
