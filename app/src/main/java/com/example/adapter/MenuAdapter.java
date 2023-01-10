package com.example.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.contract.LeftUI;
import com.example.uas_p3b.databinding.ListMenuBinding;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends BaseAdapter {
    private List<String> list;
    private LeftUI ui;
    private ListMenuBinding binding;

    public MenuAdapter(LeftUI ui) {
        this.ui = ui;
        list = new ArrayList<>();
    }
    public void update(List<String> list){
        this.list=list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if(view==null){
            binding = ListMenuBinding.inflate(this.ui.getLayout());
            view=binding.getRoot();
            vh=new ViewHolder(this.ui,binding);
            view.setTag(vh);
        }else{
            vh=(ViewHolder) view.getTag();
        }
        vh.updateView(getItem(i));
        return view;
    }
    private class ViewHolder implements View.OnClickListener{
        private LeftUI ui;
        private ListMenuBinding binding;

        public ViewHolder(LeftUI ui, ListMenuBinding binding) {
            this.ui = ui;
            this.binding = binding;
        }
        public void updateView(String str){
            this.binding.tv.setText(str);
            this.binding.tv.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            this.ui.listenerOnClick(this.binding.tv.getText().toString());

        }
    }
}
