package com.example.presenter;

import android.os.Bundle;

import com.example.contract.HomeUI;

public class HomePresenter {
    private HomeUI ui;


    public HomePresenter(HomeUI ui) {
        this.ui = ui;
        //inisialisasi page pertama yang kebuka di home
        this.ui.changePage("Home",null);
    }
}
