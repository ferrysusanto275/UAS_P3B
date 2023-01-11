package com.example.presenter;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.contract.SemesterFragmentUI;
import com.example.model.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SemesterFragmentPresenter {
    private SemesterFragmentUI ui;
    private final static String academicYearsURL = Config.BASE_URL + "academic-years";

    public SemesterFragmentPresenter(SemesterFragmentUI ui) {
        this.ui = ui;

    }
    public void callAPI(){
        String Baseurl=academicYearsURL+"/"+ui.getSem();
        RequestQueue queue = Volley.newRequestQueue(this.ui.getCtx());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Baseurl, new Response.Listener<String>() {
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
                try {
                    memprosesKeluaranGagal(error);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        ArrayList<String> matkul = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for(int i=0;i<jsonArray.length();i++){
            String name =jsonArray.getJSONObject(i).getString("name");
            matkul.add(name);
        }
        if(matkul.size()==0){
            matkul.add("Hasil tidak ditemukan!");
        }
        this.ui.updateAdapter(matkul);
    }
    public void memprosesKeluaranGagal(VolleyError error) throws JSONException {
        String res="";
        if(error instanceof NoConnectionError){
            res="Tidak ada koneksi internet";
        }else if(error instanceof TimeoutError){
            res="Server memakan waktu lama untuk merespon\nCoba Lagi!";
        }
        else{
            String jsonKeluaran = new String(error.networkResponse.data);
            JSONObject jsonObject = new JSONObject(jsonKeluaran);
            String keluaran = jsonObject.get("errcode").toString();
            res=keluaran;
        }
        this.ui.menampilkanError(res);

    }

}
