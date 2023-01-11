package com.example.contract;

import android.content.Context;

public interface TambahPengumumanUI {
    public Context getCtx();
    public void menampilkanError(String error);
    public void berhasil();
    public String getToken();
}
