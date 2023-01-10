package com.example.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.contract.FilterUI;
import com.example.model.Config;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterPresenter {
    private List<String> dicheck;
    private SharedPreferences sp;
    private HashMap<String,String>hashMap;
    private final static String tagsURL = Config.BASE_URL + "tags";
    private FilterUI ui;

    public FilterPresenter(FilterUI ui) {
        this.ui = ui;
        sp = ui.getAct().getPreferences(Context.MODE_PRIVATE);
        hashMap = new HashMap<>();
//        if(sp.contains("checkTag")){
        if(sp.getString("checkTag","").equals("")){
            dicheck = new ArrayList<>();
        }
        else{
            dicheck = new ArrayList<>(Arrays.asList(sp.getString("checkTag","").split(",")));
        }

//        }
        callAPI();
    }
    public void callAPI(){
        RequestQueue queue = Volley.newRequestQueue(this.ui.getAct());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                tagsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    memprosesKeluaranBerhasil(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ui.menampilkanError(error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+ui.getToken());
                return map;
            }
        };
        queue.add(stringRequest);
    }
    public void memprosesKeluaranBerhasil(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        for(int i=0;i<jsonArray.length();i++){
            hashMap.put(jsonArray.getJSONObject(i).getString("tag"),jsonArray.getJSONObject(i).getString("id"));
            this.ui.createCheckBox(jsonArray.getJSONObject(i).getString("tag"),dicheck.contains(jsonArray.getJSONObject(i).getString("tag")));
        }
       this.ui.createBTNApply();

    }
    public void onChangeClick(String text,boolean isChecked){
        if(isChecked){
            dicheck.add(text);
        }
        else{
            dicheck.remove(text);
        }
    }
    public void onApply(){
        SharedPreferences.Editor editor = sp.edit();
        String id="";
        for(int i=0;i<dicheck.size();i++){
            id+=hashMap.get(dicheck.get(i))+",";
        }
        if(id.length()!=0){
            editor.putString("checkTagId",id.substring(0,id.length()-1));
        }
        else{
            editor.putString("checkTagId","");
        }

        String tag = "";
        for(int i=0;i<dicheck.size();i++){
            tag+=dicheck.get(i)+",";
        }
        if(tag.length()!=0){
            editor.putString("checkTag",tag.substring(0,tag.length()-1));
        }
        else{
            editor.putString("checkTag","");
        }

        editor.apply();
    }
}
