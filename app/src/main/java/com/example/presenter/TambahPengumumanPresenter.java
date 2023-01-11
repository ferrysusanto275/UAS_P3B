package com.example.presenter;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.contract.TambahPengumumanUI;
import com.example.model.Config;
import com.example.model.InputPengumuman;
import com.example.model.InputTag;
import com.example.model.Pengumuman;
import com.example.model.Tags;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TambahPengumumanPresenter {
    private TambahPengumumanUI ui;
    private final static String announcementsURL = Config.BASE_URL + "announcements";
    private final static String tagsURL = Config.BASE_URL + "tags";
    private Gson gson;
    private List<Tags> tagsList;
    private List<String> tagInput,tagsInput;
    private InputPengumuman inputPengumuman;


    public TambahPengumumanPresenter(TambahPengumumanUI ui) {
        this.ui = ui;
        this.gson=new Gson();
        this.tagsList=new ArrayList<>();
    }
    public void simpanPengumuman(String title,String content,String tags){
        this.tagInput=new ArrayList<>();
        //pisah semua tag dengan ,
        String[] arrTag=tags.split(",");
        this.tagsInput=new ArrayList<>();
        for (String tag :arrTag) {
            this.tagsInput.add(tag);
        }
        this.inputPengumuman= new InputPengumuman(title,content,tagInput);
        //melakukan ambil tag untuk cek data tag sudah ada atu belum
        ambilTags();
    }
    private boolean cekTag(String tag){
        boolean flag =false;
        for (Tags tg :this.tagsList) {
            if(tg.getTag().equals(tag)){
                this.tagInput.add(tg.getId());
                flag=true;
                break;
            }
        }
        return flag;
    }
    private void inputAllTag(){
        //selama ada tag yang di input
        if(this.tagsInput.size()>0){
            String tagInput=this.tagsInput.remove(0);
            //cari tag
            if(cekTag(tagInput)){
                //kalau sudh ada input id ke tags di pengumuman
                inputAllTag();
            }else{
                //kalau ga ketemu input
                postAPI("inputTag",gson.toJson(new InputTag(tagInput)));
            }
        }else{
            //jika semua tag sudah di input set balik tag
            this.inputPengumuman.setTags(this.tagInput);
            //input ke pengumuman
            postAPI("inputPengumuman",gson.toJson(this.inputPengumuman));
        }
    }
    private  void getApi(){
        RequestQueue requestQueue = Volley.newRequestQueue(ui.getCtx());
    }
    private  void postAPI(String ngapain,String json){
        try {
            JSONObject obj= new JSONObject(json);
            String BASE_URL=tagsURL;
            if(ngapain.equals("inputPengumuman"))BASE_URL=announcementsURL;
            RequestQueue requestQueue = Volley.newRequestQueue(ui.getCtx());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, obj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (ngapain.equals("inputPengumuman")) {
                            memprosesInputPengumuman(response.toString());
                        } else {
                            memprosesInputTag(response.toString());
                        }
                    }catch (JSONException e){
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
                    {
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", "Bearer " + ui.getToken());
                        return map;
                    }
                }
            };
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void ambilTags(){
        RequestQueue queue = Volley.newRequestQueue(this.ui.getCtx());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                tagsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d( "onResponse: ",response);
                try {
                    memprosesKeluaranTags(response);
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
//                Log.d( "token: ",ui.getToken());
                return map;
            }
        };
        queue.add(stringRequest);
    }
    private void memprosesInputPengumuman(String response)throws JSONException{
        Pengumuman hasil=this.gson.fromJson(response,Pengumuman.class);
        this.ui.berhasil();
    }
    private void memprosesInputTag(String response)throws JSONException{
        Log.d( "memprosesInputTag: ",response);
        Tags hasil=this.gson.fromJson(response,Tags.class);
        this.tagInput.add(hasil.getId());
        inputAllTag();
    }
    private void memprosesKeluaranTags(String response) throws JSONException{
        this.tagsList=this.gson.fromJson(response,new TypeToken<ArrayList<Tags>>(){}.getType());
        //cekTag dan input jika blm ada
        inputAllTag();
    }
}
