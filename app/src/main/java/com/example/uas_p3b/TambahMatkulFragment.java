package com.example.uas_p3b;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uas_p3b.databinding.TambahMatkulBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TambahMatkulFragment extends Fragment {
    private TambahMatkulBinding tambahMatkulBinding;
    private int sem;
    private int offset;
    private HashMap<String,String> map;
    private ArrayList<String> matkul;
    private ArrayList<String> matkulygdiambil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tambahMatkulBinding = TambahMatkulBinding.inflate(inflater,container,false);
        View view = tambahMatkulBinding.getRoot();
        map = new HashMap<>();
        matkul = new ArrayList<>();
        matkulygdiambil = new ArrayList<>();
        Bundle bundle = getArguments();
        sem = bundle.getInt("tahundansem");
        tambahMatkulBinding.smt.setText(bundle.getString("heading"));
        offset=0;
        callAPI("https://ifportal.labftis.net/api/v1/enrolments/academic-years/"+sem,"matkulsudahdiambil");
        tambahMatkulBinding.list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                callAPI("https://ifportal.labftis.net/api/v1/courses/"+map.get(parent.getSelectedItem().toString())+"/prerequisites","cariprasyarat");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tambahMatkulBinding.ambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahMatkulBinding.ambil.setEnabled(false);
                callAPI("https://ifportal.labftis.net/api/v1/enrolments","enrol");
            }
        });
        return view;
    }
    public synchronized void  callAPI(String Base_URL,String ngapain){
        if(ngapain.equals("cariprasyarat")){
            tambahMatkulBinding.prasyarat.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.simple_item_spinner,R.id.isi,new String[]{"......"}));
        }
        int method=0;
        if(ngapain.equals("enrol")){
            method = Request.Method.POST;
        }
        else{
            method = Request.Method.GET;
        }
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(method,
                Base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(ngapain.equals("spinner")){
                        memprosesKeluaranBerhasil(response);
                    }
                    else if(ngapain.equals("matkulsudahdiambil")){
                        memprosesmatkulygsudahdiambil(response);
                    }
                    else if(ngapain.equals("cariprasyarat")){
                        memprosesprasyarat(response);
                    }
                    else if(ngapain.equals("enrol")){
                        matkul.clear();
                        matkulygdiambil.clear();
                        offset=0;
                        callAPI("https://ifportal.labftis.net/api/v1/enrolments/academic-years/"+sem,"matkulsudahdiambil");
                        Toast.makeText(getActivity(),"Berhasil melakukan enrol",Toast.LENGTH_LONG).show();
                        tambahMatkulBinding.ambil.setEnabled(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if(ngapain.equals("enrol")){
                        memrposeskeluarangagalEnrol(error);
                    }
                    else{
                        memprosesKeluaranGagal(error);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                if(ngapain.equals("cariprasyarat")){
                    //token admin
                    map.put("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiI2ZTY2ODZmMC0yOTZlLTRjNzItOGE0NS1hNmFjMWVkNDhlNDQiLCJyb2xlIjoiYWRtaW4ifSwiaWF0IjoxNjcyODM5MDc3fQ.H0fk8_L7x2giPEa8bylXJEreaTMdzksRwLGs7y7SgO4");
                }
                else{
                    //token student
                    map.put("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiJkNGNjOGZjMS02YmUyLTQ0MTEtOTJmNC01MDI3YTkyODEzNmMiLCJyb2xlIjoic3R1ZGVudCJ9LCJpYXQiOjE2NzI4Mzg5NzV9.-XiT0zGpiNgcKG8rdym4bL8_r7iC8Wfy4vNcYgeYAKk");
                }

                return map;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                String matkul = map.get(tambahMatkulBinding.list.getSelectedItem().toString());
                try {
                    jsonObject.put("course_id",matkul);
                    jsonObject.put("academic_year",sem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = jsonObject.toString();
                return json.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(stringRequest);
    }

    public void memprosesprasyarat(String response) throws JSONException {
        ArrayList<String> prasyarat = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for(int i=0;i<jsonArray.length();i++){
            String matkul = jsonArray.getJSONObject(i).getString("prerequisite_name");
            prasyarat.add(matkul);
        }
        if(prasyarat.size()==0){
            tambahMatkulBinding.prasyarat.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.simple_item_spinner,R.id.isi,new String[]{"Prasyarat tidak ada"}));
        }
        else{
            tambahMatkulBinding.prasyarat.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.simple_item,R.id.isi,prasyarat));
        }
        tambahMatkulBinding.ambil.setEnabled(true);
    }

    public void memprosesmatkulygsudahdiambil(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        for(int i=0;i<jsonArray.length();i++){
            String name =jsonArray.getJSONObject(i).getString("name");
            matkulygdiambil.add(name);
        }
        if(matkulygdiambil.size()==0){
            tambahMatkulBinding.matkulYgSudahDiambil.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.simple_item_spinner,R.id.isi,new String[]{"Belum ada matkul yang diambil"}));
        }
        else{
            tambahMatkulBinding.matkulYgSudahDiambil.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.simple_item,R.id.isi,matkulygdiambil));
        }
        callAPI("https://ifportal.labftis.net/api/v1/courses?limit=10","spinner");
    }

    public void memprosesKeluaranBerhasil(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        for(int i=0;i<jsonArray.length();i++){
            String name =jsonArray.getJSONObject(i).getString("name");
//                Toast.makeText(getActivity(),name,Toast.LENGTH_LONG).show();
            String id = jsonArray.getJSONObject(i).getString("id");
            map.put(name,id);
            matkul.add(name);
        }
        if(jsonArray.length()!=0){
            offset+=10;
            callAPI("https://ifportal.labftis.net/api/v1/courses?limit=10&offset="+offset,"spinner");
        }
        else{
            matkul.removeAll(matkulygdiambil);
            tambahMatkulBinding.list.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.simple_item_spinner,R.id.isi,matkul));
