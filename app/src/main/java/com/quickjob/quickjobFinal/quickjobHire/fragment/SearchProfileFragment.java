package com.quickjob.quickjobFinal.quickjobHire.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobHire.VolleySingleton;
import com.quickjob.quickjobFinal.quickjobHire.activity.ProfileDetailActivity;
import com.quickjob.quickjobFinal.quickjobHire.adapter.RecyclerAdapter;
import com.quickjob.quickjobFinal.quickjobHire.config.AppConfig;
import com.quickjob.quickjobFinal.quickjobHire.config.AppController;
import com.quickjob.quickjobFinal.quickjobHire.model.Profile;
import com.quickjob.quickjobFinal.quickjobHire.pref.RecyclerItemClickListener;
import com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.quickjob.quickjobFinal.quickjobHire.pref.SessionManager.KEY_ID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String[] arr={"Tất cả ngành nghề","Dược sỹ","Trình dược viên","Bảo trì/Sửa chửa","Bán hàng","Bảo hiểm","Bất động sản","Biên dịch viên","Chứng khoáng","Công nghệ cao","IT Phần cứng/Mạng","Internet/Online Media","IT -Phần mềm","Cơ khí,chế tạo","Dệt may/Da giày","Dịch vụ khách hàng","Hàng không/Du lịch","Điện/Điện tử","Giáo dục/Đào tạo","Kế toán","Kiểm toán","Y tế/Chăm sóc sức khỏe","Kiến trúc/Thiết kế nội thất","Ngân hàng","Mỹ thuật/Nghệ thuật/Thiết kế","Nhân sự","Nông nghiệp/Lâm nghiệp","Pháp lý","Phi chính phủ/Phi lợi nhuận","Cấp quản lý điều hành","Quản cáo/Khuyến mại/Đối thoại","Sản Xuất","Thời vụ/Hợp đồng ngắn hạn","Tài chính/Đầu tư","Thời trang","Thực phẩm đồ uống","Truyền hình/Truyền thông/Báo chí","Marketing","Tư vấn","Vận chuyển/Giao nhận","Thu mua/Vật tư/Cung vận","Viễn thông","Xây dựng","Xuất nhập khẩu","Tự động hóa/Ôtô","Overseass Jop","Khác"};
    String[] arr2={"Tất cả địa điểm","Hà Nội", "Đà Nẵng", "Hồ Chí Minh", "An Giang", "Bà Rịa-Vũng Tàu", "Bắc Cạn", "Bắc Giang", "Bạc Liêu", "Bắc Ninh", "Bến Tre", "Biên Hòa", "Bình Định", "Bình Dương", "Bình Phước", "Bình Thuận", "Cà Mau", "Cần Thơ", "Cao Bằng", "Đắc Lắc", "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Tây", "Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hòa Bình", "Huế", "Hưng Yên", "Khánh hòa", "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa-Thiên-Huế", "Tiền Giang", "Trà Vinh", "Tuyên Quang", "Kiên Giang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái", "Quốc Tế", "Hậu Giang", "Khác"};
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Profile> celebrities = new ArrayList<>();
    JSONArray mang;
    int countdata;
    int begin = 0;
    boolean loading=true;
    int beginloadmore=0;
    private RecyclerAdapter adapter = null;
    String location="", job="",salary="",exe="",sex="";
    private EditText tencv;
    private Spinner nganhnghe,mucluong,diadiem;
    private ArrayAdapter adapternn,adapterdd,adaptersalary,adapterexe,adaptersex,adapterloca,adapternganh;
    private View view;
    private ProgressBar progressBar;
    LinearLayout linlv;
    SessionManager session;
    HashMap<String, String> user;
    ImageView filter;
    private RecyclerView recyclerView; SwipeRefreshLayout swipe_refresh;
    private OnFragmentInteractionListener mListener;
    String[] arrnn,arrdd,arrsalary,arrexe,arrsex;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public SearchProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchProfileFragment newInstance(String param1, String param2) {
        SearchProfileFragment fragment = new SearchProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.searchprofile_layout_hire, container, false);
        beginloadmore = 0;
        if (adapter != null) {
            celebrities.clear();
            adapter.notifyDataSetChanged();
        }
        addView();
        loading = true;
        getData(job,location,salary,exe,sex,beginloadmore+"");

        return view;
    }
    private void addView() {
        swipe_refresh =(SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.pbLoader);
        nganhnghe = (Spinner) view.findViewById(R.id.spnganh);
        diadiem = (Spinner) view.findViewById(R.id.spdiadiem);
        filter = (ImageView) view.findViewById(R.id.filter) ;
      //  mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        // View footer = getActivity().getLayoutInflater().inflate(R.layout.progress_bar_footer, null);
        // progressBar = (ProgressBar) footer.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(getActivity(), celebrities, 1);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

         arrnn = getResources().getStringArray(R.array.nganhNghe);
         arrdd = getResources().getStringArray(R.array.select_location_search);
        arrsalary= getResources().getStringArray(R.array.mucluong);
        arrexe= getResources().getStringArray(R.array.spNamKinhNghiem);
        arrsex= getResources().getStringArray(R.array.spsex);
        adapternn = new ArrayAdapter<String>(getActivity(), R.layout.customspniner_hire, arrnn);
        adapternn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nganhnghe.setAdapter(adapternn);
        session = new SessionManager(getContext());
       user = session.getUserDetails();

        adapterdd = new ArrayAdapter<String>(getActivity(), R.layout.customspniner_hire, arrdd);
        adapterdd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diadiem.setAdapter(adapterdd);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int i) {
                        Intent is = new Intent(getActivity(), ProfileDetailActivity.class);
//                        is.putExtra("ten", celebrities.get(i).ten);
                        is.putExtra("iduser", celebrities.get(i).iduser);
//                        is.putExtra("gioitinh", celebrities.get(i).gioitinh);
//                        is.putExtra("ngaysinh", celebrities.get(i).ngaysinh);
//                        is.putExtra("email", celebrities.get(i).email);
//                        is.putExtra("sdt", celebrities.get(i).sdt);
//                        is.putExtra("kinhnghiem",celebrities.get(i).namkn);
//                        is.putExtra("tencongty", celebrities.get(i).tencongty);
//                        is.putExtra("chucdanh", celebrities.get(i).chucdanh);
//                        is.putExtra("motacv", celebrities.get(i).motacv);
//                        is.putExtra("quequan", celebrities.get(i).quequan);
//                        is.putExtra("diachi", celebrities.get(i).diachi);
//                        is.putExtra("mucluong", celebrities.get(i).mucluong);
//                        is.putExtra("kynang", celebrities.get(i).kynang);
//                        is.putExtra("ngoaingu", celebrities.get(i).ngoaingu);
//                        is.putExtra("img", celebrities.get(i).img);
                        is.putExtra("mahs", celebrities.get(i).id);
//                        is.putExtra("slogan", celebrities.get(i).slogan);
//                        is.putExtra("education", celebrities.get(i).educaton);
//                        is.putExtra("workplace", celebrities.get(i).quequan);
//                        is.putExtra("statussave",celebrities.get(i).statussave);
                        is.putExtra("key", 0);
                        startActivity(is);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
//       mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshContent();
//            }
//
//        });
        nganhnghe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!loading) {
                    loading = true;
                    progressBar.setVisibility(View.VISIBLE);

                    int pos = nganhnghe.getSelectedItemPosition();
                    job =pos+"";

                    search(job,location,"","","");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        diadiem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!loading) {
                    loading = true;
                    progressBar.setVisibility(View.VISIBLE);

                    int pos2 = diadiem.getSelectedItemPosition();
                    if (pos2 > 0) {
                        location = arr2[pos2];
                    } else {
                        location="";
                    }
                    search(job,location,"","","");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocationDialog();
            }
        });
    }

    private void search(String job, String location,String salary,String exe,String sex) {
        beginloadmore=0;
        if (adapter != null) {
            celebrities.clear();
            adapter.notifyDataSetChanged();
        }
        getData(job,location,salary,exe,sex,beginloadmore+"");
    }

    private void refreshContent() {
        beginloadmore=0;
        if (adapter != null) {
            celebrities.clear();
            adapter.notifyDataSetChanged();
        }
        getData(job,location,salary,exe,sex,beginloadmore+"");

    }
    private void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View view1 = inflater.inflate(R.layout.dialog_filtersearch_hire, null);
        final Spinner spnganh = (Spinner) view1.findViewById(R.id.spnganh);
        adapternganh = new ArrayAdapter<String>(getActivity(), R.layout.custom_spniner_filter_hire, arrnn);
        adapternganh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnganh.setAdapter(adapternganh);
        adapterloca = new ArrayAdapter<String>(getActivity(), R.layout.custom_spniner_filter_hire, arrdd);
        adapterloca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sploca = (Spinner) view1.findViewById(R.id.splocation);
        sploca.setAdapter(adapterloca);
        final Spinner spsalary = (Spinner) view1.findViewById(R.id.spsalary);
        adaptersalary = new ArrayAdapter<String>(getActivity(), R.layout.custom_spniner_filter_hire, arrsalary);
        adaptersalary.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spsalary.setAdapter(adaptersalary);
        final Spinner spexe = (Spinner) view1.findViewById(R.id.spexe);
        adapterexe = new ArrayAdapter<String>(getActivity(), R.layout.custom_spniner_filter_hire, arrexe);
        adapterexe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spexe.setAdapter(adapterexe);
        final Spinner spsex = (Spinner) view1.findViewById(R.id.spsex);
        adaptersex = new ArrayAdapter<String>(getActivity(), R.layout.custom_spniner_filter_hire, arrsex);
        adaptersex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spsex.setAdapter(adaptersex);
        builder.setTitle(getString(R.string.search_main));
        builder.setView(view1);
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int pos = spnganh.getSelectedItemPosition();

                         job = pos+"";

                        int pos2 = sploca.getSelectedItemPosition();
                        if (pos2 > 0) {
                            location = arr2[pos2];
                        } else {
                            location="";
                        }
                        int pos3= spsalary.getSelectedItemPosition();
                        if (pos3 > 0) {
                            salary = spsalary.getSelectedItemPosition()+"";
                        } else {
                            salary="";
                        }
                        int pos4= spexe.getSelectedItemPosition();
                        if (pos4 > 0) {
                            exe = spexe.getSelectedItemPosition()+"";
                        } else {
                            exe="";
                        }
                        int pos5= spsex.getSelectedItemPosition();
                        if (pos5 > 0) {
                            sex = spsex.getSelectedItemPosition()+"";
                        } else {
                            sex="";
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        search(job,location,salary,exe,sex);

                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
    private void parseJsonFeed(String result) {
        System.out.println("Resulted Value: " + result);
        if (result.equals("") || result == null) {
            Toast.makeText(getActivity(),"Không có hồ sơ nào được tìm thấy", Toast.LENGTH_SHORT).show();
            loading = false;
            progressBar.setVisibility(View.GONE);
            return;
        }
        try {
            mang = new JSONArray(result);
            countdata = mang.length();
            if(countdata>0) {
                if (adapter != null) {
                    celebrities.clear();
                    adapter.notifyDataSetChanged();
                }
                for (int i = 0; i < countdata; i++) {
                    JSONObject ob = mang.getJSONObject(i);
                    Profile cv = new Profile();
                    cv.setIduser(ob.getString("iduser"));
                    cv.setTen(ob.getString("hoten"));
                    cv.setGioitinh(ob.getString("gioitinh2"));
                    cv.setNgaysinh(ob.getString("ngaysinh"));
                    cv.setEmail(ob.getString("email"));
                    cv.setSdt(ob.getString("sdt"));
                    cv.setNamkn(ob.getString("namkn"));
                    cv.setTencongty(ob.getString("tencongty"));
                    cv.setDiadiem(ob.getString("diadiem"));
                    cv.setMucluong(ob.getString("mucluong"));
                    cv.setChucdanh(ob.getString("chucdanh"));
                    cv.setQuequan(ob.getString("diadiem"));
                    cv.setDiachi(ob.getString("diachi"));
                    cv.setNgoaingu(ob.getString("ngoaingu"));
                    cv.setKynang(ob.getString("kynang"));
                    cv.setNgoaingu(ob.getString("ngoaingu"));
                    cv.setImg(ob.getString("img"));
                    cv.setId(ob.getString("mahs"));
                    cv.setTencv(ob.getString("tencv"));
                    cv.setMucluong(ob.getString("mucluong"));
                    cv.setNgaydang(ob.getString("createdate"));
                    cv.setSlogan(ob.getString("slogan"));
                    cv.setEducaton(ob.getString("education"));
                    cv.setStatussave(ob.getString("status"));
                    celebrities.add(cv);
                }
                adapter.notifyDataSetChanged();

            }else{
                Toast.makeText(getActivity(),"Không có hồ sơ nào được tìm thấy", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        swipe_refresh.setRefreshing(false);
        loading = false;
        beginloadmore=beginloadmore+1;
        progressBar.setVisibility(View.GONE);
    }

    private void getData(final String job, final String location, final String salary, final String exe, final String sex, final String page) {
        //Getting the unique id generated at firebase
     /*   final String uniqueId = FirebaseInstanceId.getInstance().getToken();*/
        // Tag used to cancel the request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_XUATPROFILE_NEW, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                parseJsonFeed(result);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(AppController.TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("job", job);
                params.put("location", location);
                params.put("salary", salary);
                params.put("exe", exe);
                params.put("sex", sex);
                params.put("page", page);
                params.put("uid",user.get(KEY_ID));
//                Log.e("uid",user.get(KEY_ID)+"");
                return params;
            }
        };
        // Adding request to request queue
        strReq.setTag(this.getClass().getName());
        VolleySingleton.getInstance(getContext()).requestQueue.add(strReq);
    }
   /* private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);

            String jsonResult = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("job", params[1]));
                nameValuePairs.add(new BasicNameValuePair("location", params[2]));
                nameValuePairs.add(new BasicNameValuePair("salary", params[3]));
                nameValuePairs.add(new BasicNameValuePair("exe", params[4]));
                nameValuePairs.add(new BasicNameValuePair("sex", params[5]));
                nameValuePairs.add(new BasicNameValuePair("page", params[6]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned Json object " + jsonResult.toString());

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);
            if (result.equals("") || result == null) {
                Toast.makeText(getActivity(),"Không có hồ sơ nào được tìm thấy", Toast.LENGTH_SHORT).show();
                loading = false;
                progressBar.setVisibility(View.GONE);
                return;
            }
            try {
                mang = new JSONArray(result);
                countdata = mang.length();
                if(countdata>0) {
                    if (adapter != null) {
                        celebrities.clear();
                        adapter.notifyDataSetChanged();
                    }
                    for (int i = 0; i < countdata; i++) {
                        JSONObject ob = mang.getJSONObject(i);
                        Profile cv = new Profile();
                        cv.setTen(ob.getString("hoten"));
                        cv.setGioitinh(ob.getString("gioitinh2"));
                        cv.setNgaysinh(ob.getString("ngaysinh"));
                        cv.setEmail(ob.getString("email"));
                        cv.setSdt(ob.getString("sdt"));
                        cv.setNamkn(ob.getString("namkn"));
                        cv.setTencongty(ob.getString("tencongty"));
                        cv.setDiadiem(ob.getString("diadiem"));
                        cv.setMucluong(ob.getString("mucluong"));
                        cv.setChucdanh(ob.getString("chucdanh"));
                        cv.setQuequan(ob.getString("diadiem"));
                        cv.setDiachi(ob.getString("diachi"));
                        cv.setNgoaingu(ob.getString("ngoaingu"));
                        cv.setKynang(ob.getString("kynang"));
                        cv.setNgoaingu(ob.getString("ngoaingu"));
                        cv.setImg(ob.getString("img"));
                        cv.setId(ob.getString("mahs"));
                        cv.setTencv(ob.getString("tencv"));
                        cv.setMucluong(ob.getString("mucluong"));
                        cv.setNgaydang(ob.getString("createdate"));
                        cv.setSlogan(ob.getString("slogan"));
                        cv.setEducaton(ob.getString("education"));
                        celebrities.add(cv);
                    }
                    adapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(getActivity(),"Không có hồ sơ nào được tìm thấy", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loading = false;
            beginloadmore=beginloadmore+1;
            progressBar.setVisibility(View.GONE);
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return answer;
        }

    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if ( asyncRequestObject!= null) {
//            if (!asyncRequestObject.isCancelled()) {
//                asyncRequestObject.cancel(true);
//            }
//        }
//    }
}
