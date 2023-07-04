package com.example.giftgeek.API;

public class MethodsAPI {

    public static String URL_PIC = "https://balandrau.salle.url.edu/i3/repositoryimages/photo/";
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

    public static String getUrlPic(String name){
        if(name.equals("Luffy")){
            return "https://balandrau.salle.url.edu/i3/repositoryimages/photo/b858bacf-e88f-48be-aa57-1f23d5aaf186.jpg";
        }

        if(name.equals("Kirby")){
            return "https://balandrau.salle.url.edu/i3/repositoryimages/photo/3f5354ce-1e3d-46b0-8574-dc739663446c.jpg";
        }

        if(name.equals("Dog")){
            return "https://balandrau.salle.url.edu/i3/repositoryimages/photo/d5c28747-dbed-47f0-9df4-67203352e91a.jpg";
        }
        if(name.equals("Ferrari")){
            return "https://balandrau.salle.url.edu/i3/repositoryimages/photo/865dd4ac-ca8e-4efe-af8f-b8baf945dffc.jpg";
        }
        if(name.equals("Porsche")){
            return "https://balandrau.salle.url.edu/i3/repositoryimages/photo/b1cb5396-f13f-4417-8dba-e168382f4177.jpg";
        }
        if(name.equals("Cat")){
            return "https://balandrau.salle.url.edu/i3/repositoryimages/photo/bd9ea0a5-ff8f-4c33-9e03-493dbd9587c8.jpg";
        }

        return null;
    }
}
