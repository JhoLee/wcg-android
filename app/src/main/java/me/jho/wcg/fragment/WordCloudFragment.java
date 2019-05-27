package me.jho.wcg.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.jho.wcg.R;
import me.jho.wcg.api.WcgRetrofit;
import me.jho.wcg.api.WcgService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordCloudFragment extends Fragment {
    private static final int PICK_FROM_ALBUM = 500;


    // views
    View wordCloudView;
    Context wordCloudContext;
    Activity wordCloudActivity;

    @BindView(R.id.button_maskImage)
    Button maskImageButton;

    @BindView(R.id.button_generate)
    Button generateButton;

    @BindView(R.id.editText_title)
    EditText titleEditText;

    @BindView(R.id.editText_data)
    EditText dataEditText;

    @BindView(R.id.imageView)
    ImageView imageView;


    private Unbinder unbinder;


    private File tempFile;
    private AppCompatDialog progressDialog;


    //[START onCreateView]
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        wordCloudView = inflater.inflate(R.layout.fragment_wordcloud, container, false);
        wordCloudContext = this.getContext();
        wordCloudActivity = this.getActivity();

        unbinder = ButterKnife.bind(this, wordCloudView);

        tedPermission();

        return wordCloudView;
    }
    // [END onCreateView]

    // [START onDestroyView]
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    // [END onDestroyView]


    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                if (data == null)
                    break;
                Uri photoUri = data.getData();
                Cursor cursor = null;

                try {
                    /*
                      Change Uri schema to 'file:///' form 'content:///'
                     */
                    String[] proj = {MediaStore.Images.Media.DATA};

                    assert photoUri != null;
                    cursor = wordCloudActivity.getContentResolver()
                            .query(photoUri, proj, null, null, null);
                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();

                    tempFile = new File(cursor.getString(column_index));
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                setImage(imageView, tempFile);
            }
        }
    }
    // [END onActivityResult]

    // [START setImage]
    private void setImage(ImageView imageView, File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBmp = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        imageView.setImageBitmap(originalBmp);
        Log.d("setImage", "Image set.");

        progressOFF();
        //end Loading
    }

    private void setImage(ImageView imageView, ResponseBody responseBody) {
        Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(responseBody.byteStream()));
        imageView.setImageBitmap(bitmap);
        Log.d("setImage", "Image set.");

        progressOFF();
        //end Loading
    }
    // [END setImage]


    // [START tedPermission()]
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // Granted
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // Denied

            }
        };

        TedPermission.with(wordCloudActivity)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.need_permission))
                .setDeniedMessage(getResources().getString(R.string.permission_setting))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET)
                .check();
    }
    // [END tedPermission()]

    // [START goToAlbum]
    @OnClick(R.id.button_maskImage)
    public void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }
    // [END goToAlbum]


    // [START generateWordCloud]
    @SuppressLint("LongLogTag")
    @OnClick(R.id.button_generate)
    public void generateWordCloud() {

        // [START get_data_from_form]
        String title = String.valueOf(titleEditText.getText());
        String data = String.valueOf(dataEditText.getText());
        String font = "NanumGothicBold"; // default..
        File maskImage = tempFile;
//        String font = String.valueOf(fontEditText.getText());

        // [END get_data_from_form]

        // [START validate_form]
        // todo: 언젠가 여기에 입력되지 않은 데이타 입력하라고 알려주는 소스 추가..
        if (title.equals("") || data.equals("") || font.equals("")) {
            // Do nothing.
            Log.d("generateWordCloud", "FormValidation failed.");
            return;
        }
        Log.d("generateWordCloud", "Title: " + title);
        Log.d("generateWordCloud", "Data: " + data);
        Log.d("generateWordCloud", "Font: " + font);
        // [END validate_form]

        // [START prepare_multi_part]
        RequestBody partTitle = createPartFromString(title);
        RequestBody partData = createPartFromString(data);
        RequestBody partFont = createPartFromString(font);
        Log.d("generateWordCloud:prepareMultiPart", "MultiPart prepared.");
        MultipartBody.Part partImage = null;
        if (maskImage != null) {
            partImage = prepareFilePart("mask_image", tempFile);
            Log.d("generateWordCloud:prepareMultiPart", "MaskImagePart prepared.");
        }
        // [END prepare_multi_part]

        progressON(wordCloudActivity,null);
        //[START loading]

        // [START send_data_and_receive]
        WcgService wcgService = WcgRetrofit.getWcgRetrofit().create(WcgService.class);

        Call<ResponseBody> call = null;
        if (partImage == null) {
            call = wcgService.generateWordCloud(partTitle, partData, partFont);
        } else {
            call = wcgService.generateWordCloud(partTitle, partData, partFont, partImage);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("generateWordCloud:onResponse", response.body().toString());
                /* TODO: test setting image */
                setImage(imageView, response.body());
                /* */
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.w("generateWordCloud:onFailure", t.toString());

            }
        });
        // [END send_data_and_receive]



        // TODO: Receive result image from server


    }
    // [END generateWordCloud]


    // [START createPartFromString]
    @NonNull
    private RequestBody createPartFromString(String string) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), string);
    }
    // [END createPartFromString]

    // [START prepareFilePart]
    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = new File(getRealPathFromURI(fileUri));
        return prepareFilePart(partName, file);
    }
    // [END prepareFilePart]

    // [START getRealPathFromUri]
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(wordCloudContext, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();


        return result;
    }
    // [END getRealPathFromUri]


    public void progressON(Activity activity, String message) {

        if (activity == null || activity.isFinishing()) {
            return;
        }


        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {

            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_loading);
            progressDialog.show();

        }


        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }


    }
    public void progressSET(String message) {

        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

    }
    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
