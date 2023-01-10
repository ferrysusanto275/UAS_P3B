package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.contract.PengumumanUI;
import com.example.model.Pengumuman;
import com.example.uas_p3b.databinding.ItemListPengumumanBinding;
import com.example.uas_p3b.databinding.ListMenuBinding;

import java.util.ArrayList;
import java.util.List;
//buat nampilin list pengumuman per 5 biji
public class PengumumanAdapter extends BaseAdapter {
    private ItemListPengumumanBinding binding;
    private List<Pengumuman> daftarPengumuman;
    private PengumumanUI ui;
    private LayoutInflater layoutInflater;

    public PengumumanAdapter(PengumumanUI ui) {
        this.ui = ui;
        daftarPengumuman = new ArrayList<Pengumuman>();
        this.layoutInflater = this.ui.getLayout();
    }
    public void update(List<Pengumuman> list){
        this.daftarPengumuman=list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.daftarPengumuman.size();
    }

    @Override
    public Pengumuman getItem(int i) {
        return this.daftarPengumuman.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if(view == null){
            binding = ItemListPengumumanBinding.inflate(this.ui.getLayout());
            view=binding.getRoot();
            vh= new ViewHolder(this.binding,ui);
            view.setTag(vh);
        }else{
            vh=(ViewHolder) view.getTag();
        }
        vh.updateView(getItem(i),i);
        return view;
        }

    private class ViewHolder implements View.OnClickListener{
        private ItemListPengumumanBinding binding;
        private PengumumanUI ui;
        private Pengumuman pengumuman;


        public ViewHolder(ItemListPengumumanBinding binding, PengumumanUI ui) {
            this.binding = binding;
            this.ui = ui;
        }
        public void updateView(Pengumuman pengumuman, int pos){
            this.pengumuman=pengumuman;
            binding.TampilJudul.setText(pengumuman.getTitle());
            String tags = "";
            for(int i=0;i<pengumuman.getTags().size();i++){
                tags+=pengumuman.getTags().get(i).getTag()+",";
            }
            tags = tags.substring(0,tags.length()-1);
            binding.TampilTags.setText(tags);
            binding.pengumuman.setOnClickListener(this);
        }
        //butuh inisialisasi d view, buat nampilin ke detail
        @Override
        public void onClick(View view) {
            if(view == binding.pengumuman){
                this.ui.listenerOnClick(pengumuman.getId());
            }
        }
    }
}
