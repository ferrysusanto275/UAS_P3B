package com.example.uas_p3b;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.contract.HomeUI;
import com.example.contract.SemesterFragmentUI;
import com.example.uas_p3b.databinding.LayoutSmtBinding;

import java.util.ArrayList;

public class SemesterFragment extends Fragment implements SemesterFragmentUI {
    private LayoutSmtBinding binding;
    //get token dan role
    private HomeUI homeUI;
    private int sem;

    public SemesterFragment(HomeUI homeUI) {
        this.homeUI = homeUI;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=LayoutSmtBinding.inflate(inflater,container,false);
        Bundle bundle = getArguments();
        binding.thnAjaran.setText(bundle.getString("heading"));
        sem = bundle.getInt("tahundansem");
        return binding.getRoot();
    }

    @Override
    public String getToken() {
        return homeUI.getToken();
    }

    @Override
    public int getSem() {
        return sem;
    }

    @Override
    public Context getCtx() {
        return getContext();
    }

    @Override
    public void menampilkanError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateAdapter(ArrayList<String> list) {
        binding.lstMatkul.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.simple_item,R.id.isi,list));
    }
}
