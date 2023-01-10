package com.example.presenter;

import android.app.DownloadManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.contract.DetailPengumumanUI;
import com.example.model.Config;
import com.example.model.Pengumuman;
import com.example.model.Tags;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailPengumumanPresenter {
    private DetailPengumumanUI ui;
    private static String announcemnetURL = Config.BASE_URL + "announcements";
    private Gson gson;
    public DetailPengumumanPresenter(DetailPengumumanUI ui) {
        this.ui = ui;
        this.gson = new Gson();
        this.callAPI();

    }
    public void callAPI(){
        RequestQueue queue = Volley.newRequestQueue(ui.getCtx());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                announcemnetURL+"/"+ui.getIdPengumuman(), new Response.Listener<String>() {
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
                memprosesKeluaranGagal(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+ ui.getToken());
                return map;
            }
        };
        queue.add(stringRequest);
    }
    public void memprosesKeluaranBerhasil(String response)throws JSONException{
       // Log.d( "memprosesKeluaranBerhasil: ",response);
        //ga pake json objek mempermudah masukin ke class pengumuman
        Pengumuman pengumuman = gson.fromJson(response,Pengumuman.class);
        String semuaTags = "";
        for(int i=0;i<pengumuman.getTags().size();i++){
            semuaTags+=pengumuman.getTags().get(i).getTag()+",";
        }
        semuaTags = semuaTags.substring(0,semuaTags.length()-1);
        String dateTime = pengumuman.getCreated_at();
        String tanggal = dateTime.substring(0,10);
        String jam = dateTime.substring(11,19);
        this.ui.menampilkanData(pengumuman,semuaTags,tanggal,jam);

    }
    public void memprosesKeluaranGagal(VolleyError error){
        this.ui.menampilkanError(error.toString());
    }
}
