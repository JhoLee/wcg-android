package me.jho.wcg.fragment;

import static android.R.layout.simple_spinner_item;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private Unbinder unbinder;

    View wordCloudView;
    Context wordCloudContext;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        wordCloudView = inflater.inflate(R.layout.fragment_wordcloud, container, false);
        wordCloudContext = wordCloudView.getContext();
        unbinder = ButterKnife.bind(this, wordCloudView);
        retrofit = new Retrofit.Builder()
                .baseUrl(wcgService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        wcgService = retrofit.create(WcgService.class);

//        initFontSpinner();
        initFontSpinnerTest();

        return wordCloudView;
    }

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

    // [START init_fontSpinner]

    @OnClick(R.id.button_maskImage)
    public void initFontSpinner() {

        Call<String> call = wcgService.getFonts();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("font:onResponse", response.body().toString());
                Toast.makeText(wordCloudContext, "font:onResponse", Toast.LENGTH_LONG).show();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("font:onSuccess", response.body().toString());
                        String jsonResponse = response.body().toString();
                        spinJSON(jsonResponse);
                    } else {
                        Log.i("font:onEmptyResponse", "Returned empty response");
                    }
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(wordCloudContext, "font:onFailure", Toast.LENGTH_LONG).show();

            }


        });
    }

    private void spinJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("status").equals("true")) {
                fonts = new ArrayList<>();
                JSONArray dataArray = jsonObject.getJSONArray("");

                for (int i = 0; i < dataArray.length(); i++) {
                    FontVO fontVO = new FontVO();
                    JSONObject dataObject = dataArray.getJSONObject(i);

                    fontVO.setName(dataObject.getString("name"));
                    fontVO.setName(dataObject.getString("path"));

                    fonts.add(fontVO);

                }

                for (int i = 0; i < fonts.size(); i++) {
                    fontSpinnerList.add(fonts.get(i).getName().toString());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(wordCloudContext, simple_spinner_item, fontSpinnerList);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fontSpinner.setAdapter(spinnerArrayAdapter);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // [END init_fontSpinner]


}
