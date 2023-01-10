package com.example.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.lights.LightState;
import android.util.Log;
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
import com.example.contract.PengumumanUI;
import com.example.model.Config;
import com.example.model.Pengumuman;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PengumumanPresenter {
    private PengumumanUI ui;
    private List<Pengumuman>list;
    private List<String> checkTag,checkTagId;
    private final static String announcementsURL = Config.BASE_URL + "announcements";
    private SharedPreferences sp;
    private Gson gson;
    private String next;

    public PengumumanPresenter(PengumumanUI ui) {
        this.ui = ui;
        this.gson=new Gson();
        this.list= new LinkedList<>();
        this.checkTag = new LinkedList<>();
        this.checkTagId = new LinkedList<>();
        this.sp = this.ui.getAct().getPreferences(Context.MODE_PRIVATE);
        this.ambilTags();
        this.callAPI(false);
    }
    //kl pk iscursor = tru, dia ambil next page
    //kl false, dia ambil dr awal dgn filter"nya
    public void callAPI(boolean isCursor){
        String Base_URL=announcementsURL;
        if(!isCursor){
            Base_URL+="?filter[title]="+this.ui.getTitle();
            if(checkTagId.size()!=0){
                for(int i=0;i<checkTagId.size();i++){
                    Base_URL+="&filter[tags][]="+checkTagId.get(i);
                }
            }
        }else{
            Base_URL+="?cursor="+next;
        }

//        Log.d("base_url", Base_URL);
//        Toast.makeText(getActivity(),Base_URL,Toast.LENGTH_LONG).show();
        this.ui.loadingAdapter();
        RequestQueue queue = Volley.newRequestQueue(this.ui.getLayout().getContext());
        //request base url hasilnya onResponse/errorResponse
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Base_URL, new Response.Listener<String>() {
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
            //si base url butuh auth, ini metod buat auth
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                //ambil token dari kelas pengumumanfrag/ hasil login
                map.put("Authorization","Bearer "+ui.getToken());
                return map;
            }
        };
        queue.add(stringRequest);
    }
    public void memprosesKeluaranBerhasil(String response) throws JSONException {
        //metod ini perlu this.ui buat ngejalanin logika yg sd d buat (setvisible,dll)
        JSONObject jsonObject = new JSONObject(response);
        //hasil dari respon ada "metadata" di dalemnya dan di dlm meta data ada next
        Object object = jsonObject.getJSONObject("metadata").get("next");
        //kalau next tidak ketemu, visible = false
        this.ui.setVisibleBtnNext(!object.equals(null));
        if(!object.equals(null)){
            next = object.toString();
        }
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        list = gson.fromJson(jsonArray.toString(),new TypeToken<ArrayList<Pengumuman>>(){}.getType());
        if(list.size()>0){
            this.ui.updateList(list);
        }else{
            this.ui.noDataAdapter();
        }
    }
    public void memprosesKeluaranGagal(VolleyError error) throws JSONException{
        String res="";
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

    public void ambilTags(){
//        if(sp.contains("checkTag")){
        //sp nge-dapetin dari data local hp (shared pref)
        if(sp.getString("checkTag","").equals("")){
            checkTag.clear();
            this.ui.setFilter("Filter : None");
        }
        else{
            checkTag = new LinkedList<>(Arrays.asList(sp.getString("checkTag","").split(",")));
            this.ui.setFilter("Filter:"+sp.getString("checkTag",""));
        }
//        }
//        if(sp.contains("checkTagId")){
        if(sp.getString("checkTagId","").equals("")){
            checkTagId.clear();
        }else{
            checkTagId = new LinkedList<>(Arrays.asList(sp.getString("checkTagId","").split(",")));
        }

//        }
    }
    public void refresh(){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        ambilTags();
        this.callAPI(false);
    }
}
