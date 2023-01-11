package com.example.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.contract.FilterUI;
import com.example.model.Config;
import com.example.model.Filter;
import com.example.model.Tags;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FilterPresenter {
    private List<String> dicheck;
    private SharedPreferences sp;
    private HashMap<String,String>hashMap;
    private List<Filter> list;
    private List<Tags> tagsList;
    private Gson gson;
    private final static String tagsURL = Config.BASE_URL + "tags";
    private FilterUI ui;

    public FilterPresenter(FilterUI ui) {
        this.ui = ui;
        this.sp = ui.getAct().getPreferences(Context.MODE_PRIVATE);
        this.list=new LinkedList<>();
        this.tagsList=new ArrayList<>();
        this.gson=new Gson();
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
        if(sp.getString("checkTag","").equals("")){
            dicheck = new ArrayList<>();
        }
        else{
            dicheck = new ArrayList<>(Arrays.asList(sp.getString("checkTag","").split(",")));
        }
        this.tagsList=this.gson.fromJson(response,new TypeToken<ArrayList<Tags>>(){}.getType());
        for (Tags tag :this.tagsList) {
            list.add(new Filter(tag,dicheck.contains(tag.getTag())));
        }
        ui.updateList(list);

//        for(int i=0;i<jsonArray.length();i++){
//            hashMap.put(jsonArray.getJSONObject(i).getString("tag"),jsonArray.getJSONObject(i).getString("id"));
////            this.ui.createCheckBox(jsonArray.getJSONObject(i).getString("tag"),dicheck.contains(jsonArray.getJSONObject(i).getString("tag")));
//        }
//       this.ui.createBTNApply();

    }
    public void onChangeClick(String text,boolean isChecked){
        if(isChecked){
            if(!dicheck.contains(text)){
                dicheck.add(text);
            }
        }else{
            Log.d("onChangeClick: ","masuk");
            dicheck.remove(text);
        }
        Log.d( "onChangeClick: ",this.gson.toJson(dicheck));
    }
    public void onApply(){
        String id="";
        String stringTag = "";
//        for(int i=0;i<dicheck.size();i++){
        for (Tags tag:this.tagsList) {
            if(dicheck.contains(tag.getTag())){
                id+=tag.getId()+",";
                stringTag+=tag.getTag()+",";
            }
        }
//        }
        SharedPreferences.Editor editor = sp.edit();
        if(id.length()!=0){
            editor.putString("checkTagId",id.substring(0,id.length()-1));
        }
        else{
            editor.putString("checkTagId","");
        }

        if(stringTag.length()!=0){
            editor.putString("checkTag",stringTag.substring(0,stringTag.length()-1));
        }
        else{
            editor.putString("checkTag","");
        }

        editor.apply();
    }
}
