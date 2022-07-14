package com.nur_ikhsan.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nur_ikhsan.movieapp.R;
import com.nur_ikhsan.movieapp.api.ApiEndPoint;
import com.nur_ikhsan.movieapp.model.ModelTrailer;

import java.util.List;

public class AdapterTrailer extends RecyclerView.Adapter<AdapterTrailer.Trailer> {
    private Context context;
    private List<ModelTrailer>modelTrailers;

    public AdapterTrailer(Context context, List<ModelTrailer> modelTrailers) {
        this.context = context;
        this.modelTrailers = modelTrailers;
    }

    @NonNull
    @Override
    public Trailer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_trailer, parent, false);
        return new Trailer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Trailer holder, int position) {
        final ModelTrailer dataTrailer = modelTrailers.get(position);
        holder.btn_trailer.setText(dataTrailer.getType());
        holder.btn_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v="+dataTrailer.getKey()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelTrailers.size();
    }

    class Trailer extends RecyclerView.ViewHolder {
        public Button btn_trailer;
        public Trailer(@NonNull View itemView) {
            super(itemView);
            btn_trailer = itemView.findViewById(R.id.btn_trailer);
        }
    }
}
