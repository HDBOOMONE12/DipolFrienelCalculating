package Artem_Pupyshev;

import java.util.ArrayList;
import java.util.List;

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
    private double calculateEnergyTransmittanceFactor(double Rp) {
        return 1 - Rp;
    }

    //Считаем направление диполя по вертикали
    private int calculateVerticalDirection(double angleFromVertical) {
        return angleFromVertical < Math.PI ? 1 : -2;

    }

    //Изменить направление движения по вертикали
    private int changeVerticalDirection(int verticalDirection) {
        return verticalDirection == 1 ? 1 : -2;
    }


    public void calculateSumIntensity(Ray ray) {

        //Контейнеры для производных лучей
        List<Ray> firstUp = new ArrayList<>(5);  // 4 номер (слой +1)
        List<Ray> firstDown = new ArrayList<>(5); // 1 номер (слой -2)
        List<Ray> secondUp = new ArrayList<>(5); //3 номер
        List<Ray> secondDown = new ArrayList<>(5); //0 номер
        List<Ray> thirdUp = new ArrayList<>(5); // 2 номер
        List<Ray> thirdDown = new ArrayList<>(5); // -1 номер

        if (ray.getVerticalDirection() == 1) {
            while (ray.getCurrentLayerNumber() != 4) {
                double Rp = calculateEnergyReflectionFactor
                        (ray.getDirection(), ray.getCurrentLayerNumber(), ray.getCurrentLayerNumber() + 1);
                double Tp = calculateEnergyTransmittanceFactor(Rp);
                int contNumber = ray.getCurrentLayerNumber() - 2; // Если луч идет вверх, то производный луч идет вниз,
                // а значит надо вычитать 2

                switch (contNumber) {  //Луч идет вверх, а значит все производные будут идти вниз
                    case 1:
                        firstDown.add(new Ray(ray.getCurrentLayerNumber(), -2, ray.getI() * Rp, ray.getDirection()));
                        break;

                    case 0:
                        secondDown.add(new Ray(ray.getCurrentLayerNumber(), -2, ray.getI() * Rp, ray.getDirection()));
                        break;

                    case -1:
                        thirdDown.add(new Ray(ray.getCurrentLayerNumber(), -2, ray.getI() * Rp, ray.getDirection()));
                        break;

                }
                ray.setI(ray.getI() * Tp);
                ray.setDirection(calculateSnellAngle
                        (ray.getDirection(), ray.getCurrentLayerNumber(), ray.getCurrentLayerNumber() + 1));
                ray.setCurrentLayerNumber(ray.getCurrentLayerNumber() + 1);
            }

        } else if (ray.getVerticalDirection() == -2) {
            while (ray.getCurrentLayerNumber() != 4) {

            }
        }


    }


}
