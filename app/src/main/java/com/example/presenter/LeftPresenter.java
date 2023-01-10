package com.example.presenter;

import com.example.contract.LeftUI;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LeftPresenter {
    private LeftUI leftUI;
    private List<String> list;

    public LeftPresenter(LeftUI leftUI) {
        this.leftUI = leftUI;
        this.list=new LinkedList<>();
        String[] arr={"Pengumuman","Pertemuan","FRS","Keluar"};
        this.loadData(arr);
    }
    public void changeListener(String page){
        this.leftUI.listenerOnClick(page);
    }
    public void loadData(String[] str){ //buat ngubah array jd list
        this.list.addAll(Arrays.asList(str));
        this.leftUI.updateList(list);
    }
}
