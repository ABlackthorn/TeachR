package com.teachr.teachr;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.teachr.teachr.ui.home.HomeFragment;

public class HomeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        //getSupportActionBar()?.hide(); // hide the title bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          //      WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.home_activity);

        getSupportFragmentManager().beginTransaction().replace(R.id.content, HomeFragment.newInstance()).commitNow();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new MyNavigationItemSelectedListener(getApplicationContext(), getSupportFragmentManager()));
        bottomNavigationView.setSelectedItemId(R.id.navigation_recherche);

    }

}

class MyNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private FragmentManager manager;

    public MyNavigationItemSelectedListener(Context context, FragmentManager manager){
        this.context = context;
        this.manager = manager;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.navigation_mescours) {
            manager.beginTransaction().replace(R.id.content, MyCoursesFragment.newInstance()).commitNow();
        } else if(item.getItemId() == R.id.navigation_profil) {
            manager.beginTransaction().replace(R.id.content, ProfileFragment.newInstance()).commitNow();
        } else if(item.getItemId() == R.id.navigation_recherche) {
            manager.beginTransaction().replace(R.id.content, HomeFragment.newInstance()).commitNow();
        }
        return true;
    }

}
