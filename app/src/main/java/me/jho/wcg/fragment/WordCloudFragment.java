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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.jho.wcg.R;

public class WordCloudFragment extends Fragment {

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    @NonNull
    private View wordCloudView;

    @NonNull
    private Context wordCloudContext;
    private File tempFile;

    // [START onCreateView()]
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        wordCloudView = inflater.inflate(R.layout.fragment_wordCloud, container, false);
        wordCloudContext = wordCloudView.getContext();

        // 권한 요청
        tedPermission();

        return wordCloudView;
    }
    // [END onCreateView()]

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(wordCloudContext, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();

            Cursor cursor = null;

            try {
                /*
                 * Uri 스키마를
                 * content:/// 에서 file:/// 로 변경.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;

                cursor = wordCloudContext.getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            setImage();
        }
        // TODO: 예외 처리.

    }
    // [END onActivityResult]

    // [START setImage()]
    private void setImage() {
        ImageView imageView = wordCloudView.findViewById(R.id.mask_imageView);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        imageView.setImageBitmap(originalBm);
    }
    // [END setImage()]

    // [START takePhoto()]
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(wordCloudContext, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//            finish();
            e.printStackTrace();
        }

        if (tempFile != null) {

            Uri photoUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }
    // [END takePhoto()]

    // [START createImageFile()]
    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( wordcloud_mask_{time}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "wordcloud_mask_" + timeStamp + "_";

        // 이미지 저장될 폴더 이름 ( wordcloud_mask)
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/wordcloud_mask/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".png", storageDir);

        return image;

    }
    // [END createImageFile()]

    // [START tedPermission()]
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패

            }
        };

        TedPermission.with(wordCloudContext)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_request))
                .setDeniedMessage(getResources().getString(R.string.permission_denied))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }
    // [END tedPermission()]

    // [START goToAlbum()]
    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);

    }
    // [END goToAlbum()]


}
