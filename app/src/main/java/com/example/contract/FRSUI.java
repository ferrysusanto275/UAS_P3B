package com.example.contract;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.List;

public interface FRSUI {
    public String getToken();
    public String getEmail();
    public void setActiveYear(int activeYear);
    public int getActiveYear();
    public Activity getAct();
    public LayoutInflater getLayout();
    public void menampilkanError(String error);
    public void updateList(List<Integer> list);
    public void changePage(Bundle bundle);
}
