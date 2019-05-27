package me.jho.wcg.wordcloud.api;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WcgService {
    @Multipart
    @POST("/wordcloud")
    Call<ResponseBody> generateWordCloud(
            @Part("title") RequestBody title, @Part("data") RequestBody data, @Part("font") RequestBody font
    );

    @Multipart
    @POST("/wordcloud")
    Call<ResponseBody> generateWordCloud(
            @Part("title") RequestBody Bodytitle, @Part("data") RequestBody data, @Part("font") RequestBody font, @Part MultipartBody.Part mask_image
    );


}
