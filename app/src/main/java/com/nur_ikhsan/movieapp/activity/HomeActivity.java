package com.nur_ikhsan.movieapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.nur_ikhsan.movieapp.R;
import com.nur_ikhsan.movieapp.behavior.BottomBarBehavior;
import com.nur_ikhsan.movieapp.fragment.FragmentMovie;
import com.nur_ikhsan.movieapp.fragment.FragmentSearch;
import com.nur_ikhsan.movieapp.fragment.FragmentTVshow;

public class HomeActivity extends AppCompatActivity {
    BubbleNavigationLinearView bubble_navigasi;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bubble_navigasi = findViewById(R.id.buble_navigasi);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentMovie()).commit();

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bubble_navigasi.getLayoutParams();
        layoutParams.setBehavior(new BottomBarBehavior());
        bubble_navigasi.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                switch (position){

                    case 0:
                        fragment = new FragmentMovie();
                        break;
                    case 1:
                        fragment = new FragmentTVshow();
                        break;
                    case 2:
                        fragment = new FragmentSearch();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
            }
        });
    }
}