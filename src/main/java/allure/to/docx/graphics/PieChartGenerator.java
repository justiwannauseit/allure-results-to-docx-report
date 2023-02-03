package allure.to.docx.graphics;

import allure.to.docx.metric.Metric;
import allure.to.docx.style.Colors;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.style.Styler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static allure.to.docx.util.LocaleUtil.REPORT_CONFIGURATION;
import static allure.to.docx.util.LocaleUtil.localizeCode;

@UtilityClass
public class PieChartGenerator {

    public static PieChart getChart(Metric metric) {

        // Create Chart
        PieChart chart = new PieChartBuilder()
                .width(REPORT_CONFIGURATION.pieChartWidth())
                .height(REPORT_CONFIGURATION.pieChartHeight())
                .title(String.format(localizeCode("pie.chart.title"), metric.getCountAllTests(), metric.getCountPassedTests()))
                .build();

        // Customize Chart
        chart.getStyler().setSeriesColors(Colors.getSliceColors());

        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setChartTitleFont(new Font(REPORT_CONFIGURATION.fontFamily(), Font.PLAIN, REPORT_CONFIGURATION.pieChartFontSize()));

        chart.getStyler().setLabelsFont((new Font(REPORT_CONFIGURATION.pieChartFontFamily(), Font.BOLD, REPORT_CONFIGURATION.pieChartFontSize())));
        chart.getStyler().setLabelsFontColor(Colors.WHITE.getColor());

        chart.getStyler().setChartBackgroundColor(Color.white);

        // Customize Chart
        chart.getStyler().setLegendVisible(true); // Показать легенду

        chart.getStyler().setLabelsDistance(REPORT_CONFIGURATION.pieChartLabelsDistance());
        chart.getStyler().setPlotContentSize(REPORT_CONFIGURATION.pieChartPlotContentSize());
        chart.getStyler().setDefaultSeriesRenderStyle(PieSeries.PieSeriesRenderStyle.Donut);
        chart.getStyler().setPlotBorderColor(Colors.TRANSPARENT.getColor());

        chart.getStyler().setLegendPadding(REPORT_CONFIGURATION.pieChartLegendPadding());
        chart.getStyler().setLegendFont(new Font(REPORT_CONFIGURATION.pieChartFontFamily(), Font.BOLD, REPORT_CONFIGURATION.pieChartLegendFontSize()));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
        chart.getStyler().setLegendBorderColor(Colors.TRANSPARENT.getColor());

        chart.getStyler().setSliceBorderWidth(REPORT_CONFIGURATION.pieChartBorderWidth());

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
        ImageIO.write(bImg, "png", new File(REPORT_CONFIGURATION.outputPath()));
    }

}
