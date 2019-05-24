package me.jho.wcg.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jho.wcg.R;

public class WordCloudFragment extends Fragment {
    View wordCloudView;
    Context wordCloudContext;
    Activity wordCloudActivity;
    private Unbinder unbinder;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
