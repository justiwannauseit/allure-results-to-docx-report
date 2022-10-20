package style;

import lombok.Getter;

import java.awt.*;

@Getter
public enum Colors {

    PASSED(new Color(34, 197, 94), "Успешные"),
    FAILED(new Color(239, 68, 68), "Не успешные"),
    BROKEN(new Color(250, 204, 21), "Ошибка теста"),
    SKIPPED(new Color(148, 163, 184), "Пропущенные"),
    UNKNOWN(new Color(168, 85, 247), "Неизвестные"),

    WHITE(new Color(232, 229, 229), "Белый"),

    EMPTY(new Color(255, 255, 255, 0), "Пустой");

    private final Color color;
    private final String label;

    Colors(Color color, String label) {
        this.color = color;
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
