package ru.nmago.stepikapitest.api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nmago.stepikapitest.exception.CannotGetCoursesException;
import ru.nmago.stepikapitest.model.Course;
import ru.nmago.stepikapitest.model.StepicResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class StepikAPI {
    private String STEPIC_API_URL = "https://stepic.org:443/api/";
    private StepicService stepicService;
    private boolean showProgress;

    public StepikAPI(boolean showProgress){
        this.showProgress = showProgress;
        setupAPI();
    }

    private void setupAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(STEPIC_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        stepicService = retrofit.create(StepicService.class);
    }

    /**
     * Sets the API URL and recreates service
     * @param apiURL - API URL str
     */
    public void setStepicApiUrl(String apiURL){
        this.STEPIC_API_URL = apiURL;
        setupAPI();
    }

    /**
     * Finds top-N courses by learners count
     * @param N - set how many courses is need to return from top
     * @return - list of most popular courses (by learners count)
     * @throws CannotGetCoursesException
     */
    public List<Course> getTopNCourses(int N) throws CannotGetCoursesException{
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
                if(stepicResponse == null){
                    throw new CannotGetCoursesException("Response is null");
                }
                if(stepicResponse.getCourses() == null){
                    throw new CannotGetCoursesException("Response has not courses");
                }
                for(Course course : stepicResponse.getCourses()){
                    topNCourses.offer(course);
                    if(topNCourses.size() > N){
                        topNCourses.poll(); //removing course with less learners count
                    }
                }
                currentPage++;
            } while(stepicResponse.getMeta() != null && stepicResponse.getMeta().isHasNext());
        }catch (IOException ioe){
            throw new CannotGetCoursesException(ioe.getMessage());
        }

        List<Course> courseList = new ArrayList<>(topNCourses);
        courseList.sort(Comparator.comparingInt(Course::getLearnersCount).reversed());

        return courseList;
    }
}
