package com.nur_ikhsan.movieapp.realm;

import android.content.Context;

import com.nur_ikhsan.movieapp.model.ModelFilm;
import com.nur_ikhsan.movieapp.model.ModelTV;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {
    private Context mContext;
    Realm realm;
    private RealmResults<ModelFilm> modelFilm;
    private RealmResults<ModelTV>modelTVS;

    public RealmHelper(Context context) {
        this.mContext = context;
        Realm.init(context);
        realm = Realm.getDefaultInstance();

    }

    public ArrayList<ModelTV> shwoFavoritTVhsow(){
        ArrayList<ModelTV> dataTV = new ArrayList<>();
        modelTVS = realm.where(ModelTV.class).findAll();
        if (modelTVS.size() >0){
            for (int tv = 0; tv <modelTVS.size();tv++){
                ModelTV tvshow = new ModelTV();

                tvshow.setId(modelTVS.get(tv).getId());
                tvshow.setVoteAverage(modelTVS.get(tv).getVoteAverage());
                tvshow.setName(modelTVS.get(tv).getName());
                tvshow.setReleaseDate(modelTVS.get(tv).getReleaseDate());
                tvshow.setOverview(modelTVS.get(tv).getOverview());
                tvshow.setBackdropPath(modelTVS.get(tv).getBackdropPath());
                dataTV.add(tvshow);
            }
        }
        return dataTV;
    }


    public void addFavoritTV(int Id, double RatingTv, String Name, String Release, String Deskripsi, String Cover){

        ModelTV modelTV = new ModelTV();
       modelTV.setId(Id);
       modelTV.setVoteAverage(RatingTv);
       modelTV.setName(Name);
       modelTV.setReleaseDate(Release);
       modelTV.setOverview(Deskripsi);
       modelTV.setBackdropPath(Cover);

        realm.beginTransaction();
        realm.copyToRealm(modelTV);
        realm.commitTransaction();
    }




    public ArrayList<ModelFilm> showFavoriteMovie() {
        ArrayList<ModelFilm> dataFavorite = new ArrayList<>();
        modelFilm = realm.where(ModelFilm.class).findAll();

        if (modelFilm.size() > 0) {
            for (int fav = 0; fav < modelFilm.size(); fav++) {

                ModelFilm movie = new ModelFilm();
                movie.setId(modelFilm.get(fav).getId());
                movie.setReleaseDate(modelFilm.get(fav).getReleaseDate());
                movie.setOverview(modelFilm.get(fav).getOverview());
                movie.setTitle(modelFilm.get(fav).getTitle());
                movie.setBackdropPath(modelFilm.get(fav).getBackdropPath());
                movie.setVoteAverage(modelFilm.get(fav).getVoteAverage());
                dataFavorite.add(movie);

            }
        }
        return dataFavorite;
    }

    public void addFavoriteMovie(int Id, String Release, String Deskripsi, String Title, String Cover, double RatingMovie) {
        ModelFilm film = new ModelFilm();

        film.setId(Id);
        film.setReleaseDate(Release);
        film.setOverview(Deskripsi);
        film.setTitle(Title);
        film.setBackdropPath(Cover);
        film.setVoteAverage(RatingMovie);

        realm.beginTransaction();
        realm.copyToRealm(film);
        realm.commitTransaction();
    }

    public void deletFilmFavorit(int id){
            realm.beginTransaction();
            RealmResults<ModelFilm> filmRealmResults = realm.where(ModelFilm.class).findAll();
            filmRealmResults.deleteAllFromRealm();
            realm.commitTransaction();
    }
    public void deletTVfavorit(int id){
        realm.beginTransaction();
        RealmResults<ModelTV> modelTVRealmResults = realm.where(ModelTV.class).findAll();
        modelTVRealmResults.deleteAllFromRealm();
        realm.commitTransaction();

    }
}
