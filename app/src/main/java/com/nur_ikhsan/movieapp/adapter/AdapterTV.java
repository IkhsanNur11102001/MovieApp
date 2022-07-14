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
import com.nur_ikhsan.movieapp.model.ModelTV;

import java.util.List;

public class AdapterTV extends RecyclerView.Adapter<AdapterTV.TvShow> {
    private Context context;
    private List<ModelTV>modelTVS;
    private AdapterTV.onSelectdata selectdata;
    double Rating;

    public interface onSelectdata{
        void onSelectdata(ModelTV modelTV);
    }

    public AdapterTV(Context context, List<ModelTV> modelTVS, onSelectdata selectdata) {
        this.context = context;
        this.modelTVS = modelTVS;
        this.selectdata = selectdata;
    }

    @NonNull
    @Override
    public TvShow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cover_tv, parent, false);
       return new TvShow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShow holder, int position) {
        final ModelTV dataTv = modelTVS.get(position);

        Rating = dataTv.getVoteAverage();
        holder.tvTitle.setText(dataTv.getName());
        holder.tvRealeseDate.setText(dataTv.getReleaseDate());
        holder.tvDeskripsi.setText(dataTv.getOverview());

        CountDownTimer count = new CountDownTimer(1000, 5000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                Glide.with(context).load(ApiEndPoint.URLIMAGE+dataTv.getPosterPath())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_baseline_image_24)
                                .transform(new RoundedCorners(16)))
                        .into(holder.img_film);
                holder.progressBar.setVisibility(View.GONE);
            }
        };
        count.start();
        float newRating = (float) Rating;
        holder.ratingMovie.setNumStars(5);
        holder.ratingMovie.setStepSize((float) 0.5);
        holder.ratingMovie.setRating(newRating / 2);


        holder.list_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectdata.onSelectdata(dataTv);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelTVS.size();
    }

    class TvShow extends RecyclerView.ViewHolder {
        public TextView tvTitle,tvDeskripsi, tvRealeseDate;
        public RelativeLayout list_tv;
        public RatingBar ratingMovie;
        public ImageView img_film;
        public ProgressBar progressBar;

        public TvShow(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvRealeseDate = itemView.findViewById(R.id.tvRealeseDate);
            list_tv = itemView.findViewById(R.id.list_tv);
            ratingMovie = itemView.findViewById(R.id.ratingBar);
            img_film = itemView.findViewById(R.id.imgPhoto);
            progressBar = itemView.findViewById(R.id.progress_image);
        }
    }
}
