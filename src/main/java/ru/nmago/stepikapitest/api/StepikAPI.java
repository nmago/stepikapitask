package ru.nmago.stepikapitest.api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nmago.stepikapitest.exception.CannotGetCoursesException;
import ru.nmago.stepikapitest.model.Course;
import ru.nmago.stepikapitest.model.StepikResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class StepikAPI {
    private String STEPIK_API_URL = "https://stepic.org:443/api/";
    private StepikService stepikService;
    private boolean showProgress;

    public StepikAPI(boolean showProgress){
        this.showProgress = showProgress;
        setupAPI();
    }

    private void setupAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(STEPIK_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        stepikService = retrofit.create(StepikService.class);
    }

    /**
     * Sets the API URL and recreates service
     * @param apiURL - API URL str
     */
    public void setStepikApiUrl(String apiURL){
        this.STEPIK_API_URL = apiURL;
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
        StepikResponse stepikResponse;
        int currentPage = 1;
        try {
            do {
                if(showProgress) System.out.println("Просмотр страницы #" + currentPage);
                Call<StepikResponse> getCoursesCaller = stepikService.getCourses(currentPage);
                stepikResponse = getCoursesCaller.execute().body();
                if(stepikResponse == null){
                    throw new CannotGetCoursesException("Response is null");
                }
                if(stepikResponse.getCourses() == null){
                    throw new CannotGetCoursesException("Response has not courses");
                }
                for(Course course : stepikResponse.getCourses()){
                    topNCourses.offer(course);
                    if(topNCourses.size() > N){
                        topNCourses.poll(); //removing course with less learners count
                    }
                }
                currentPage++;
            } while(stepikResponse.getMeta() != null && stepikResponse.getMeta().isHasNext());
        }catch (IOException ioe){
            throw new CannotGetCoursesException(ioe.getMessage());
        }

        List<Course> courseList = new ArrayList<>(topNCourses);
        courseList.sort(Comparator.comparingInt(Course::getLearnersCount).reversed());

        return courseList;
    }
}
