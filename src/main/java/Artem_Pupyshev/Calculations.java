package Artem_Pupyshev;

public class Calculations {

    private final int N; // Кол-во разбиений наших углов

    private final double dAngle; // Шаг по увеличения угла

    DipolChar dipolChar; // Наш диполь

    public Calculations(int N, double I0, double TILT_ANGLE) {
        this.N = N;
        dipolChar = new DipolChar(I0, TILT_ANGLE);
        dAngle = 2 * Math.PI / N; // Рассчет шага по увеличению угла
    }


    // Рассчет начальной интенсивности вышедшего луча
    private double calculateInitialIntensity(double angleFromVertical) {
        return (dipolChar.getI0() / Math.PI) * Math.pow(Math.sin(angleFromVertical - dipolChar.getTILT_ANGLE()), 2);
    }

    //Рассчет угла по закону Снелла
    private double calculateSnellAngle(double angleFromVertical, double nFrom, double nTo) {
        return Math.asin(nFrom / nTo * Math.sin(angleFromVertical));
    }

    //Рассчет Энергетического коэффициента отражения(Rp)
    private double calculateEnergyReflectionFactor(double angleFromVertical, double nFrom, double nTo) {
        double snellAngle = calculateSnellAngle(angleFromVertical, nFrom, nTo);
        double rp = (nFrom * Math.cos(snellAngle) - nTo * Math.cos(angleFromVertical)) /
                nFrom * Math.cos(snellAngle) + nTo * Math.cos(angleFromVertical);
        return Math.pow(rp, 2);
    }

    //Рассчет Энергетического коэффициента пропускания(Tp)
    private double calculateEnergyTransmittanceFactor(double Rp){
        return 1-Rp;
    }





}
