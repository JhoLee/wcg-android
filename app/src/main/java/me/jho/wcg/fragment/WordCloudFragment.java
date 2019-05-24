package me.jho.wcg.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.jho.wcg.R;

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

                setImage(imageView);
            }
        }
    }
    // [END onActivityResult]

    // [START setImage]
    private void setImage(ImageView imageView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBmp = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        imageView.setImageBitmap(originalBmp);

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
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
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


}
