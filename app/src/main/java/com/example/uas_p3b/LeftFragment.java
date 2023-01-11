package com.example.uas_p3b;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.adapter.MenuAdapter;
import com.example.contract.LeftUI;
import com.example.presenter.LeftPresenter;
import com.example.uas_p3b.databinding.LeftFragmentBinding;

import java.util.List;

public class LeftFragment extends Fragment implements LeftUI {
    private LeftFragmentBinding binding;
    private LeftPresenter presenter;
    private MenuAdapter menuAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=LeftFragmentBinding.inflate(inflater,container,false);
        menuAdapter = new MenuAdapter(this);
        presenter = new LeftPresenter(this);
        binding.listLeft.setAdapter(menuAdapter);

        return binding.getRoot();
    }

    @Override
    public void updateList(List<String> list) {
        menuAdapter.update(list);
    }

    @Override
    public void listenerOnClick(String page) {
        switch (page){
            case "Keluar": this.getParentFragmentManager().setFragmentResult("closeApp",new Bundle());
                break;
            default:
                Bundle res = new Bundle();
                res.putString("page",page);
                this.getParentFragmentManager().setFragmentResult("changePage",res);}
    }

    @Override
    public LayoutInflater getLayout() {
        return getLayoutInflater();
    }

}
