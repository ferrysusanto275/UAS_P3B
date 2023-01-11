package com.example.uas_p3b;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.contract.TambahPengumumanUI;
import com.example.presenter.TambahPengetahuanPresenter;
import com.example.uas_p3b.databinding.TambahPengumumanBinding;

public class TambahPengumumanFragment extends Fragment implements TambahPengumumanUI,View.OnClickListener {
    private TambahPengumumanBinding binding;
    private String token;
    private TambahPengetahuanPresenter presenter;

    public TambahPengumumanFragment(String token) {
        this.token = token;
    }
    public static TambahPengumumanFragment newInstance(String token){
        return new TambahPengumumanFragment(token);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=TambahPengumumanBinding.inflate(inflater,container,false);
        presenter= new TambahPengetahuanPresenter(this);
        binding.btnSimpan.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public Context getCtx() {
        return getContext();
    }

    @Override
    public void menampilkanError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void berhasil() {
        Toast.makeText(getContext(), "Berhasil Menambah Pengumuman", Toast.LENGTH_SHORT).show();
        Bundle result = new Bundle();
        result.putString("page","pengumuman");
        getParentFragmentManager().setFragmentResult("changePage",result);
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public void onClick(View view) {
        if(view==binding.btnSimpan){
        presenter.simpanPengumuman(binding.etTitle.getText().toString(),binding.etContent.getText().toString(),binding.etTags.getText().toString());
        }
    }
}
