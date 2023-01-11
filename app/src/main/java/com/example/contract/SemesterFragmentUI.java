package com.example.contract;

import android.content.Context;

import java.util.ArrayList;

public interface SemesterFragmentUI {
    public String getToken();
    public int getSem();
    public Context getCtx();
    public void menampilkanError(String error);
    public void updateAdapter(ArrayList<String> list);
}
