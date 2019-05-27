package me.jho.wcg.wordcloud;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jho.wcg.R;
import me.jho.wcg.db.SQLiteHelper;

public class ResultActivity extends AppCompatActivity {

    @BindView(R.id.textView_resultTitleContent)
    TextView resultTitleTextView;
    @BindView(R.id.textView_resultDataContent)
    TextView resultDataTextView;
    @BindView(R.id.imageView_result)
    ImageView resultImageView;

    private SQLiteDatabase db;

    private Cursor result;


    // [START onCreate]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        SQLiteHelper sqLiteHelper = new SQLiteHelper(this, SQLiteHelper.NAME, null, SQLiteHelper.VERSION);
        db = sqLiteHelper.getReadableDatabase();


        Intent intent = getIntent();

        Long rowId = intent.getExtras().getLong("rowId");

        result = selectResult(rowId);

        ButterKnife.bind(this);


        if (result.moveToFirst()) {
            String title = result.getString(1);
            String data = result.getString(2);
            String font = result.getString(3);
            byte[] wordCloudByte = result.getBlob(5);

            resultTitleTextView.setText(title);
            resultDataTextView.setText(data);
            resultImageView.setImageBitmap(BitmapFactory.decodeByteArray(wordCloudByte, 0, wordCloudByte.length));


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

}
