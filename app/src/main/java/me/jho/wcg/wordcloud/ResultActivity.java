package me.jho.wcg.wordcloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jho.wcg.R;
import me.jho.wcg.db.SQLiteHelper;

public class ResultActivity extends AppCompatActivity {

    @BindView(R.id.imageView_result)
    ImageView resultImageView;
    @BindView(R.id.textView_resultTitleContent)
    TextView resultTitleTextView;
    @BindView(R.id.textView_resultDataContent)
    TextView resultDataTextView;
    @BindView(R.id.textView_resultFontContent)
    TextView resultFontTextView;
    @BindView(R.id.textView_resultBackgroundColorContent)
    TextView resultBackgroundTextView;

    @BindView(R.id.button_save)
    Button saveButton;
    @BindView(R.id.button_share)
    Button shareButton;
    @BindView(R.id.button_remove)
    Button removeButton;

    private SQLiteDatabase db;

    private Cursor result;
    private AppCompatDialog progressDialog;

    private String title;
    private String data;
    private String font;
    private byte[] wordCloudByte;
    private Bitmap wordCloudBitmap;

    // [START onCreate]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        SQLiteHelper sqLiteHelper = new SQLiteHelper(this, SQLiteHelper.NAME, null, SQLiteHelper.VERSION);
        db = sqLiteHelper.getReadableDatabase();


        Intent intent = getIntent();

        long rowId = Objects.requireNonNull(intent.getExtras()).getLong("rowId") + 1;
        Log.d("ResultActivity:onCreate", String.valueOf(rowId));

        result = selectResult(rowId);

        ButterKnife.bind(this);


        if (result.moveToFirst()) {
            title = result.getString(1);
            data = result.getString(2);
            font = result.getString(3);
            wordCloudByte = result.getBlob(5);

            resultTitleTextView.setText(title);
            resultDataTextView.setText(data);
            resultFontTextView.setText(font);
            resultBackgroundTextView.setText("white");
            wordCloudBitmap = BitmapFactory.decodeByteArray(wordCloudByte, 0, wordCloudByte.length);
            resultImageView.setImageBitmap(wordCloudBitmap);


        }


    }
    // [END onCreate]


    // [START selectResult]
    public Cursor selectResult(long id) {
        String sql = "SELECT * FROM wordcloud WHERE _id = " + id + ";";
        Cursor result = db.rawQuery(sql, null);

        return result;

    }
    // [END selectResult]


    // [START saveImage]
    @OnClick(R.id.button_save)
    public void saveImage() {
        progressON(this, null);
        saveImage(wordCloudBitmap, "wordcloud", title);
        progressOFF();

    }

    public void saveImage(Bitmap finalBitmap, String type, String title) {
        String exStorage = Environment.getExternalStorageDirectory().getAbsolutePath(); // Get Absolute Path in External Sdcard String foler_name = "/"+folder+"/"; String file_name = name+".jpg"; String string_path = ex_storage+foler_name; File file_path; try{ file_path = new File(string_path); if(!file_path.isDirectory()){ file_path.mkdirs(); } FileOutputStream out = new FileOutputStream(string_path+file_name);
        String folderName = "/" + type + "/";
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyMMdd_HHmmss");


        String now = dateTimeFormat.format(new Date());
        String fName = type + "-" + title + "_" + now + ".png";
        File image = new File(Environment.getExternalStorageDirectory() + folderName, fName);

        FileOutputStream out = null;

        try {
            out = new FileOutputStream(image);
            wordCloudBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(image)));

        Log.d("saveImage", "Image saved to >> " + image.getAbsolutePath());

    }
    // [END saveImage]


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
