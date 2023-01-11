package com.example.uas_p3b;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.contract.HomeUI;
import com.example.presenter.HomePresenter;
import com.example.presenter.MainPresenter;
import com.example.uas_p3b.databinding.ActivityMainBinding;
import com.example.uas_p3b.databinding.HomeBinding;
import com.example.uas_p3b.databinding.HomeFragmentBinding;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements HomeUI {
    private HomeBinding binding;
    private HomePresenter hp;
    private String token;
    private String email;
    private String role;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  HomeBinding.inflate(getLayoutInflater());
        Intent intent = getIntent();
        //ambil token dari login
        this.token = intent.getStringExtra("token");
        //ambil email
        this.email = intent.getStringExtra("email");
        //ambil role
        this.role = intent.getStringExtra("role");


        setContentView(binding.getRoot());
        hp = new HomePresenter(this);

        this.getSupportFragmentManager().setFragmentResultListener(
                "changePage", this,new FragmentResultListener(){
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result){
                        String page = result.getString("page");
                        if(page=="detailSemester"){
                            Bundle bundle = new Bundle();
                            bundle.putInt("tahundansem",result.getInt("tahundansem"));
                            bundle.putString("heading",result.getString("heading"));
                            changePage(page,bundle);
                        }
                        else{
                            changePage(page,null);
                        }
                        binding.drawerLayout.closeDrawers();
                    }


                }
        );
        this.getSupportFragmentManager().setFragmentResultListener(
                "closeApp", this,new FragmentResultListener(){
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result){
                        closeApp();
                    }


                }
        );
    }

    @Override
    public void changePage(String page,Bundle bundle) {
        FragmentTransaction ft=this.getSupportFragmentManager().beginTransaction();
        switch(page.toLowerCase()){
            case "home":
                ft.replace(binding.fragmentContainer.getId(), HomeFragment.newInstance(this));
                ft.addToBackStack(null);
                break;
            case "pengumuman":
                ft.replace(binding.fragmentContainer.getId(), PengumumanFragment.newInstance(token));
                ft.addToBackStack(null);
                break;
            case "tambah_pengumuman":
                ft.replace(binding.fragmentContainer.getId(), TambahPengumumanFragment.newInstance(token));
                ft.addToBackStack(null);
                break;
            case "frs":
                if(this.role.equals("student")){
                    ft.replace(binding.fragmentContainer.getId(), FRSFragment.newInstance(this));
                    ft.addToBackStack(null);
                    break;
                }else{
                    Toast.makeText(this, "Bukan Mahasiswa", Toast.LENGTH_SHORT).show();
                }


        }

        ft.commit();
    }

    @Override
    public void closeApp() {
        this.moveTaskToBack(true);
        this.finish();
    }

    @Override
    public String getToken() {
        return this.token;
    }


    @Override
    public String getRole() {
        return this.role;
    }

    @Override
    public String getEmail() {
        return this.email;
    }
}
