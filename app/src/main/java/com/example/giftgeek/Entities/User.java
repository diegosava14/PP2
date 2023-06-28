package com.example.giftgeek.Entities;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public static JSONObject registerUserJson(String name, String lastName, String email, String password, String image) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("last_name", lastName);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("image", image);
        } catch (JSONException e) {
            System.out.println("Error2: " + e);
            e.printStackTrace();
        }
        return jsonBody;
    }

    public static JSONObject signInUserJson(String email, String password) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            System.out.println("JSON: " + jsonBody);
        } catch (JSONException e) {
            System.out.println("Error2: " + e);
            e.printStackTrace();
        }
        return jsonBody;
    }
}


