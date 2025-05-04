package Artem_Pupyshev;

import lombok.Getter;

@Getter // Эта аннотация позволяет автоматически генерировать все геттеры

public class OpticalMedia {
    // Показатели преломления слоев
    public static final double N4 = 2; // Среда вне
    public static final double N3 = 3; // Верхний слой
    public static final double N2 = 4; // Средний слой
    public static final double N1 = 5; // Нижний слой
}
