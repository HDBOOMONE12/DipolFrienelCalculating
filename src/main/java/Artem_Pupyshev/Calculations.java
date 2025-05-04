package Artem_Pupyshev;

import java.util.ArrayList;
import java.util.List;

public class Calculations {

    private final int N; // Кол-во разбиений наших углов

    private final double dAngle; // Шаг по увеличения угла

    DipolChar dipolChar; // Наш диполь

    private final double IO = calculateInitialIntensity(0); //Todo
    private final double FROM_PRIMARY = 0.001;

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
        return angleFromVertical < Math.PI/2 ? 1 : -2;

    }

    //Изменить направление движения по вертикали
    private int changeVerticalDirection(int verticalDirection) {
        return verticalDirection == 1 ? 1 : -2;
    }

    //Рассчеты если наш луч идет вверх
    public List<Ray> calculateUpwardRay(Ray ray) {

        //Контейнеры для производных лучей
        List<Ray> derivedRays = new ArrayList<>();


        while (ray.getCurrentLayerNumber() != 4) {
            //Рассчитываем Энергетические коэффициенты
            double Rp = calculateEnergyReflectionFactor
                    (ray.getDirection(), ray.getRefractiveIndex(ray.getCurrentLayerNumber()), ray.getRefractiveIndex(ray.getCurrentLayerNumber() + 1));
            double Tp = calculateEnergyTransmittanceFactor(Rp);

            //Делаем проверку на угол полного внутреннего отражения
            if (Tp == 0 || ray.getI()<this.IO*FROM_PRIMARY){
                return null;
            }

            //Добавляем производные лучи
            derivedRays.add
                    (new Ray(ray.getCurrentLayerNumber(), -2, ray.getI() * Rp, ray.getDirection()));

            //Меняем характеристики нашего начального луча
            ray.setI(ray.getI() * Tp);
            ray.setDirection
                    (calculateSnellAngle(ray.getDirection(), ray.getRefractiveIndex(ray.getCurrentLayerNumber()), ray.getRefractiveIndex(ray.getCurrentLayerNumber() + 1)));
            ray.setCurrentLayerNumber(ray.getCurrentLayerNumber() + 1);


        }
        return derivedRays;

    }

    public List<Ray> calculateDownwardRay(Ray ray) {
        //Пересчитываем углы(т.к угол от вертикали не равен углу падения)
        ray.setDirection(Math.PI - ray.getDirection());

        //Контейнеры для производных лучей
        List<Ray> derivedRays = new ArrayList<>();

        while (ray.getCurrentLayerNumber() != 0) {

            if (ray.getCurrentLayerNumber() == 1) {
                derivedRays.add(ray);
                break;
            }

            //Рассчитываем Энергетические коэффициенты
            double Rp = calculateEnergyReflectionFactor
                    (ray.getDirection(), ray.getRefractiveIndex(ray.getCurrentLayerNumber()),
                            ray.getRefractiveIndex(ray.getCurrentLayerNumber() - 1));
            ;
            double Tp = calculateEnergyTransmittanceFactor(Rp);

            //Делаем проверку на угол полного внутреннего отражения и минимальную энергию луча
            if (Tp == 0 || ray.getI()<this.IO*FROM_PRIMARY){
                return null;
            }


            //Добавляем производные лучи
            derivedRays.add
                    (new Ray(ray.getCurrentLayerNumber(), 1, ray.getI() * Rp, ray.getDirection()));

            //Меняем характеристики нашего начального луча
            ray.setI(ray.getI() * Tp);
            ray.setDirection
                    (calculateSnellAngle(ray.getDirection(), ray.getRefractiveIndex(ray.getCurrentLayerNumber()),
                            ray.getRefractiveIndex(ray.getCurrentLayerNumber() - 1)));
            ray.setCurrentLayerNumber(ray.getCurrentLayerNumber() - 1);
        }
        return derivedRays;
    }

    public double calculateSumIntensity(){
            double Isum = 0;
            double angleFromVertical = -Math.PI/2;
            for (int i = 0; i < N; i++) {
                Ray ray = new Ray(2,calculateVerticalDirection(angleFromVertical),
                        calculateInitialIntensity(angleFromVertical), angleFromVertical);
                while(true){
                    if (ray.getVerticalDirection()==1){

                    }
                    if (ray.getVerticalDirection()==(-2)){

                    }
                }


                angleFromVertical += dAngle;
            }

    }


}
