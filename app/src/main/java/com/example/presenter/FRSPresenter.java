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
import com.example.adapter.FRSAdapter;
import com.example.contract.FRSUI;
import com.example.model.Config;
import com.example.uas_p3b.MainActivity;
import com.example.uas_p3b.R;
import com.example.uas_p3b.databinding.LayoutFrsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FRSPresenter {
    private FRSUI ui;
    private FRSAdapter adapterFRS;
    //initial dapet waktu login
    private String email;
    private int initial_year;
    private int active_year;
    private ArrayList<Integer> semester;
    private String announcementsURL;
    private final static String acdemicYearURL=Config.BASE_URL+"academic-years";

    public FRSPresenter (FRSUI ui){
        this.ui = ui;
        this.semester = new ArrayList<>();
        this.email = ui.getEmail();
        this.announcementsURL = Config.BASE_URL+"students/email/" + this.email;
        callAPI(this.announcementsURL,"cariInitialYear");

    }
    public void callAPI(String BASE_URL,String ngapain){

//        frsBinding.lstFrs.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_item_spinner,R.id.isi,new String[]{"Harap Tunggu..."}));
        RequestQueue queue = Volley.newRequestQueue(this.ui.getAct());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(ngapain.equals("cariTahun")){
                        memprosesKeluaranBerhasil(response);
                    }
                    else{
                        memprosesInitialYear(response);
                    }
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
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("academic_years");
        this.active_year = jsonObject.getInt("active_year");
        for(int i=0;i<jsonArray.length();i++){
            int masukan = jsonArray.getInt(i);
            int tahun = masukan/10;
            if(tahun<=(this.initial_year/10)+7 && tahun>=(this.initial_year/10)){
                semester.add(masukan);
            }
        }
        ui.setActiveYear(active_year);
        ui.updateList(semester);
//        adapterFRS = new FRSAdapter(this);
//        frsBinding.lstFrs.setAdapter(adapterFRS);
    }
    public void memprosesKeluaranGagal(VolleyError error) throws JSONException {
        String res = "";
        if(error instanceof NoConnectionError){
            res="Tidak ada koneksi internet";
        }else if(error instanceof TimeoutError){
            res="Server memakan waktu lama untuk merespon\nCoba Lagi!";
        }
        else{
            String jsonKeluaran = new String(error.networkResponse.data);
            JSONObject jsonObject = new JSONObject(jsonKeluaran);
            res = jsonObject.get("errcode").toString();
        }
        this.ui.menampilkanError(res);
    }
    public void memprosesInitialYear(String response) throws JSONException {
        initial_year = new JSONObject(response).getInt("initial_year");
        callAPI(this.acdemicYearURL,"cariTahun");
    }
}
