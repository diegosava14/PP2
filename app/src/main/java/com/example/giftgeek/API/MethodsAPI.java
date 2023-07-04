package com.example.giftgeek.API;

public class MethodsAPI {

    public static String URL_PRODUCTS = "https://balandrau.salle.url.edu/i3/mercadoexpress/api/v1/products";
    public static String URL_BASE = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/";
    public static String URL_BASE_PRODUCTS = "https://balandrau.salle.url.edu/i3/mercadoexpress/api-docs/v1/";
    public static String URL_LOGIN = URL_BASE + "users/login"; //@POST
    public static String URL_REGISTER = URL_BASE + "users"; //@POST
    public static String URL_GET_USER = URL_BASE + "users/search"; //@GET
    public static String URL_GET_USER_ID = URL_BASE + "users/"; //@GET

    public static String getUserByString(String search) { return URL_GET_USER + "?s=" + search; }

    public static String getUserById(int id){return URL_GET_USER_ID + id;}

    public static String getReservedGifts(int id){return URL_GET_USER_ID + id + "/gifts/reserved";}

    public static String getWishLists(int id){return URL_GET_USER_ID + id + "/wishlists";}
}
