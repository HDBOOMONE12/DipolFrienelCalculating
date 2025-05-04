package Artem_Pupyshev;

import lombok.Getter;

@Getter

public class DipolChar {

    // Характеристики Диполя
    private final double I0; // Начальная интенсивность
    private final double TILT_ANGLE; // Угол наклона в градусах

    public DipolChar(double I0, double TILT_ANGLE) {
        this.I0 = I0;
        this.TILT_ANGLE = TILT_ANGLE;
    }

    public double getTILT_ANGLE() {
        return TILT_ANGLE * Math.PI / 180; // Возвращаем уже в радианах
    }
}
