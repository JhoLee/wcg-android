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

import me.jho.wcg.fragment.Menu1Fragment;
import me.jho.wcg.fragment.Menu2Fragment;
import me.jho.wcg.fragment.Menu3Fragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    // Fragment variables
    private FragmentManager fragmentManager;
    private Menu1Fragment menu1Fragment;
    private Menu2Fragment menu2Fragment;
    private Menu3Fragment menu3Fragment;

    // BottomNavigationView
    BottomNavigationView bottomNavigationView;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START Fragments]
        fragmentManager = getSupportFragmentManager();
        menu1Fragment = new Menu1Fragment();
        menu2Fragment = new Menu2Fragment();
        menu3Fragment = new Menu3Fragment();
        // [END Fragments]

        // [START bottomNavigationView]
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Set First Fragment
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();

        // Set EventListener to call Fragment when bottomNavigationView clicked
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fragmentTransaction = fragmentManager.beginTransaction();

                Object fragment = menu1Fragment;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_menu1:
                        fragment = menu1Fragment;
                        break;
                    case R.id.navigation_menu2:
                        fragment = menu2Fragment;
                        break;
                    case R.id.navigation_menu3:
                        fragment = menu3Fragment;
                        break;
                    default:
                        return false;
                }

                fragmentTransaction.replace(R.id.frame_layout, (Fragment) fragment).commitAllowingStateLoss();
                return true;
            }
        });

        // [END bottomNavigationView]
    }

}
