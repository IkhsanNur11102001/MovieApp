package com.nur_ikhsan.movieapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.nur_ikhsan.movieapp.activity.DetailTvActivity;
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

public class Fragment_search extends Fragment implements Adapter_allFilm.selectAllfilm, AdapterTV.onSelectdata{
    RecyclerView rv_search;
    private Adapter_allFilm adapterAllFilm;
    private AdapterTV adapterTV;
    private List<ModelTV>modelTVS = new ArrayList<>();
    private List<ModelFilm>modelFilms = new ArrayList<>();
    SearchView multisearch;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rv_search = view.findViewById(R.id.rv_search);
        rv_search.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_search.setHasFixedSize(true);


        multisearch = view.findViewById(R.id.multi_search);
        multisearch.setQueryHint("Cari disini...");

        multisearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                carifilm(query);
                caritv(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              if (newText.equals(""))
              carifilm(newText);
              caritv(newText);
                return false;
            }
        });
        return view;

    }


    private void carifilm(String query) {
        AndroidNetworking.get(ApiEndPoint.BASEURL+ApiEndPoint.MULTI_SEARCH+ApiEndPoint.APIKEY+ApiEndPoint.QUERY+query
                +ApiEndPoint.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            modelFilms = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int cari = 0; cari<jsonArray.length();cari++){
                                JSONObject jsonObject = jsonArray.getJSONObject(cari);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dMMM yyy");
                                SimpleDateFormat format = new SimpleDateFormat("yyy-mm-dd");
                                String tanggalfilm = jsonObject.getString("release_date");

                                ModelFilm dataAll = new ModelFilm();
                                dataAll.setId(jsonObject.getInt("id"));
                                dataAll.setTitle(jsonObject.getString("title"));
                                dataAll.setReleaseDate(dateFormat.format(format.parse(tanggalfilm)));
                                dataAll.setPopularity(jsonObject.getString("popularity"));
                                dataAll.setOverview(jsonObject.getString("overview"));
                                dataAll.setBackdropPath(jsonObject.getString("backdrop_path"));
                                dataAll.setPosterPath(jsonObject.getString("poster_path"));
                                dataAll.setVoteAverage(jsonObject.getDouble("vote_average"));
                                modelFilms.add(dataAll);
                                showFilm();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }


    private void caritv(String query) {
        AndroidNetworking.get(ApiEndPoint.BASEURL+ApiEndPoint.MULTI_SEARCH+ApiEndPoint.APIKEY+ApiEndPoint.QUERY+query
                        +ApiEndPoint.LANGUAGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            modelTVS = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int cari = 0; cari<jsonArray.length();cari++){
                                JSONObject jsonObject = jsonArray.getJSONObject(cari);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dMMM yyy");
                                SimpleDateFormat format = new SimpleDateFormat("yyy-mm-dd");
                                String tanggaltv = jsonObject.getString("first_air_date");

                                ModelTV datatv = new ModelTV();
                                datatv.setId(jsonObject.getInt("id"));
                                datatv.setName(jsonObject.getString("name"));
                                datatv.setReleaseDate(dateFormat.format(format.parse(tanggaltv)));
                                datatv.setPopularity(jsonObject.getString("popularity"));
                                datatv.setOverview(jsonObject.getString("overview"));
                                datatv.setBackdropPath(jsonObject.getString("backdrop_path"));
                                datatv.setPosterPath(jsonObject.getString("poster_path"));
                                datatv.setVoteAverage(jsonObject.getDouble("vote_average"));
                                modelTVS.add(datatv);
                                showTV();
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();

                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    private void showTV() {
        adapterTV = new AdapterTV(getActivity(), modelTVS, this);
        rv_search.setAdapter(adapterTV);
        adapterTV.notifyDataSetChanged();

    }


    private void showFilm() {
        adapterAllFilm = new Adapter_allFilm(getActivity(), modelFilms, this);
        rv_search.setAdapter(adapterAllFilm);
        adapterAllFilm.notifyDataSetChanged();
    }


    @Override
    public void selectAllFilm(ModelFilm modelFilm) {
        Intent intent = new Intent(getActivity(), DetailActivityFilm.class);
        intent.putExtra("detail", modelFilm);
        startActivity(intent);
    }


    @Override
    public void onSelectdata(ModelTV modelTV) {
        Intent intent = new Intent(getActivity(), DetailTvActivity.class);
        intent.putExtra("detail", modelTV);
        startActivity(intent);
    }

}
