package me.jho.wcg.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jho.wcg.R;
import me.jho.wcg.api.NetRetrofit;
import me.jho.wcg.api.WcgService;
import me.jho.wcg.model.Order;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class InfoFragment extends Fragment {

    private Unbinder unbinder;

    private Retrofit retrofit;
    WcgService wcgService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View infoView = inflater.inflate(R.layout.fragment_menu2, container, false);
        unbinder = ButterKnife.bind(this, infoView);

        // [START retrofit]
        retrofit = new Retrofit.Builder()
                .baseUrl(WcgService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        wcgService = retrofit.create(WcgService.class);

        // [END retrofit]

        Log.d("hi", "hi");
        // TEST
        testPost3(infoView);

        return infoView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void testPost3(View view) {
//        String id = editText.getText().toString();
        String title = "title_test";
        String data = "data_test";
        String font = "font_test";
        String mask_image = "mask_image_test";

        String id = "JhoLee";

        if (!id.isEmpty()) {
            Call<ArrayList<JsonObject>> res = NetRetrofit.getInstance().getService().getListRepos(id);
            res.enqueue(new Callback<ArrayList<JsonObject>>() {
                @Override
                public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                    Log.d("Retrofit", response.toString());
                    if (response.body() != null)
                        Log.d("TEST", response.body().toString());
                }

                @Override
                public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                    Log.e("Err", t.getMessage());
                }
            });
        }
    }

    private void testPost() {
        Log.d("testPost(): ", "start");
        HashMap<String, Object> input = new HashMap<>();
        input.put("title", "this is title!!");
        input.put("data", "this is body!");
        input.put("font", "test-font");
        input.put("mask_image", "test_image");
        Log.d("testPost(): ", "made");
        wcgService.postOrder(input).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                Log.d("testPost(): ", "onResponse()");
                if (response.isSuccessful()) {
                    Log.d("testPost(): ", "Successful()");
                    Order body = response.body();
                    if (body != null) {
                        Log.d("data.title()", body.getTitle() + "");
                        Log.d("data.data()", body.getData() + "");
                        Log.d("data.font()", body.getFont() + "");
                        Log.d("data.mask_image()", body.getMaskImage() + "");
                        Log.e("postOrder end", "======================================");
                    } else {
                        Log.w("testPost()", "Body is null");
                    }
                } else {
                    Log.w("testPost()", "Not Sucessful");

                }
            }


            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                Log.w("testPost()", "Fail.");

            }
        });

        Log.d("testPost()", "??");
    }

}

