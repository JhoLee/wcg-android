package me.jho.wcg.gallery;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jho.wcg.R;
import me.jho.wcg.db.SQLiteHelper;

public class GalleryFragment extends Fragment {
    private View galleryView;
    private Context galleryContext;
    private Activity galleryActivity;

    @BindView(R.id.recyclerView_gallery)
    RecyclerView galleryRecyclerView;

    private static ArrayList<GalleryVO> galleryArrayList;

    private Unbinder unbinder;
    private GalleryAdapter galleryAdapter;
    private SQLiteDatabase db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        galleryView = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryContext = this.getContext();
        galleryActivity = this.getActivity();

        unbinder = ButterKnife.bind(this, galleryView);

        db = galleryActivity.openOrCreateDatabase(SQLiteHelper.NAME, Context.MODE_PRIVATE, null);

        // [START set_data_into_list]
        galleryArrayList = selectAllData();
        // [END set_data_into_list]
//      // [START set_data_into_list]
//        galleryArrayList = new ArrayList<>();
//        /* TODO: TEST CODE */
//        for (int i = 0; i < 15; i++) {
//            galleryArrayList.add(new GalleryVO(i, "title" + i, "default", "white"));
//        }
//        /* */
//        // [END set_data_into_list]

        galleryRecyclerView.setHasFixedSize(true);

        //
        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(galleryContext));
        //
        galleryAdapter = new GalleryAdapter(galleryArrayList);
        galleryRecyclerView.setAdapter(galleryAdapter);

        return galleryView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    // [START selectAllData]
    public ArrayList<GalleryVO> selectAllData() {
        ArrayList<GalleryVO> galleryList = new ArrayList<>();

        String sql = "SELECT * FROM wordcloud;";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();

        while (!results.isAfterLast()) {
            long _id = results.getLong(0);
            String title = results.getString(1);
            String font = results.getString(3);
            byte[] wordCloudByte = results.getBlob(5);
            galleryList.add(new GalleryVO(_id, title, font, "white", wordCloudByte));


            results.moveToNext();

        }

        results.close();
        return galleryList;
    }
    // [END selectAllData]

}
