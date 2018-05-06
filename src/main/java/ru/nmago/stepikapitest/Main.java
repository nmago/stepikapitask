package ru.nmago.stepikapitest;

import ru.nmago.stepikapitest.api.StepikAPI;
import ru.nmago.stepikapitest.model.Course;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        int N;
        boolean showProgress;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите N: ");
        N = scanner.nextInt();

        System.out.println("Выводить прогресс? (y/n): ");
        showProgress = scanner.next().equals("y");

        System.out.println("Загрузка..");
        StepikAPI stepikAPI = new StepikAPI(showProgress);
        List<Course> courses = stepikAPI.getTopNCourses(N);

        System.out.println("== Топ курсов ==");
        for(int i = 0; i < courses.size(); ++i){
            Course current = courses.get(i);
            System.out.printf("Курс #%d\n", i + 1);
            System.out.printf("Название: %s\n", current.getTitle());
            System.out.printf("Всего изучающих: %d чел.\n", current.getLearnersCount());
            System.out.println("- - -");
        }
    }

}
