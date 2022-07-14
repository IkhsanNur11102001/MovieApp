package com.nur_ikhsan.movieapp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.nur_ikhsan.movieapp.R;
import com.nur_ikhsan.movieapp.activity.DetailTvActivity;
import com.nur_ikhsan.movieapp.adapter.AdapterHorizontalFilm;
import com.nur_ikhsan.movieapp.adapter.AdapterTV;
import com.nur_ikhsan.movieapp.adapter.Adapter_allFilm;
import com.nur_ikhsan.movieapp.api.ApiEndPoint;
import com.nur_ikhsan.movieapp.model.ModelFilm;
import com.nur_ikhsan.movieapp.model.ModelTV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Fragment_tv extends Fragment implements AdapterTV.onSelectdata {
    private RecyclerView rv_tv;
    private List<ModelTV> modeltv = new ArrayList<>();
    private AdapterTV adapterTV;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIcon(R.drawable.manscatalog);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Harap tunggu");
        progressDialog.setCancelable(false);

        rv_tv = view.findViewById(R.id.rv_tv);
        rv_tv.setLayoutManager(new LinearLayoutManager(getActivity()));
        

        getTv();
        return view;
    }


    @SuppressLint("NotifyDataSetChanged")
    private void foundMovie() {
        adapterTV = new AdapterTV(getActivity(), modeltv, this);
        rv_tv.setAdapter(adapterTV);
        adapterTV.notifyDataSetChanged();
    }

    private void getTv() {
        AndroidNetworking.get(ApiEndPoint.BASEURL + ApiEndPoint.TV_POPULER + ApiEndPoint.APIKEY
                        + ApiEndPoint.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            modeltv = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int tv = 0; tv < jsonArray.length(); tv++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(tv);

                                ModelTV dataTv = new ModelTV();
                                SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-mm-dd");
                                String tanggal = jsonObject.getString("first_air_date");

                                dataTv.setName(jsonObject.getString("name"));
                                dataTv.setId(jsonObject.getInt("id"));
                                dataTv.setPosterPath(jsonObject.getString("poster_path"));
                                dataTv.setReleaseDate(format.format(dateFormat.parse(tanggal)));
                                dataTv.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataTv.setPopularity(jsonObject.getString("popularity"));
                                dataTv.setOverview(jsonObject.getString("overview"));
                                dataTv.setVoteAverage(jsonObject.getDouble("vote_average"));
                                modeltv.add(dataTv);
                                showTV();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showTV() {
        adapterTV = new AdapterTV(getActivity(), modeltv, this);
        rv_tv.setAdapter(adapterTV);
        adapterTV.notifyDataSetChanged();
    }

    @Override
    public void onSelectdata(ModelTV modelTV) {
        Intent intent = new Intent(getActivity(), DetailTvActivity.class);
        intent.putExtra("detail", modelTV);
        startActivity(intent);
    }
}
