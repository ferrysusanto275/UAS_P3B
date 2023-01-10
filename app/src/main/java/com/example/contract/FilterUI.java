package com.example.contract;

import android.app.Activity;

public interface FilterUI {
    public Activity getAct();
    public String getToken();
    public void  createCheckBox(String tag,boolean check);
    public void createBTNApply();
    public void menampilkanError(String error);
}
