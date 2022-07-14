package com.nur_ikhsan.movieapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.nur_ikhsan.movieapp.R;
import com.nur_ikhsan.movieapp.adapter.Adapter_allFilm;
import com.nur_ikhsan.movieapp.model.ModelFilm;
import com.nur_ikhsan.movieapp.realm.RealmHelper;

import java.util.ArrayList;
import java.util.List;

public class FilmfavoritActivity extends AppCompatActivity implements Adapter_allFilm.selectAllfilm {
    private RecyclerView rv_favoritFilm;
    private List<ModelFilm>modelFilms = new ArrayList<>();
    private Adapter_allFilm adapterAllFilm;
    private RealmHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmfavorit);

        helper = new RealmHelper(getApplicationContext());

        rv_favoritFilm = findViewById(R.id.rv_favorite);
        rv_favoritFilm.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterAllFilm = new Adapter_allFilm(getApplicationContext(), modelFilms, this);
        rv_favoritFilm.setHasFixedSize(true);
        
        setDataFilmFavorit();

    }

    private void setDataFilmFavorit() {
        modelFilms = helper.showFavoriteMovie();
        rv_favoritFilm.setAdapter(new Adapter_allFilm(getApplicationContext(), modelFilms, this));
    }

    @Override
    public void selectAllFilm(ModelFilm modelFilm) {
        Intent intent = new Intent(getApplicationContext(), DetailActivityFilm.class);
        intent.putExtra("detail", modelFilm);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setDataFilmFavorit();
    }
}