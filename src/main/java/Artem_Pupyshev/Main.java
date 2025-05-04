package Artem_Pupyshev;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите начальную интенсивность диполя:");
        double I0 = scanner.nextDouble();

        System.out.println("Введите угол наклона диполя в градусах:");
        double TiltAngle = scanner.nextDouble();

        System.out.println("Введите количество разбиений");
        int N = scanner.nextInt();

       Calculations calculations = new Calculations(N,I0,TiltAngle);










    }
}