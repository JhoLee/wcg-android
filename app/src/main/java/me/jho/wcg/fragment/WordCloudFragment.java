package me.jho.wcg.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import me.jho.wcg.R;

public class WordCloudFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View wordCloudView = inflater.inflate(R.layout.fragment_menu1, container, false);
        ButterKnife.bind(this, wordCloudView);


        return wordCloudView;
    }
}
