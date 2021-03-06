package ru.nmago.stepikapitest;

import ru.nmago.stepikapitest.api.StepikAPI;
import ru.nmago.stepikapitest.exception.CannotGetCoursesException;
import ru.nmago.stepikapitest.model.Course;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        int N;
        boolean showProgress;
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите N: ");
            N = scanner.nextInt();
            if(N < 1){
                throw new InputMismatchException();
            }
            System.out.println("Выводить прогресс? (y/n): ");
            String progresStr = scanner.next();
            showProgress = progresStr.equals("y") || progresStr.equals("Y");

            System.out.println("Загрузка..");
            StepikAPI stepikAPI = new StepikAPI(showProgress);
            List<Course> courses = stepikAPI.getTopNCourses(N);
            System.out.println("\n== Топ курсов ==");
            for(int i = 0; i < courses.size(); ++i){
                Course current = courses.get(i);
                System.out.printf("Курс #%d\n", i + 1);
                System.out.printf("ID: %d\n", current.getId());
                System.out.printf("Название: %s\n", current.getTitle());
                System.out.printf("Всего изучающих: %d чел.\n", current.getLearnersCount());
                System.out.println("- - -");
            }
        }catch (InputMismatchException ime){
            System.out.println("Некорректный ввод");
        }catch (CannotGetCoursesException cgce){
            System.out.println("Не удалось получить список курсов, причина: " + cgce.getMessage());
        }
    }

}
