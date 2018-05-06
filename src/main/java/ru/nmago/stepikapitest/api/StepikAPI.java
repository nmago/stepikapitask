package ru.nmago.stepikapitest.api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nmago.stepikapitest.model.Course;
import ru.nmago.stepikapitest.model.StepicResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class StepikAPI {
    private static final String STEPIC_API_URL = "https://stepic.org:443/api/";
    private StepicService stepicService;
    private boolean showProgress;

    public StepikAPI(boolean showProgress){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(STEPIC_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        stepicService = retrofit.create(StepicService.class);
        this.showProgress = showProgress;
    }

    public List<Course> getTopNCourses(int N){
        if(N < 1) return null;
        PriorityQueue<Course> topNCourses = new PriorityQueue<>(N,
                Comparator.comparingInt(Course::getLearnersCount));
        StepicResponse stepicResponse;
        int currentPage = 1;
        try {
            do {
                if(showProgress) System.out.println("Просмотр страницы #" + currentPage);
                Call<StepicResponse> getCoursesCaller = stepicService.getCourses(currentPage);
                stepicResponse = getCoursesCaller.execute().body();
                for(Course course : stepicResponse.getCourses()){
                    topNCourses.offer(course);
                    if(topNCourses.size() > N){
                        topNCourses.poll(); //removing course with less learners count
                    }
                }
                currentPage++;
            } while(stepicResponse.getMeta().isHasNext());
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

        List<Course> courseList = new ArrayList<>(topNCourses);
        courseList.sort(Comparator.comparingInt(Course::getLearnersCount).reversed());

        return courseList;
    }
}
