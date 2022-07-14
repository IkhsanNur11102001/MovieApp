package com.nur_ikhsan.movieapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
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
import com.nur_ikhsan.movieapp.model.ModelTV;
import com.nur_ikhsan.movieapp.model.ModelTrailer;
import com.nur_ikhsan.movieapp.realm.RealmHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailTvActivity extends AppCompatActivity {
    TextView tvRealeseDate, tvDeskripsi, tv_title_movie;
    ImageView img_movie_cover, imgPhoto;
    RatingBar ratingBar;
    int Id;
    double RatingTV;
    String Name, Release, Deskripsi, Cover, Photo, UrlMovie;
    ModelTV modelTV;
    List<ModelTrailer> modelTrailerList = new ArrayList<>();
    AdapterTrailer adapterTrailer;
    RecyclerView rv_tv;
    FloatingActionButton btn_share;
    MaterialFavoriteButton btn_favTV;
    RealmHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >=21){
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }




        helper = new RealmHelper(this);

        tv_title_movie = findViewById(R.id.tv_title_movie);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        tvRealeseDate  = findViewById(R.id.tvRealeseDate);
        imgPhoto = findViewById(R.id.imgPhoto);
        img_movie_cover = findViewById(R.id.img_movie_cover);
        ratingBar = findViewById(R.id.ratingBar);

        rv_tv = findViewById(R.id.rv_trailerTv);
        rv_tv.setLayoutManager(new LinearLayoutManager(this));
        rv_tv.setHasFixedSize(true);

        modelTV = (ModelTV) getIntent().getSerializableExtra("detail");
        if (modelTV !=null){

            Id = modelTV.getId();
            Name = modelTV.getName();
            Release = modelTV.getReleaseDate();
            Deskripsi = modelTV.getOverview();
            Cover = modelTV.getBackdropPath();
            Photo = modelTV.getPosterPath();
            UrlMovie = ApiEndPoint.URLFILM+""+Id;
            RatingTV = modelTV.getVoteAverage();

            tv_title_movie.setText(Name);
            tvDeskripsi.setText(Deskripsi);
            tvRealeseDate.setText(Release);

            Glide.with(this).load(ApiEndPoint.URLIMAGE+Cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_movie_cover);
            Glide.with(this).load(ApiEndPoint.URLIMAGE+Photo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPhoto);

            float ratingMovie = (float) RatingTV;
            ratingBar.setRating(ratingMovie /2);
            ratingBar.setStepSize((float) 0.5);
            ratingBar.setNumStars(5);

            getTVtrailer();

            btn_share = findViewById(R.id.btn_share);
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String Name = modelTV.getName();
                    String Overview = modelTV.getOverview();
                    intent.putExtra(Intent.EXTRA_SUBJECT, Name);
                    intent.putExtra(Intent.EXTRA_TEXT, Name+"\n\n"+Overview+"\n\n"+UrlMovie);
                    startActivity(Intent.createChooser(intent, "Bagikan melalui : "));

                }
            });
            btn_favTV = findViewById(R.id.btn_fav_TV);
            btn_favTV.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                @Override
                public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                    if (favorite){
                        Id = modelTV.getId();
                        RatingTV = modelTV.getVoteAverage();
                        Name = modelTV.getName();
                        Release = modelTV.getReleaseDate();
                        Deskripsi = modelTV.getOverview();
                        Cover = modelTV.getBackdropPath();
                        Photo = modelTV.getPosterPath();
                        helper.addFavoritTV(Id, RatingTV, Name, Release, Deskripsi, Cover, Photo);
                        Snackbar.make(buttonView, modelTV.getName()+" Berhasil ditambahkan kefavorit", Snackbar.LENGTH_SHORT).show();
                    }else {
                        helper.deletTVfavorit(modelTV.getId());
                        Snackbar.make(buttonView, modelTV.getName()+" Berhasil dihapus dari favorit", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void getTVtrailer() {
        AndroidNetworking.get(ApiEndPoint.BASEURL+ApiEndPoint.TV_VIDEO+ApiEndPoint.APIKEY
        +ApiEndPoint.LANGUAGE)
                .addPathParameter("id", String.valueOf(Id))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int tv = 0; tv < jsonArray.length();tv++){
                                JSONObject jsonObject = jsonArray.getJSONObject(tv);

                                ModelTrailer trailerTV = new ModelTrailer();
                                trailerTV.setKey(jsonObject.getString("key"));
                                trailerTV.setType(jsonObject.getString("type"));
                                modelTrailerList.add(trailerTV);
                                showTV();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DetailTvActivity.this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void showTV() {
        adapterTrailer = new AdapterTrailer(DetailTvActivity. this, modelTrailerList);
        rv_tv.setAdapter(adapterTrailer);
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

}