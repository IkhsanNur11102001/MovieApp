package com.nur_ikhsan.movieapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.nur_ikhsan.movieapp.R;
import com.nur_ikhsan.movieapp.adapter.AdapterTV;
import com.nur_ikhsan.movieapp.model.ModelTV;
import com.nur_ikhsan.movieapp.realm.RealmHelper;

import java.util.ArrayList;
import java.util.List;

public class TvshowFavoritActivity extends AppCompatActivity implements AdapterTV.onSelectdata {
private RecyclerView rv_favorit; AdapterTV adapterTV;
private List<ModelTV>modelTVS = new ArrayList<>();
private RealmHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_favorit);

        helper = new RealmHelper(getApplicationContext());
        rv_favorit = findViewById(R.id.rv_favorite_tv);
        adapterTV = new AdapterTV(getApplicationContext(), modelTVS, this);
        rv_favorit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_favorit.setHasFixedSize(true);

        getFavoritTV();
    }

    private void getFavoritTV() {
        modelTVS = helper.shwoFavoritTVhsow();
        rv_favorit.setAdapter(new AdapterTV(getApplicationContext(), modelTVS, this));

    }

    @Override
    public void onSelectdata(ModelTV modelTV) {
        Intent intent = new Intent(getApplicationContext(), DetailTvActivity.class);
        intent.putExtra("detail", modelTV);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getFavoritTV();
    }
}