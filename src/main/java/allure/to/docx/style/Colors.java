package allure.to.docx.style;

import lombok.Getter;

import java.awt.*;

@Getter
public enum Colors {

    PASSED(new Color(34, 197, 94), "000000", "Успешные"),
    FAILED(new Color(239, 68, 68), "FF0000", "Не успешные"),
    BROKEN(new Color(250, 204, 21), "d0d40b", "Ошибка теста"),
    SKIPPED(new Color(148, 163, 184), "6e6e6e", "Пропущенные"),
    UNKNOWN(new Color(168, 85, 247), "cccf38", "Неизвестные"),

    WHITE(new Color(232, 229, 229), "FFFFFF", "Белый"),

    TRANSPARENT(new Color(255, 255, 255, 0), "0000ffff", "Прозрачный");

    private final Color color;

    private final String hexCode;
    private final String label;

    Colors(Color color, String hexCode, String label) {
        this.color = color;
        this.hexCode = hexCode;
        this.label = label;
    }

    public static Color[] getSliceColors() {
        return new Color[]{PASSED.getColor(),
                FAILED.getColor(),
                BROKEN.getColor(),
                SKIPPED.getColor(),
                UNKNOWN.getColor()};
    }

}
