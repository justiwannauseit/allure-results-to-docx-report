package allure.to.docx.config;

import org.aeonbits.owner.Config;

@Config.Sources(value = "classpath:report.properties")
public interface ReportConfiguration extends Config {

    @Key("title.color")
    String titleColor();

    @Key("failed.color")
    String failedColor();

    @Key("font.family")
    String fontFamily();

    @Key("sub.title.font.size")
    Integer subTitleFontSize();

    @Key("text.position")
    Integer textPosition();

    @Key("steps.text.position")
    Integer stepsTextPosition();

    @Key("title.font.size")
    Long fontSize();

    @Key("metric.font.size")
    Long metricFontSize();

    @Key("steps.font.size")
    Long stepsFontSize();

    @Key("date.pattern")
    String datePattern();

    @Key("output.image.path")
    String outputPath();

    @Key("status.separator")
    String statusSeparator();

    @Key("pie.chart.width")
    Integer pieChartWidth();

    @Key("pie.chart.height")
    Integer pieChartHeight();

    @Key("pie.chart.font.family")
    String pieChartFontFamily();

    @Key("pie.chart.title.font.size")
    Integer pieChartFontSize();

    @Key("pie.chart.legend.font.size")
    Integer pieChartLegendFontSize();

    @Key("pie.chart.labels.distance")
    Double pieChartLabelsDistance();

    @Key("pie.chart.plot.content.size")
    Double pieChartPlotContentSize();

    @Key("pie.chart.legend.padding")
    Integer pieChartLegendPadding();

    @Key("pie.chart.slice.border.width")
    Integer pieChartBorderWidth();
}
