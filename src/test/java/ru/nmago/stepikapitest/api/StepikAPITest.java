package ru.nmago.stepikapitest.api;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;
import ru.nmago.stepikapitest.exception.CannotGetCoursesException;
import ru.nmago.stepikapitest.model.Course;
import ru.nmago.stepikapitest.utils.RestMockServiceUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StepikAPITest {

    private MockWebServer mockWebServer;
    private static final String testServerURL = "stepikapitest/";

    @Before
    public void setup() throws IOException{
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @Test //N < 1
    public void getTopNCoursesInvalidN() throws IOException, CannotGetCoursesException{
        StepikAPI stepikAPI = new StepikAPI(true);
        List<Course> courses = stepikAPI.getTopNCourses(0);
        assertEquals(null, courses);
        courses = stepikAPI.getTopNCourses(-1);
        assertEquals(null, courses);
    }

    @Test //getting final page
    public void getTopNCoursesResponseOkFinal() throws IOException, CannotGetCoursesException{
        StepikAPI stepikAPI = new StepikAPI(true);
        stepikAPI.setStepikApiUrl(mockWebServer.url(testServerURL).toString());

        String resp = "final/courses_200_ok_response_last";
        mockWebServer.enqueue(new MockResponse()
                        .setResponseCode(200)
                        .setHeader("Content-Type", "application/json")
                        .setBody(RestMockServiceUtils.getResponseBodyStr(resp)));

        List<Course> courses = stepikAPI.getTopNCourses(3);
        List<Long> topIDS = RestMockServiceUtils.getResponseAnswers(resp);
        for(int i = 0; i < courses.size(); ++i){
            Course current = courses.get(i);
            assertEquals((long) topIDS.get(i), current.getId());
        }
    }

    @Test(expected = CannotGetCoursesException.class) //getting 503 header
    public void getTopNCoursesResponse503() throws IOException, CannotGetCoursesException {
        StepikAPI stepikAPI = new StepikAPI(true);
        stepikAPI.setStepikApiUrl(mockWebServer.url(testServerURL).toString());

        String resp = "final/courses_200_ok_response_last";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(503)
                .setBody(RestMockServiceUtils.getResponseBodyStr(resp)));

        List<Course> courses = stepikAPI.getTopNCourses(5);
    }

    @Test(expected = CannotGetCoursesException.class) //getting 404 header
    public void getTopNCoursesResponse404() throws IOException, CannotGetCoursesException {
        StepikAPI stepikAPI = new StepikAPI(true);
        stepikAPI.setStepikApiUrl(mockWebServer.url(testServerURL).toString());

        String resp = "final/courses_200_ok_response_last";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(RestMockServiceUtils.getResponseBodyStr(resp)));

        List<Course> courses = stepikAPI.getTopNCourses(5);
    }

    @Test //loading many pages
    public void getTopNCoursesResponseOkManyPages() throws IOException, CannotGetCoursesException{
        StepikAPI stepikAPI = new StepikAPI(true);
        stepikAPI.setStepikApiUrl(mockWebServer.url(testServerURL).toString());

        String resp = "manyPages/courses_200_ok_response";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(RestMockServiceUtils.getResponseBodyStr(resp + "_page1")));
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(RestMockServiceUtils.getResponseBodyStr(resp + "_page2")));
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(RestMockServiceUtils.getResponseBodyStr(resp + "_page3")));

        List<Long> topIDS = RestMockServiceUtils.getResponseAnswers(resp);
        List<Course> courses = stepikAPI.getTopNCourses(topIDS.size());
        for(int i = 0; i < courses.size(); ++i){
            Course current = courses.get(i);
            assertEquals((long) topIDS.get(i), current.getId());
        }
    }

}