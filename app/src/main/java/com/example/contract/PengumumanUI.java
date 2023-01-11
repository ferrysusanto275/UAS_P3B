package com.example.contract;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;

import com.example.model.Pengumuman;

import java.util.List;

public interface PengumumanUI {
    public LayoutInflater getLayout();
    public Activity getAct();
    public void setFilter(String Filter);
    public String getTitle();
    public String getToken();
    public String getRole();
    public void menampilkanError(String error);
    public void setVisibleBtnNext(boolean visible);
    public void setVisibleBtnTambah(boolean visible);
    public void loadingAdapter();
    public void noDataAdapter();
    //untuk mengupdate smua list yang ada
    public void updateList(List<Pengumuman> list);
    //buat manggil kom antara fragment
    public void listenerOnClick (String id);
    public void reloadAdapter();
}
