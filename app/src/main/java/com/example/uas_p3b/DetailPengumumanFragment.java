package com.example.uas_p3b;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.contract.DetailPengumumanUI;
import com.example.contract.PengumumanUI;
import com.example.model.Pengumuman;
import com.example.presenter.DetailPengumumanPresenter;
import com.example.uas_p3b.databinding.DetailPengumumanBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailPengumumanFragment extends DialogFragment implements DetailPengumumanUI {
    private DetailPengumumanBinding binding;
    private DetailPengumumanPresenter presenter;
    private PengumumanUI ui;

    public DetailPengumumanFragment(PengumumanUI ui) {
        this.ui = ui;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = DetailPengumumanBinding.inflate(inflater,container,false);
       presenter = new DetailPengumumanPresenter(this);
        return binding.getRoot();

    }

    @Override
    public String getIdPengumuman() {
        return (getArguments().getString("id"));
    }

    @Override
    public Context getCtx() {
        //
        return getContext();
    }

    @Override
    public String getToken() {
        return ui.getToken();
    }

    @Override
    public void menampilkanError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void menampilkanData(Pengumuman pengumuman, String semuaTag, String tanggal, String jam) {
        binding.title.setText(pengumuman.getTitle());
        binding.content.setText(pengumuman.getContent());
        binding.tags.setText(semuaTag);
        binding.tanggalDibuat.setText(tanggal);
        binding.jamDibuat.setText(jam);
        //pertama ambil class author trs di kelas author ini ada id juga
        binding.author.setText(pengumuman.getAuthor().getAuthor());
    }
}
