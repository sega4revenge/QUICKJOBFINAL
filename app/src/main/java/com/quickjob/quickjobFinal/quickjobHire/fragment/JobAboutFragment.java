package com.quickjob.quickjobFinal.quickjobHire.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.activity.MainActivity;


public class JobAboutFragment extends Fragment {


    private static final int REQUEST_CALL = 0;
    public static final String TAG = "JobDetailActivity";
    public static String macv;
    int status = 0,luong=0,gt=0,hv=0;
    private TextView txttencv, txtdiachi, txtluong,txtbangcap, txtmotacv, txtkn, txtdotuoi, txtgt, txtnn, txtkhac, txtdate;
    private String id, sdt, tencv, tenct, diadiem, mucluong, ngayup, yeucaubangcap, dotuoi, ngoaingu, gioitinh, khac, motacv, kn,quymo,jobcompany,location,detail;
    int type;
    private View v;
    String[] arrnganh,arrhv,arrsalary,arrsex;

    public JobAboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        v =  inflater.inflate(R.layout.fragment_about_job_hire, container, false);
        arrsalary = getResources().getStringArray(R.array.mucluong);
        arrhv = getResources().getStringArray(R.array.spHocVan);
        arrsex= getResources().getStringArray(R.array.sex);
        init();
        actionGetIntent();
        if(kn.equals("")&&ngoaingu.equals("")&&dotuoi.equals(""))
        {
//            LinearLayout lin = (LinearLayout) v.findViewById(R.id.lininfor);
//            lin.setVisibility(View.GONE);
//            TextView txt = (TextView) v.findViewById(R.id.txtmt);
//            txt.setText(khac);
        }
        txtdiachi.setText(diadiem);
        txtluong.setText(arrsalary[luong] + " VND");
        txtdate.setText(ngayup + "");
        txtmotacv.setText(motacv + "");
        txttencv.setText(tencv + "");
        txtbangcap.setText(arrhv[hv] + "");
        txtkn.setText(kn + "");
        txtdotuoi.setText(dotuoi + "");
        txtgt.setText(arrsex[gt] + "");
        txtnn.setText(ngoaingu + "");
        txtkhac.setText(khac + "");
        id = MainActivity.uid;








        return v;


    }








    private void takePicture() {
        String phone = sdt;
        String number = phone + "";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        try {
            startActivity(callIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), R.string.st_loiKXD, Toast.LENGTH_SHORT).show();
        }
    }

    private void requestCameraPermission() {


        // Camera permission has not been granted yet. Request it directly.
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                REQUEST_CALL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i(TAG, "Received response for Camera permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Camera permission has been granted, preview can be displayed

                takePicture();

            } else {
                //Permission not granted
                Toast.makeText(getActivity(), R.string.st_pemissonCamera, Toast.LENGTH_SHORT).show();
            }

        }
    }




    private void init() {



//        txttencv = (TextView)  v.findViewById((R.id.txttencv));
//        txtdiachi = (TextView)  v.findViewById(R.id.txtdiadiem);
//        txtluong = (TextView) v.findViewById((R.id.txtluong));
//        txtbangcap = (TextView) v.findViewById(R.id.txtbc);
//        txtmotacv = (TextView) v.findViewById((R.id.txtmotacv));
//        txtkn = (TextView) v.findViewById((R.id.txtnamkn));
//        txtdotuoi = (TextView) v.findViewById((R.id.txtdotuoi));
//        txtgt = (TextView) v.findViewById(R.id.txtgioitinh);
//        txtnn = (TextView) v.findViewById((R.id.txtnn));
//        txtkhac = (TextView) v.findViewById((R.id.txtkhac));
//        txtdate = (TextView) v.findViewById(R.id.txthethan);



    }

    private void actionGetIntent() {
        Intent i = getActivity().getIntent();
        type= i.getIntExtra("type",1);
        tencv = i.getStringExtra("tencongviec");
        tenct = i.getStringExtra("tencongty");
        diadiem = i.getStringExtra("diadiem");
        luong=Integer.parseInt(i.getStringExtra("mucluong"));
        ngayup = i.getStringExtra("ngayup");
        if(i.getStringExtra("yeucaubangcap").equals(""))
        {

        }else{
            hv=Integer.parseInt(i.getStringExtra("yeucaubangcap"));
        }
        dotuoi = i.getStringExtra("dotuoi");
        ngoaingu = i.getStringExtra("ngoaingu");
        if(i.getStringExtra("gioitinh").equals(""))
        {

        }else{
            gt=Integer.parseInt(i.getStringExtra("gioitinh"));
        }

        khac = i.getStringExtra("khac");
        motacv = i.getStringExtra("motacv");
        kn = i.getStringExtra("kn");
        macv = i.getStringExtra("macv");
        sdt = i.getStringExtra("sdt");
        quymo = i.getStringExtra("quymo");
        jobcompany = i.getStringExtra("nganhnghe");
        detail = i.getStringExtra("motact");
        location = i.getStringExtra("diachi");


    }





}
