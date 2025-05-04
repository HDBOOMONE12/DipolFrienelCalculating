package Artem_Pupyshev;

import lombok.Getter;

@Getter // Эта аннотация позволяет автоматически генерировать все геттеры

public class OpticalMedia {
    // Показатели преломления слоев
    private final double N4 = 2; // Среда вне
    private final double N3 = 3; // Верхний слой
    private final double N2 = 4; // Средний слой
    private final double N1 = 5; // Нижний слой
}
