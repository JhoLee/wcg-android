package me.jho.wcg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jho.wcg.fragment.InfoFragment;
import me.jho.wcg.fragment.WordCloudFragment;
import me.jho.wcg.fragment.GalleryFragment;

public class MainActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
}
