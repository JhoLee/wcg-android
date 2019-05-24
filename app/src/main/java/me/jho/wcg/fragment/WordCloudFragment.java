package me.jho.wcg.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import me.jho.wcg.R;
import me.jho.wcg.api.FontVO;
import me.jho.wcg.api.WcgService;
import me.jho.wcg.view.FontSpinnerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordCloudFragment extends Fragment {
    private static final int MY_PERMISSIONS_INTERNET = 2019;
    private static final int MY_PERMISSIONS_READ_STORAGE = 2020;
    private static final int MY_PERMISSIONS_WRITE_STORAGE = 2021;

    private int internetPermissionCehck;

    private Unbinder unbinder;

    View wordCloudView;
    Context wordCloudContext;
    Activity mainActivity;

    @BindView(R.id.editText_title)
    EditText titleEditText;
    @BindView(R.id.editText_data)
    EditText dataEditTExt;
    @BindView(R.id.button_maskImage)
    Button maskImageButton;
    @BindView(R.id.spinner_font)
    Spinner fontSpinner;

    String[] spinnerNames;

    Retrofit retrofit;
    WcgService wcgService;

    private ArrayList<FontVO> fonts;
    private ArrayList<String> fontSpinnerList;
    private int selectedFontIndex = 0;

    public WordCloudFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        wordCloudView = inflater.inflate(R.layout.fragment_wordcloud, container, false);
        wordCloudContext = wordCloudView.getContext();
        unbinder = ButterKnife.bind(this, wordCloudView);
        mainActivity = getActivity();
        assert mainActivity != null;
        internetPermissionCehck = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.INTERNET);

        initRetrofit();

//        getFontList();

        // [START init_font_spinner]

        // [END init_font_spinner]

        // initFontSpinner();
        initFontSpinnerTest();

        return wordCloudView;
    }

    // [START init_retrofit]
    private void initRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://jho6.iptime.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.wcgService = this.retrofit.create(WcgService.class);
    }
    // [END init_retrofit]

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // [START TEST_init_fontSpinner]
    public void initFontSpinnerTest() {
        spinnerNames = new String[]{"test1", "test2", "test33"};

        FontSpinnerAdapter fontSpinnerAdapter = new FontSpinnerAdapter(wordCloudContext, spinnerNames);
        fontSpinner.setAdapter(fontSpinnerAdapter);


    }

    @OnItemSelected(R.id.spinner_font)
    public void fontSpinnerItemSelected(Spinner spinner, int position) {
        selectedFontIndex = spinner.getSelectedItemPosition();
        Toast.makeText(wordCloudContext, spinnerNames[selectedFontIndex], Toast.LENGTH_LONG).show();
    }

    @OnItemSelected(value = R.id.spinner_font, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    public void fontSpinnerNothingSelected() {
        Toast.makeText(wordCloudContext, "Nothing selected", Toast.LENGTH_SHORT).show();
    }
    // [END TEST_init_fontSpinner]

    // [START require_internet_permission]
    @OnClick(R.id.button_maskImage)
    public void requireInternetPermission() {
        Toast.makeText(wordCloudContext, "require intetnert permission", Toast.LENGTH_LONG).show();
        if (internetPermissionCehck != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(wordCloudContext, "requireihihihihision", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_INTERNET);
        }
    }
    // [END require_internet_permission]

    // [START on_request_permissions_result]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_INTERNET:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
        }
    }
    // [END on_request_permissions_result]

//    // [START init_fontSpinner]
//
//    @OnClick(R.id.button_maskImage)
//    public void initFontSpinner() {
//
//        Call<String> call = wcgService.getFonts();
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.i("font:onResponse", response.body().toString());
//                Toast.makeText(wordCloudContext, "font:onResponse", Toast.LENGTH_LONG).show();
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        Log.i("font:onSuccess", response.body().toString());
//                        String jsonResponse = response.body().toString();
//                        spinJSON(jsonResponse);
//                    } else {
//                        Log.i("font:onEmptyResponse", "Returned empty response");
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(wordCloudContext, "font:onFailure", Toast.LENGTH_LONG).show();
//
//            }
//
//
//        });
//    }
//
//    private void spinJSON(String response) {
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            if (jsonObject.optString("status").equals("true")) {
//                fonts = new ArrayList<>();
//                JSONArray dataArray = jsonObject.getJSONArray("");
//
//                for (int i = 0; i < dataArray.length(); i++) {
//                    FontVO fontVO = new FontVO();
//                    JSONObject dataObject = dataArray.getJSONObject(i);
//
//                    fontVO.setName(dataObject.getString("name"));
//                    fontVO.setName(dataObject.getString("path"));
//
//                    fonts.add(fontVO);
//
//                }
//
//                for (int i = 0; i < fonts.size(); i++) {
//                    fontSpinnerList.add(fonts.get(i).getName().toString());
//                }
//
//                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(wordCloudContext, simple_spinner_item, fontSpinnerList);
//                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                fontSpinner.setAdapter(spinnerArrayAdapter);
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//    // [END init_fontSpinner]


    // [START get_fontList]
    @OnClick(R.id.button_generate)
    public void getFontList() {

        Call<List<FontVO>> request = wcgService.getfontList();
        request.enqueue(new Callback<List<FontVO>>() {
            @Override
            public void onResponse(@NonNull Call<List<FontVO>> call, @NonNull Response<List<FontVO>> response) {

                assert response.body() != null;
                Toast.makeText(wordCloudContext, response.body().toString(), Toast.LENGTH_SHORT).show();
                //TODO
                fonts = (ArrayList<FontVO>) response.body();
                for (FontVO font : fonts) {
                    Log.d("onResponse", font.getName());

                }

            }

            @Override
            public void onFailure(Call<List<FontVO>> call, Throwable t) {

                Toast.makeText(wordCloudContext, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        // TODO: Test this
    }
    // [END get_fontList]
}
