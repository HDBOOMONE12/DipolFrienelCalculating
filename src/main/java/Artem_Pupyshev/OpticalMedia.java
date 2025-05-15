package Artem_Pupyshev;

import lombok.Getter;
import lombok.Setter;

@Getter // Эта аннотация позволяет автоматически генерировать все геттеры

public class OpticalMedia {
    // Показатели преломления слоев
    public static final double N4 = 1; // Среда вне
    public static final double N3 = 2; // Верхний слой
    public static double N2 = 3; // Средний слой
    public static final double N1 = 2; // Нижний слой
}
