package com.example.contract;

import android.content.Context;

import com.example.model.Pengumuman;

import java.net.ContentHandler;

public interface DetailPengumumanUI {
    public String getIdPengumuman();
    public Context getCtx();
    public String getToken();
    public void menampilkanError(String error);
    public void menampilkanData(Pengumuman pengumuman,String semuaTag,String tanggal,String jam);

}
