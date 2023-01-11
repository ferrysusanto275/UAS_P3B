package com.example.uas_p3b;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.adapter.PengumumanAdapter;
import com.example.contract.HomeUI;
import com.example.contract.PengumumanUI;
import com.example.model.Pengumuman;
import com.example.presenter.PengumumanPresenter;
import com.example.uas_p3b.databinding.PengumumanBinding;

import java.util.List;

public class PengumumanFragment extends Fragment implements PengumumanUI, View.OnClickListener {
    private PengumumanBinding binding;
    private PengumumanAdapter adapter;
    private PengumumanPresenter presenter;
    //get token dan role
    private HomeUI homeUI;

    public PengumumanFragment(HomeUI homeUI) {
        this.homeUI = homeUI;
    }

    public static PengumumanFragment newInstance(HomeUI homeUI){
        return new PengumumanFragment(homeUI);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PengumumanBinding.inflate(inflater,container,false);
        adapter = new PengumumanAdapter(this);
        presenter = new PengumumanPresenter(this);
        binding.listview.setAdapter(adapter);
        binding.refresh.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        binding.filter.setOnClickListener(this);
        binding.btnTambah.setOnClickListener(this);
        binding.searchTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.callAPI(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();

    }

    @Override
    public void onClick(View view) {
        if(view==binding.refresh){
            presenter.refresh();
            binding.searchTitle.setText("");
        }else if(view == binding.next){
            presenter.callAPI(true);
        }else if (view == binding.filter){
            FilterDialogFragment filter = new FilterDialogFragment(this);
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            filter.show(ft,"filter");
        }else if(view==binding.btnTambah){
            //pindah ke tambah pengumuman
            Bundle result = new Bundle();
            result.putString("page","tambah_pengumuman");
            getParentFragmentManager().setFragmentResult("changePage",result);
        }
    }

    @Override
    public LayoutInflater getLayout() {
        return getLayoutInflater();
    }

    @Override
    public Activity getAct() {
        return getActivity();
    }

    @Override
    public void setFilter(String Filter) {
        binding.filterApply.setText(Filter);
    }

    @Override
    public String getTitle() {
        return binding.searchTitle.getText().toString();
    }

    @Override
    public String getToken() {
        return homeUI.getToken();
    }

    @Override
    public String getRole() {
        return homeUI.getRole();
    }

    @Override
    public void menampilkanError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setVisibleBtnNext(boolean visible) {
        if(visible){
            binding.next.setVisibility(View.VISIBLE);

        }else{
            binding.next.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setVisibleBtnTambah(boolean visible) {
        if(visible){
            binding.btnTambah.setVisibility(View.VISIBLE);

        }else{
            binding.btnTambah.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void loadingAdapter() {
        binding.listview.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.item_list_pengumuman,R.id.TampilJudul,new String[]{"loading...."}));
    }

    @Override
    public void noDataAdapter() {
        binding.listview.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.item_list_pengumuman,R.id.TampilJudul,new String[]{"Hasil tidak ditemukan!"}));
    }


    @Override
    public void updateList(List<Pengumuman> list) {
        binding.listview.setAdapter(adapter);
        this.adapter.update(list);
    }

    @Override
    public void listenerOnClick(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        //pengumumanui msk ke detail pengumuman fragment buat ngejalanin metod" ui di detail
        //id string lewat args soalnya untuk kelas pengumuman ga bs msk ke bundle. krn gaada fragment put
        DetailPengumumanFragment detailPengumumanFragment = new DetailPengumumanFragment(this);
        detailPengumumanFragment.setArguments(bundle);
        FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
        detailPengumumanFragment.show(ft,"detail");

    }
    //dibutuhkan di filter
    //cmn ui yg d butuhin, krn view ga butuh d panggil d tmpt lain

    @Override
    public void reloadAdapter() {
        presenter.ambilTags();
        presenter.callAPI(false);
    }
}
