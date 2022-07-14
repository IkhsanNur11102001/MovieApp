package com.nur_ikhsan.movieapp.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nur_ikhsan.movieapp.R;
import com.nur_ikhsan.movieapp.api.ApiEndPoint;
import com.nur_ikhsan.movieapp.model.ModelFilm;

import java.util.List;

public class Adapter_allFilm extends RecyclerView.Adapter<Adapter_allFilm.Film> {
    private Context context;
    private List<ModelFilm>modelFilm;
    private Adapter_allFilm.selectAllfilm selectAllfilm;
    private double Rating;

    public interface selectAllfilm{
        void selectAllFilm(ModelFilm modelFilm);
    }

    public Adapter_allFilm(Context context, List<ModelFilm> modelFilm, Adapter_allFilm.selectAllfilm selectAllfilm) {
        this.context = context;
        this.modelFilm = modelFilm;
        this.selectAllfilm = selectAllfilm;
    }

    @NonNull
    @Override
    public Film onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_all_film, parent, false);
        return new Film(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Film holder, int position) {
        final ModelFilm film = modelFilm.get(position);


        Rating = film.getVoteAverage();
        holder.tvTitle.setText(film.getTitle());
        holder.tvRealeseDate.setText(film.getReleaseDate());
        holder.tvDeskripsi.setText(film.getOverview());

        CountDownTimer count = new CountDownTimer(1000, 5000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Glide.with(context).load(ApiEndPoint.URLIMAGE+film.getPosterPath())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_baseline_image_24)
                                .transform(new RoundedCorners(10)))
                        .into(holder.img_film);
                holder.progressBar.setVisibility(View.GONE);

            }
        };
        count.start();


        float newRating = (float) Rating;
        holder.ratingMovie.setNumStars(5);
        holder.ratingMovie.setStepSize((float) 0.5);
        holder.ratingMovie.setRating(newRating / 2);



        holder.card_Film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAllfilm.selectAllFilm(film);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelFilm.size();
    }

    class Film extends RecyclerView.ViewHolder {
        public TextView tvTitle,tvDeskripsi, tvRealeseDate;
        public RelativeLayout card_Film;
        public RatingBar ratingMovie;
        public ImageView img_film;
        public ProgressBar progressBar;

        public Film(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvRealeseDate = itemView.findViewById(R.id.tvRealeseDate);
            card_Film = itemView.findViewById(R.id.cvFilm);
            ratingMovie = itemView.findViewById(R.id.ratingBar);
            img_film = itemView.findViewById(R.id.imgPhoto);
            progressBar = itemView.findViewById(R.id.progress_image);

        }
    }
}
