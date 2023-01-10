package com.example.contract;

import android.view.LayoutInflater;

import java.util.List;

public interface LeftUI {
    //untuk mengupdate smua list yang ada
    public void updateList(List<String> list);
    //buat manggil kom antara fragment
    public void listenerOnClick (String page);
    //supaya rapih doang inflatenya disini
    public LayoutInflater getLayout();
}
