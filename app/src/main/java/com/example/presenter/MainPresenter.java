package com.example.presenter;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.contract.MainUI;
import com.example.model.Config;
import com.example.model.RespAuth;
import com.example.model.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MainPresenter  {
    private MainUI ui;
    private Context context;
    private final static String  authURL = Config.BASE_URL + "authenticate";
    private Gson gson;

    public MainPresenter(MainUI ui) {
        this.ui = ui;
        this.context = ui.getContext();
        this.gson = new Gson();
    }

    public void Login(String email, String password, String role){
        this.ui.disabledInput();
        User loginUser = new User(email,password,role);
        this.prosesLoginAPI(this.gson.toJson(loginUser));

    }
    private void prosesLoginAPI(String json){
        RequestQueue queue = Volley.newRequestQueue(this.context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,this.authURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        memprosesKeluaranBerhasil(response);
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
                    public byte[] getBody() throws AuthFailureError {
                        return json.getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
        queue.add(stringRequest);
    }
    private void memprosesKeluaranBerhasil(String respose){
        //ngubah respon dari API jadiin kelas RespAuth
        RespAuth ra= gson.fromJson(respose,RespAuth.class);
        //harus pakein ui soalnya nge intent nya di main activity
        this.ui.loginBerhasil(ra.getToken());
    }
    private void memprosesKeluaranGagal(VolleyError error) throws JSONException{
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
            res = "Gagal Login";
            if(keluaran.equals("E_AUTH_FAILED")){
                res = "Email atau Password atau Role anda salah";
            }
        }
        this.ui.enabledInput();
        this.ui.loginGagal(res);
    }
}

