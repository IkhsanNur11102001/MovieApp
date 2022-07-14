package com.nur_ikhsan.movieapp.api;

public class ApiEndPoint {

    //REQUIRED
    public static String BASEURL = "https://api.themoviedb.org/3/";
    public static String APIKEY = "api_key=0472f7d4ffa78d6681edf657193c8625";
    public static String LANGUAGE = "&language=en-US";
    public static String URLIMAGE = "https://image.tmdb.org/t/p/w780/";
    public static String URLFILM = "https://www.themoviedb.org/movie/";

    //SEARCH MOVIE/TV SHOW
    public static String SEARCH_MOVIE = "search/movie?";
    public static String SEARCH_TV = "search/tv?";
    public static String QUERY = "&query=";
    public static String MULTI_SEARCH = "search/multi?";

    //FOR MOVIE
    public static String MOVIE_PLAYNOW = "movie/now_playing?";
    public static String MOVIE_POPULAR = "discover/movie?";
    public static String MOVIE_VIDEO = "movie/{id}/videos?";

    //FOR TV SHOW
    public static String TV_PLAYNOW = "tv/on_the_air?";
    public static String TV_POPULER = "discover/tv?";
    public static String TV_VIDEO = "tv/{id}/videos?";




    // NOTIFICATION
    public static String NOTIF_DATE = "&primary_release_date.gte=";
    public static String REALESE_DATE = "&primary_release_date.lte=";


}
