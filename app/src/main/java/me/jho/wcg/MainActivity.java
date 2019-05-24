package me.jho.wcg;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jho.wcg.fragment.InfoFragment;
import me.jho.wcg.fragment.WordCloudFragment;
import me.jho.wcg.fragment.GalleryFragment;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {

    private static final int INTERNET_PERMISSION = 2019;
    private TextView mTextMessage;

    // Fragment variables
    private FragmentManager fragmentManager;
    private WordCloudFragment wordCloudFragment;
    private GalleryFragment galleryFragment;
    private InfoFragment infoFragment;

    // BottomNavigationView
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;
    FragmentTransaction fragmentTransaction;
    private final int MY_PERMISSIONS_INTERNET = 1;

    // [START onCreate]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        checkPermissions();


//        if (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.INTERNET)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                    Manifest.permission.INTERNET)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{Manifest.permission.READ_CONTACTS},
//                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
//            // Permission has already been granted
//        }
        // [END request_the_permissions]

        // [START Fragments]
        fragmentManager = getSupportFragmentManager();
        wordCloudFragment = new WordCloudFragment();
        galleryFragment = new GalleryFragment();
        infoFragment = new InfoFragment();
        // [END Fragments]

        // [START bottomNavigationView]

        // Set First Fragment
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, wordCloudFragment).commitAllowingStateLoss();

        // Set EventListener to call Fragment when bottomNavigationView clicked
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fragmentTransaction = fragmentManager.beginTransaction();
                Object fragment = wordCloudFragment;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_menu1:
                        fragment = wordCloudFragment;
                        break;
                    case R.id.navigation_menu2:
                        fragment = galleryFragment;
                        break;
                    case R.id.navigation_menu3:
                        fragment = infoFragment;
                        break;
                    default:
                        return true;
                }
                fragmentTransaction.replace(R.id.frame_layout, (Fragment) fragment).commitAllowingStateLoss();
                return true;
            }
        });
        // [END bottomNavigationView]
    }
    // [END onCreate]

    // [START onResume]
    @Override
    public void onResume() {
        super.onResume();
        checkPermissionF();
    }
    // [END onResume]


    // [START check_permission]
    private void checkPermissionF() {

        if (android.os.Build.VERSION.SDK_INT >= M) {
            // only for LOLLIPOP and newer versions
            int permissionResult = getApplicationContext().checkSelfPermission(Manifest.permission.INTERNET);

            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.INTERNET)) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                    dialog.setTitle("Need Permission")
                            .setMessage("Need permission to connect to the internet. \\n continue?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_INTERNET);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "Canceled permission", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .create()
                            .show();
                } else {
                    // First time to require permission
                    requestPermissions(new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_INTERNET);

                }
            } else {
                // Permission Already Granted


            }
        } else {
            // Under Marshmallow
        }
    }
    // [END check_permission]


    /**
     * 사용자가 권한을 허용했는지 거부했는지 체크
     *
     * @param requestCode  MY_PERMISSIONS_INTERNET
     * @param permissions  개발자가 요청한 권한들
     * @param grantResults 권한에 대한 응답들
     */

    // [START on_request_permissions_result]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_INTERNET:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
        }
    }
    // [END on_request_permissions_result]
}


