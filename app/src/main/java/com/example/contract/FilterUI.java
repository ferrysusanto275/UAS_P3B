package com.example.contract;

import android.app.Activity;
import android.view.LayoutInflater;

import com.example.model.Filter;

import java.util.List;

public interface FilterUI {
    public Activity getAct();
    public String getToken();
    public LayoutInflater getLayout();
//    public void  createCheckBox(String tag,boolean check);
//    public void createBTNApply();
    public void menampilkanError(String error);
    public void updateList(List<Filter> list);
    public void onChangeClick(String tag,boolean isChecked);
}