//            Toast.makeText(getActivity(),matkul.get(0),Toast.LENGTH_LONG).show();

        }
    }
    public void memprosesKeluaranGagal(VolleyError error) throws JSONException {
        if(error instanceof NoConnectionError){
            Toast.makeText(getActivity(),"Tidak ada koneksi internet",Toast.LENGTH_LONG).show();
        }else if(error instanceof TimeoutError){
            Toast.makeText(getActivity(),"Server memakan waktu lama untuk merespon\nCoba Lagi!",Toast.LENGTH_LONG).show();
        }
        else{
            String jsonKeluaran = new String(error.networkResponse.data);
            JSONObject jsonObject = new JSONObject(jsonKeluaran);
            String keluaran = jsonObject.get("errcode").toString();

            Toast.makeText(getActivity(),keluaran,Toast.LENGTH_LONG).show();
        }

    }
    public void memrposeskeluarangagalEnrol(VolleyError error) throws JSONException {
        String jsonKeluaran  = new String(error.networkResponse.data);
        JSONObject jsonObject = new JSONObject(jsonKeluaran);
        if(jsonObject.getString("errcode").equals("E_UNSATISFIED_PREREQUISITE")){
            JSONArray jsonArray = jsonObject.getJSONArray("reason");
            String matkul="" ;
            for(int i=0;i<jsonArray.length();i++){
                matkul+=jsonArray.getJSONObject(i).getString("name")+" ";
            }
            Toast.makeText(getActivity(),"Belum memenuhi prasyarat:\n"+matkul,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(),jsonObject.getString("errcode"),Toast.LENGTH_LONG).show();
        }
        tambahMatkulBinding.ambil.setEnabled(true);
    }
    class Matkul{
        String nama;
        String id;
        public Matkul(String nama,String id){
            this.nama = nama;
            this.id = id;
        }
    }
}
