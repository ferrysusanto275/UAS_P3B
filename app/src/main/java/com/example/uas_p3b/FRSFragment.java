package com.example.uas_p3b;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.adapter.FRSAdapter;
import com.example.contract.FRSUI;
import com.example.contract.HomeUI;
import com.example.presenter.FRSPresenter;
import com.example.uas_p3b.databinding.LayoutFrsBinding;

import java.util.List;
public class FRSFragment extends Fragment implements FRSUI, View.OnClickListener{
    private LayoutFrsBinding frsBinding;
    private HomeUI homeUI;
    private FRSAdapter adapter;
    private FRSPresenter presenter;
    private int activeYear;

    public FRSFragment(HomeUI homeUI) {
        this.homeUI = homeUI;
    }

    public static FRSFragment newInstance(HomeUI homeUI){
        return new FRSFragment(homeUI);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        frsBinding = LayoutFrsBinding.inflate(inflater,container,false);
        View view = frsBinding.getRoot();
        presenter = new FRSPresenter(this);
        adapter = new FRSAdapter(this);
        frsBinding.lstFrs.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public String getToken() {
        return homeUI.getToken();
    }

    @Override
    public String getEmail() {
        return homeUI.getEmail();
    }

    @Override
    public void setActiveYear(int activeYear) {
        this.activeYear=activeYear;
    }

    @Override
    public int getActiveYear() {
        return activeYear;
    }

    @Override
    public Activity getAct() {
        return getActivity();
    }

    @Override
    public LayoutInflater getLayout() {
        return getLayoutInflater();
    }

    @Override
    public void menampilkanError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateList(List<Integer> list) {
        adapter.update(list);
    }

    @Override
    public void changePage(Bundle bundle) {
        getParentFragmentManager().setFragmentResult("changePage",bundle);
    }
}
