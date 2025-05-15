package Artem_Pupyshev;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);
        System.out.println("Введите начальную интенсивность диполя:");
        double I0 = scanner.nextDouble();

        System.out.println("Введите угол наклона диполя в градусах:");
        double TiltAngle = scanner.nextDouble();

        System.out.println("Введите количество разбиений");
        int N = scanner.nextInt();



        System.out.println("Сколько диполей создать?");
        int nD = scanner.nextInt();



        Calculations calculations = new Calculations(N, I0, TiltAngle);

        System.out.println(calculations.calculateSumIntensityFromOneDipole());

        System.out.println("Суммарная интенсивность: "+calculations.calculateTotalIntensityFromAllDipoles(nD));

        System.out.println("Строим график? 1 если да, 0 если нет");
        int answer = scanner.nextInt();


        if (answer == 1) {
            System.out.println("Введите 2 граничных показателя преломления, в интервале которых мы будем менять n и шаг");
            System.out.println("От:");
            double n1 = scanner.nextDouble();
            System.out.println("До:");
            double n2 = scanner.nextDouble();
            System.out.println("Шаг");
            double dn = scanner.nextDouble();
            ScheduleBuilder.displayChart(N,I0,TiltAngle,nD,n1,n2,dn);
        }






    }
}