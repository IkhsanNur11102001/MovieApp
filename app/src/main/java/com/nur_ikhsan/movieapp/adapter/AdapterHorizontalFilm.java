package com.nur_ikhsan.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.nur_ikhsan.movieapp.R;
import com.nur_ikhsan.movieapp.api.ApiEndPoint;
import com.nur_ikhsan.movieapp.model.ModelFilm;

import java.util.List;

public class AdapterHorizontalFilm extends RecyclerView.Adapter<AdapterHorizontalFilm.Film> {
    private Context mContext;
    private List<ModelFilm>modelFilm;
    private AdapterHorizontalFilm.selectDataFilm selecDataFilm;

    public interface selectDataFilm{
        void selectDataFilm(ModelFilm modelFilm);
    }

    public AdapterHorizontalFilm(Context Context, List<ModelFilm> modelFilm, selectDataFilm xDataFilm) {
        this.mContext = Context;
        this.modelFilm = modelFilm;
        this.selecDataFilm = xDataFilm;
    }

    @NonNull
    @Override
    public Film onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cover_film, parent, false);
        return new Film(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Film holder, int position) {
        final ModelFilm data = modelFilm.get(position);
       Glide.with(mContext).load(ApiEndPoint.URLIMAGE+data.getPosterPath())
                       .apply(new RequestOptions()
                               .placeholder(R.drawable.ic_baseline_image_24)
                               .transform(new RoundedCorners(10)))
                               .into(holder.img_film);

        holder.img_film.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecDataFilm.selectDataFilm(data);
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelFilm.size();
    }

    class Film extends RecyclerView.ViewHolder {

        public ImageView img_film;

        public Film(@NonNull View itemView) {
            super(itemView);
            img_film = itemView.findViewById(R.id.cover_film);
        }
    }
}
