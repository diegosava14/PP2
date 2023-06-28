package com.example.giftgeek.API;

public class MethodsAPI {

    public static String URL_BASE = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/";
    public static String URL_LOGIN = URL_BASE + "users/login"; //@POST
    public static String URL_REGISTER = URL_BASE + "users"; //@POST
    public static String URL_GET_USER = URL_BASE + "users/search"; //@GET

    public static String URL_GET_USER_ID = URL_BASE + "users/"; //@GET

    public static String getUserByString(String search) { return URL_GET_USER + "?s=" + search; }

    public static String getUserById(int id){return URL_GET_USER_ID + id;}
}
