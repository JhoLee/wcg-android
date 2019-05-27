package me.jho.wcg.gallery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jho.wcg.R;

public class GalleryFragment extends Fragment {
    private View galleryView;
    private Context galleryContext;
    private Activity galleryActivity;

    @BindView(R.id.recyclerView_gallery)
    RecyclerView galleryRecyclerView;

    private static ArrayList<GalleryVO> galleryArrayList;

    private Unbinder unbinder;
    private GalleryAdapter galleryAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        galleryView = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryContext = this.getContext();
        galleryActivity = this.getActivity();

        unbinder = ButterKnife.bind(this, galleryView);

        // [START set_data_into_list]
        galleryArrayList = new ArrayList<>();
        /* TODO: TEST CODE */
        for (int i = 0; i < 15; i++) {
            galleryArrayList.add(new GalleryVO(i, "title" + i, "default", "white"));
        }
        /* */
        // [END set_data_into_list]

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

}
