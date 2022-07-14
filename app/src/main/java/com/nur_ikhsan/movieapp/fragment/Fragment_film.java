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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.nur_ikhsan.movieapp.R;
import com.nur_ikhsan.movieapp.activity.DetailActivityFilm;
import com.nur_ikhsan.movieapp.adapter.AdapterHorizontalFilm;
import com.nur_ikhsan.movieapp.adapter.Adapter_allFilm;
import com.nur_ikhsan.movieapp.api.ApiEndPoint;
import com.nur_ikhsan.movieapp.model.ModelFilm;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Fragment_film extends Fragment implements AdapterHorizontalFilm.selectDataFilm, Adapter_allFilm.selectAllfilm {
    private AdapterHorizontalFilm adapterHorizontalFilm;
    private Adapter_allFilm adapterAllFilm;
    private RecyclerView rv_cover, rv_allfilm;
    private List<ModelFilm> modelHorizontal = new ArrayList<>();
    private List<ModelFilm> modelAllfilm = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film, container, false);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIcon(R.drawable.manscatalog);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Harap tunggu");
        progressDialog.setCancelable(false);


        rv_cover = view.findViewById(R.id.rv_cover);
        rv_cover.setHasFixedSize(true);
        rv_cover.setLayoutManager(new CardSliderLayoutManager(getActivity()));
        new CardSnapHelper().attachToRecyclerView(rv_cover);



        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.sweeperRef);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().replace(R.id.frame_laout, new Fragment_film()).commit();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        rv_allfilm = view.findViewById(R.id.rv_all_film);
        rv_allfilm.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_allfilm.setHasFixedSize(true);

        getAllfilm();
        getCoverFilm();
        return view;
    }

    private void getAllfilm() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndPoint.BASEURL + ApiEndPoint.MOVIE_POPULAR + ApiEndPoint.APIKEY + ApiEndPoint.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            modelAllfilm = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int f = 0; f < jsonArray.length(); f++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(f);
                                ModelFilm dataFilm = new ModelFilm();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                                String tanggal = jsonObject.getString("release_date");

                                dataFilm.setId(jsonObject.getInt("id"));
                                dataFilm.setReleaseDate(dateFormat.format(format.parse(tanggal)));
                                dataFilm.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataFilm.setTitle(jsonObject.getString("title"));
                                dataFilm.setOverview(jsonObject.getString("overview"));
                                dataFilm.setPopularity(jsonObject.getString("popularity"));
                                dataFilm.setPosterPath(jsonObject.getString("poster_path"));
                                dataFilm.setBackdropPath(jsonObject.getString("backdrop_path"));
                                modelAllfilm.add(dataFilm);
                                ShowAllFilm();
                            }

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }




    private void getCoverFilm() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndPoint.BASEURL + ApiEndPoint.MOVIE_PLAYNOW + ApiEndPoint.APIKEY
                        + ApiEndPoint.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            modelHorizontal = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                               SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMMM yyyy");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                                String tanggal = jsonObject.getString("release_date");
                                ModelFilm dataApi = new ModelFilm();

                                dataApi.setId(jsonObject.getInt("id"));
                                dataApi.setPosterPath(jsonObject.getString("poster_path"));
                                dataApi.setVoteAverage(jsonObject.getDouble("vote_average"));
                                dataApi.setOverview(jsonObject.getString("overview"));
                                dataApi.setPopularity(jsonObject.getString("popularity"));
                                dataApi.setTitle(jsonObject.getString("title"));
                                dataApi.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataApi.setReleaseDate(dateFormat.format(format.parse(tanggal)));
                                modelHorizontal.add(dataApi);
                                showFilmCover();

                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showFilmCover() {
        adapterHorizontalFilm = new AdapterHorizontalFilm(getActivity(), modelHorizontal, this);
        rv_cover.setAdapter(adapterHorizontalFilm);
        adapterHorizontalFilm.notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void ShowAllFilm() {
        adapterAllFilm = new Adapter_allFilm(getActivity(), modelAllfilm, this);
        rv_allfilm.setAdapter(adapterAllFilm);
        adapterAllFilm.notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void foundMovie() {
        adapterAllFilm = new Adapter_allFilm(getActivity(), modelAllfilm, this);
        rv_allfilm.setAdapter(adapterAllFilm);
        adapterAllFilm.notifyDataSetChanged();
    }

    @Override
    public void selectDataFilm(ModelFilm modelFilm) {
        Intent intent = new Intent(getActivity(), DetailActivityFilm.class);
        intent.putExtra("detail", modelFilm);
        startActivity(intent);
    }

    @Override
    public void selectAllFilm(ModelFilm modelFilm) {
        Intent intent = new Intent(getActivity(), DetailActivityFilm.class);
        intent.putExtra("detail", modelFilm);
        startActivity(intent);

    }
}
