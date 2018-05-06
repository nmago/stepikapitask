package ru.nmago.stepikapitest.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nmago.stepikapitest.model.StepikResponse;

public interface StepikService {

    @GET("courses")
    Call<StepikResponse> getCourses(@Query("page") int page);

}
