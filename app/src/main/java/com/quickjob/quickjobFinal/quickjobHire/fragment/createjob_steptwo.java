package com.quickjob.quickjobFinal.quickjobHire.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.quickjob.quickjobFinal.R;


/**
 * Created by VinhNguyen on 1/8/2017.
 */

public class createjob_steptwo extends Fragment {
    View view;
    private Spinner sptennganhnghe,spmucluong;
    private EditText eddc, edtencv,edmotacv,edhannophoso,edsoluong;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.createjob_steptwo_hire,container,false);
        addView();
        return view;
    }

    private void addView() {
        eddc= (EditText) view.findViewById(R.id.eddc) ;
        edhannophoso = (EditText) view.findViewById(R.id.hannophoso);
        edsoluong = (EditText) view.findViewById(R.id.soluong);
        edtencv = (EditText) view.findViewById(R.id.tencv);
        edmotacv = (EditText) view.findViewById(R.id.motacv);

        spmucluong = (Spinner) view.findViewById(R.id.mucluong);
        sptennganhnghe = (Spinner) view.findViewById(R.id.nganhnghe1);
//        spchucdanh = (Spinner) view.findViewById(R.id.chucdanh);

//        ArrayAdapter adaptercd= ArrayAdapter.createFromResource(getActivity(),R.array.spChucDanh,android.R.layout.simple_spinner_item);
//        adaptercd.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        //spchucdanh.setAdapter(adaptercd);
        ArrayAdapter nganhNghe= ArrayAdapter.createFromResource(getActivity(),R.array.nganhNghe,android.R.layout.simple_spinner_item);
        nganhNghe.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sptennganhnghe.setAdapter(nganhNghe);
        ArrayAdapter mucLuong= ArrayAdapter.createFromResource(getActivity(),R.array.mucluong,android.R.layout.simple_spinner_item);
        mucLuong.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spmucluong.setAdapter(mucLuong);

    }
}
