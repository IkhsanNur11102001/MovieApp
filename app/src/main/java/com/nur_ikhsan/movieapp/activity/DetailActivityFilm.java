package com.nur_ikhsan.movieapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nur_ikhsan.movieapp.R;
import com.nur_ikhsan.movieapp.adapter.AdapterTrailer;
import com.nur_ikhsan.movieapp.api.ApiEndPoint;
import com.nur_ikhsan.movieapp.model.ModelFilm;
import com.nur_ikhsan.movieapp.model.ModelTrailer;
import com.nur_ikhsan.movieapp.realm.RealmHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivityFilm extends AppCompatActivity {
    TextView tvRealeseDate, tvDeskripsi, tv_title_movie;
    ImageView img_movie_cover;
    RatingBar ratingBar;
    int Id;
    double RatingMovie;
    String Release, Deskripsi, Title, Cover, UrlMovie;
    ModelFilm modelFilm;
    List<ModelTrailer> modelTrailerList = new ArrayList<>();
    AdapterTrailer adapterTrailer;
    RecyclerView rv_trailer;
    FloatingActionButton btn_share;
    RealmHelper helper;
    MaterialFavoriteButton btn_favFil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >=21){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        btn_share = findViewById(R.id.btn_share);
        btn_favFil = findViewById(R.id.btn_fav_film);
        tv_title_movie = findViewById(R.id.tv_title_movie);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        tvRealeseDate = findViewById(R.id.tvRealeseDate);
        img_movie_cover = findViewById(R.id.img_movie_cover);
        ratingBar = findViewById(R.id.ratingBar);

        rv_trailer = findViewById(R.id.rv_trailer);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                2, LinearLayout.VERTICAL);
        rv_trailer.setLayoutManager(staggeredGridLayoutManager);
        rv_trailer.setHasFixedSize(true);

        helper = new RealmHelper(this);

        modelFilm = (ModelFilm) getIntent().getSerializableExtra("detail");
        if (modelFilm !=null){

            Id = modelFilm.getId();
            Release = modelFilm.getReleaseDate();
            Deskripsi = modelFilm.getOverview();
            Title = modelFilm.getTitle();
            Cover = modelFilm.getBackdropPath();
            UrlMovie = ApiEndPoint.URLFILM+""+Id;
            RatingMovie = modelFilm.getVoteAverage();

            tv_title_movie.setText(Title);
            tvDeskripsi.setText(Deskripsi);
            tvRealeseDate.setText(Release);
            tv_title_movie.setSelected(true);


            float newRating = (float) RatingMovie;
            ratingBar.setNumStars(5);
            ratingBar.setStepSize((float) 0.5);
            ratingBar.setRating(newRating/ 2);


            Glide.with(this).load(ApiEndPoint.URLIMAGE+Cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_movie_cover);
            showTrailer();

            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String title = modelFilm.getTitle();
                    String overview = modelFilm.getOverview();
                    intent.putExtra(Intent.EXTRA_SUBJECT, title);
                    intent.putExtra(Intent.EXTRA_TEXT, title+"\n\n"+overview+"\n\n" +UrlMovie);
                    startActivity(Intent.createChooser(intent, "Bagikan melalui : "));


                }
            });

            btn_favFil.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                @Override
                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                   if (favorite){
                       Id = modelFilm.getId();
                       Release = modelFilm.getReleaseDate();
                       Deskripsi = modelFilm.getOverview();
                       Title = modelFilm.getTitle();
                       Cover = modelFilm.getBackdropPath();
                       RatingMovie = modelFilm.getVoteAverage();
                       helper.addFavoriteMovie(Id, Release, Deskripsi, Title, Cover, RatingMovie);
                       Snackbar.make(buttonView, modelFilm.getTitle() + " Berhasil ditambahkan kefavorit",
                               Snackbar.LENGTH_SHORT).show();
                   }else {
                       helper.deletFilmFavorit(modelFilm.getId());
                       Snackbar.make(buttonView, modelFilm.getTitle()+ " Berhasil dihapus dari favaorit",
                               Snackbar.LENGTH_SHORT).show();
                   }

                }
            });
        }
    }

    public static void setWindowFlag(Activity activity, final  int trailer, boolean maman ) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (maman){
            layoutParams.flags |= trailer;
        }else {
            layoutParams.flags &= ~trailer;
        }
        window.setAttributes(layoutParams);
    }


    private void showTrailer() {
        AndroidNetworking.get(ApiEndPoint.BASEURL+ApiEndPoint.MOVIE_VIDEO+ ApiEndPoint.APIKEY
                +ApiEndPoint.LANGUAGE)
                .addPathParameter("id", String.valueOf(Id))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                               for (int t = 0; t <jsonArray.length();t++){

                                   JSONObject jsonObject = jsonArray.getJSONObject(t);
                                   ModelTrailer trailer = new ModelTrailer();
                                  trailer.setType(jsonObject.getString("type"));
                                  trailer.setKey(jsonObject.getString("key"));
                                  modelTrailerList.add(trailer);
                                  getTrailer();

                               }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DetailActivityFilm.this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getTrailer() {
        adapterTrailer = new AdapterTrailer(DetailActivityFilm.this, modelTrailerList);
        rv_trailer.setAdapter(adapterTrailer);
        adapterTrailer.notifyDataSetChanged();

    }
}