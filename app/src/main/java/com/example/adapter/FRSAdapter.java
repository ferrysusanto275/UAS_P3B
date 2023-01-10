package com.example.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.fragment.app.FragmentActivity;

import com.example.contract.FRSUI;
import com.example.model.Pengumuman;
import com.example.uas_p3b.databinding.ListFrsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FRSAdapter extends BaseAdapter {
    private ListFrsBinding binding;
    private FRSUI ui;
    private List<Integer> tahundansemester;


    public FRSAdapter(FRSUI ui){
        this.ui = ui;
        this.tahundansemester = new ArrayList<>();
    }
    public void update(List<Integer> list){
        this.tahundansemester=list;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return tahundansemester.size();
    }

    @Override
    public Integer getItem(int position) {
        return tahundansemester.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){

            binding = ListFrsBinding.inflate(ui.getLayout());
            vh=new ViewHolder(binding,ui);
            convertView=binding.getRoot();
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder) convertView.getTag();
        }
        vh.updateView(getItem(position));


        return convertView;
    }
    public class ViewHolder implements View.OnClickListener{
        private ListFrsBinding binding;
        private FRSUI ui;
        private HashMap<Integer,String> map;
        private int tahundansemester;

        public ViewHolder(ListFrsBinding binding, FRSUI ui) {
            this.binding = binding;
            this.ui = ui;
            map = new HashMap<>();
            map.put(1,"Semester Ganjil");
            map.put(2,"Semester Genap");
            map.put(3,"Semester Pendek");
        }
        public void updateView(int tahundansemester){
            this.tahundansemester=tahundansemester;
            int tahun = tahundansemester/10;
            String semester = map.get(tahundansemester%10);
            binding.smt.setText(semester+" "+tahun+"/"+(tahun+1));

        }

        @Override
        public void onClick(View view) {
            if(view==binding.containerSemester){
                int tahun = tahundansemester/10;
                String semester = map.get(tahundansemester%10);
                Bundle result = new Bundle();
                if(tahundansemester==ui.getActiveYear()) {
                    result.putString("page","tambahMatkul");
                }else{
                    result.putString("page","detailSemester");
                }
                result.putInt("tahundansem",tahundansemester);
                result.putString("heading",semester+" "+tahun+"/"+(tahun+1));
                ui.changePage(result);
            }

        }
    }
}
