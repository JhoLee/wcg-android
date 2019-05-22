package me.jho.wcg.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WcgService {
    String URL = "http://jho6.iptime.org";

    @GET("/static/font")
    Call<String> getFonts();
}
