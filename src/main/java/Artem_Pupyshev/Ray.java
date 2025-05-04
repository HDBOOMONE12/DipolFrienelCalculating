package Artem_Pupyshev;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Ray {

    private int currentLayerNumber;
    // Слой в котором находится луч в данный момент.
    // Принимает значения от [1,4]. Цифра соответствует номеру слоя.


    private int verticalDirection; //Куда направлен луч(Вверх или Вниз).
    // 1 - Вверх, -2 - Вниз


    private double I;
    //Интенсивность луча в данный момент времени

    private double direction;
    // Угол под которым он идет в данный момент


}
