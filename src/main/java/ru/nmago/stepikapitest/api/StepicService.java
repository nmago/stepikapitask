package ru.nmago.stepikapitest.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nmago.stepikapitest.model.StepicResponse;

public interface StepicService {

    @GET("courses")
    Call<StepicResponse> getCourses(@Query("page") int page);

}
