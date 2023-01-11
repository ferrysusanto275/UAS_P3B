package com.example.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import com.example.contract.FilterUI;
import com.example.contract.LeftUI;
import com.example.model.Filter;
import com.example.uas_p3b.databinding.ItemListFilterBinding;
import com.example.uas_p3b.databinding.ListMenuBinding;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends BaseAdapter {
    private ItemListFilterBinding binding;
    private FilterUI ui;
    private List<Filter> list;
    public FilterAdapter(FilterUI ui) {
        this.ui = ui;
        list = new ArrayList<>();
    }
    public void update(List<Filter> list){
        this.list=list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Filter getItem(int i) {
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
            binding = ItemListFilterBinding.inflate(this.ui.getLayout());
            view=binding.getRoot();
            vh=new ViewHolder(this.ui,binding);
            view.setTag(vh);
        }else{
            vh=(ViewHolder) view.getTag();
        }
        vh.updateView(getItem(i));
        return view;
    }
    private class ViewHolder implements CompoundButton.OnCheckedChangeListener{
        private FilterUI ui;
        private ItemListFilterBinding binding;

        public ViewHolder(FilterUI ui, ItemListFilterBinding binding) {
            this.ui = ui;
            this.binding = binding;
        }
        public void updateView(Filter filter){
            this.binding.cb.setText(filter.getTag().getTag());
            this.binding.cb.setChecked(filter.isSelected());
            this.binding.cb.setOnCheckedChangeListener(this);
        }


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            ui.onChangeClick(this.binding.cb.getText().toString(),b);
        }
    }
}
