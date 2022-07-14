package com.nur_ikhsan.movieapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.nur_ikhsan.movieapp.R;
import com.nur_ikhsan.movieapp.fragment.Fragment_film;
import com.nur_ikhsan.movieapp.fragment.Fragment_search;
import com.nur_ikhsan.movieapp.fragment.Fragment_tv;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView botnav;
    Toolbar toolbar;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbarhome);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() !=null;
        getSupportActionBar();



        botnav = findViewById(R.id.bot_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_laout, new Fragment_film()).commit();
        botnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){

                    case R.id.bot_home:
                        fragment = new Fragment_film();
                        break;
                    case R.id.bot_tv:
                        fragment = new Fragment_tv();
                        break;
                    case R.id.bot_search:
                        fragment = new Fragment_search();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_laout, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     getMenuInflater().inflate(R.menu.optionmenu, menu);
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.notif_setting){
            startActivity(new Intent(this, SettingActivity.class));
        }else {
            if (item.getItemId()==R.id.favorite){
                startActivity(new Intent(this, FilmfavoritActivity. class));
            }
        }if (item.getItemId()==R.id.tvshow){
            startActivity(new Intent(this, TvshowFavoritActivity. class));
        }
        return true;
    }
}