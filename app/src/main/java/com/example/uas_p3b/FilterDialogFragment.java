package com.example.uas_p3b;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.adapter.FilterAdapter;
import com.example.contract.FilterUI;
import com.example.contract.PengumumanUI;
import com.example.model.Filter;
import com.example.presenter.FilterPresenter;
import com.example.uas_p3b.databinding.FilterBinding;

import java.util.List;

public class FilterDialogFragment extends DialogFragment implements FilterUI,View.OnClickListener {
    private FilterBinding binding;
    private PengumumanUI pengumumanUI;
    private FilterPresenter filterPresenter;
    private FilterAdapter adapter;

    public FilterDialogFragment(PengumumanUI pengumumanUI) {
        this.pengumumanUI = pengumumanUI;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FilterBinding.inflate(inflater,container,false);
        filterPresenter = new FilterPresenter(this);
        adapter= new FilterAdapter(this);
        binding.listview.setAdapter(adapter);
        binding.apply.setOnClickListener(this);

        return binding.getRoot();

    }

    @Override
    public Activity getAct() {

        return getActivity();

    }

    @Override
    public String getToken() {
        return pengumumanUI.getToken();
    }

    @Override
    public LayoutInflater getLayout() {
        return getLayoutInflater();
    }

//    @Override
//    public void createCheckBox(String tag, boolean check) {
//        CheckBox checkBox = new CheckBox(getActivity());
//        checkBox.setText(tag);
//        checkBox.setChecked(check);
//        //spy dibuat per cekbox beda code nya
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                filterPresenter.onChangeClick(buttonView.getText().toString(),isChecked);
//            }
//        });
//        binding.containerFilter.addView(checkBox);
//    }

//    @Override
//    public void createBTNApply() {
//        Button button = new Button(getActivity());
//        button.setText("APPLY");
//        button.setOnClickListener(this);
//        binding.containerFilter.addView(button);
//    }

    @Override
    public void menampilkanError(String error) {
        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateList(List<Filter> list) {
       adapter.update(list);
    }

    @Override
    public void onChangeClick(String tag, boolean isChecked) {
        filterPresenter.onChangeClick(tag,isChecked);
    }

    @Override
    public void onClick(View view) {
        filterPresenter.onApply();
        this.dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        pengumumanUI.reloadAdapter();
    }
}
