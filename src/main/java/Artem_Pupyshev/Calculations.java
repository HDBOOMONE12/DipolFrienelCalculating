package Artem_Pupyshev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Calculations {

    private final int N; // Кол-во разбиений наших углов

    private final double dAngle; // Шаг по увеличения угла

    DipolChar dipolChar; // Наш диполь

    private final double IOl; // Интенсивность начального луча
    private final double FROM_PRIMARY = 0.0001;

    public Calculations(int N, double I0, double TILT_ANGLE) {
        this.N = N;
        dipolChar = new DipolChar(I0, TILT_ANGLE);
        dAngle = 2 * Math.PI / N; // Расчет шага по увеличению угла
        IOl = calculateInitialIntensity(-Math.PI / 2);
    }


    // Расчет начальной интенсивности вышедшего луча
    private double calculateInitialIntensity(double angleFromVertical) {
        return (dipolChar.getI0() / Math.PI) * Math.pow(Math.sin(angleFromVertical - dipolChar.getTILT_ANGLE()), 2);
    }

    //Расчет угла по закону Снелла
    private double calculateSnellAngle(double angleFromVertical, double nFrom, double nTo) {
        return Math.asin(nFrom / nTo * Math.sin(angleFromVertical));
    }

    //Рассчет Энергетического коэффициента отражения(Rp)
    private double calculateEnergyReflectionFactor(double angleFromVertical, double nFrom, double nTo) {
        double snellAngle = calculateSnellAngle(angleFromVertical, nFrom, nTo);
        double A = nFrom * Math.cos(snellAngle);
        double B = nTo * Math.cos(angleFromVertical);
        double rp = (A - B) / (A + B);
        return Math.pow(rp, 2);
    }

    //Расчет Энергетического коэффициента пропускания(Tp)
    private double calculateEnergyTransmittanceFactor(double Rp) {
        return 1 - Rp;
    }


    //Считаем направление диполя по вертикали
    private int calculateVerticalDirection(double angleFromVertical) {
        return angleFromVertical < Math.PI / 2 ? 1 : -2;
    }


    //Расчеты если наш луч идет вверх
    private List<Ray> calculateUpwardRay(Ray ray) {

        //Контейнеры для производных лучей
        List<Ray> derivedRays = new ArrayList<>();


        while (ray.getCurrentLayerNumber() != 4) {
            //Рассчитываем Энергетические коэффициенты
            double Rp = calculateEnergyReflectionFactor(ray.getDirection(),
                    ray.getRefractiveIndex(ray.getCurrentLayerNumber()),
                    ray.getRefractiveIndex(ray.getCurrentLayerNumber() + 1));
            double Tp = calculateEnergyTransmittanceFactor(Rp);

            //Делаем проверку на угол полного внутреннего отражения
            if (!Double.isFinite(Tp) || Tp <= 0 || ray.getI() < IOl * FROM_PRIMARY) {
                // либо полное внутреннее отражение, либо слишком низкая энергия
                return null;
            }


            // Добавляем отражённый дочерний луч (только если не слишком мал)
            double Iref = ray.getI() * Rp;
            if (Iref >= IOl * FROM_PRIMARY) {
                derivedRays.add(new Ray(ray.getCurrentLayerNumber(), -2, Iref, ray.getDirection()));
            }


            //Меняем характеристики нашего начального луча
            ray.setI(ray.getI() * Tp);
            ray.setDirection
                    (calculateSnellAngle(
                                    ray.getDirection(),
                                    ray.getRefractiveIndex(ray.getCurrentLayerNumber()),
                                    ray.getRefractiveIndex(ray.getCurrentLayerNumber() + 1)
                            )
                    );
            ray.setCurrentLayerNumber(ray.getCurrentLayerNumber() + 1);
        }
        return derivedRays;
    }

    private List<Ray> calculateDownwardRay(Ray ray) {
        // Пересчитываем углы (т.к. угол от вертикали не равен углу падения)
        ray.setDirection(Math.PI - ray.getDirection());

        // Контейнер для производных лучей
        List<Ray> derivedRays = new ArrayList<>();

        while (ray.getCurrentLayerNumber() != 0) {

            if (ray.getCurrentLayerNumber() == 1) {

                double IrefMirror = ray.getI(); // Todo сделать коэффициент
                if (IrefMirror >= IOl * FROM_PRIMARY) {
                    // зеркальное отражение — создаём новый луч вверх
                    derivedRays.add(new Ray(
                            1,    // остаёмся в слое 1
                            1,    // направление вверх
                            IrefMirror,
                            ray.getDirection()
                    ));
                }
                return derivedRays;  // сразу выходим из метода
            }

            // Рассчитываем Энергетические коэффициенты
            double Rp = calculateEnergyReflectionFactor(
                    ray.getDirection(),
                    ray.getRefractiveIndex(ray.getCurrentLayerNumber()),
                    ray.getRefractiveIndex(ray.getCurrentLayerNumber() - 1)
            );
            double Tp = calculateEnergyTransmittanceFactor(Rp);

            // Проверка на полный внутренний и порог энергии
            if (!Double.isFinite(Tp) || Tp <= 0 || ray.getI() < IOl * FROM_PRIMARY) {
                // либо полное внутреннее отражение, либо слишком низкая энергия
                return null;
            }

            // Добавляем отражённый дочерний луч (только если не слишком мал)
            double Iref = ray.getI() * Rp;
            if (Iref >= IOl * FROM_PRIMARY) {
                derivedRays.add(new Ray(ray.getCurrentLayerNumber(), 1, Iref, ray.getDirection()));
            }

            // Меняем характеристики «основного» луча и идём дальше вниз
            ray.setI(ray.getI() * Tp);
            ray.setDirection(calculateSnellAngle(
                    ray.getDirection(),
                    ray.getRefractiveIndex(ray.getCurrentLayerNumber()),
                    ray.getRefractiveIndex(ray.getCurrentLayerNumber() - 1)
            ));
            ray.setCurrentLayerNumber(ray.getCurrentLayerNumber() - 1);
        }
        return derivedRays;
    }

    private double calculateSumIntensityFromOneRay(Ray ray) {
        if (ray == null) return 0;

        double Isum = 0;
        List<Ray> daughters;

        // Сначала строим дочерние лучи
        if (ray.getVerticalDirection() == 1) {
            daughters = calculateUpwardRay(ray);
        } else {
            daughters = calculateDownwardRay(ray);
        }

        // **Добавляем в сумму только выходящие вверх лучи**
        if (ray.getVerticalDirection() == 1) {
            Isum += ray.getI();
        }

        // Рекурсивно обходим всех отражённых дочерних лучей
        if (daughters != null) {
            for (Ray d : daughters) {
                Isum += calculateSumIntensityFromOneRay(d);
            }
        }

        return Isum;
    }


    public double calculateSumIntensityFromOneDipole() {
        double i0 = 0;
        double angle = -Math.PI / 2;
        for (int i = 0; i < N; i++) {
            Ray ray = new Ray(2, calculateVerticalDirection(angle), calculateInitialIntensity(angle), angle);
            i0 += calculateSumIntensityFromOneRay(ray) * dAngle;
            angle += dAngle;
        }
        return i0;
    }

    public double calculateTotalIntensityFromAllDipoles(double nD) {
        double I = 0;
        for (int i = 0; i < nD; i++) {
            double TITLT_ANGLE = Math.random() * 360 - 90;
            Calculations calculations1 = new Calculations(N, dipolChar.getI0(), TITLT_ANGLE);
            I += calculations1.calculateSumIntensityFromOneDipole();
        }
        return I;
    }

    public List<Double> getYData(int nD, double n1, double n2, double dn) {
        List<Double> Isums = new ArrayList<>();
        double OptMed = OpticalMedia.N2;

        List<Double> tiltAngles = new ArrayList<>(nD);
        Random rnd = new Random();
        for (int i = 0; i < nD; i++) {
            tiltAngles.add(rnd.nextDouble() * 360 - 90);
        }

        int steps = (int) Math.round((n2 - n1) / dn);
        for (int i = 0; i <= steps; i++) {
            double n = n1 + i * dn;
            OpticalMedia.N2 = n;

            double I = 0;
            for (Double tiltAngle : tiltAngles) {
                Calculations calc = new Calculations(N, dipolChar.getI0(), tiltAngle);
                I += calc.calculateSumIntensityFromOneDipole();
            }
            Isums.add(I);
        }

        OpticalMedia.N2 = OptMed;
        return Isums;
    }

}

