package com.quickjob.quickjobFinal.quickjobFind.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quickjob.quickjobFinal.R;
import com.quickjob.quickjobFinal.quickjobFind.adapter.ListJobCompanyAdapter;
import com.quickjob.quickjobFinal.quickjobFind.model.CongViec;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class MoreNew_Activity extends AppCompatActivity {
    JSONArray mang;
    private ListJobCompanyAdapter adapter = null;
    int countdata,beginloadmore=0,st=0;
    boolean loading;
    private ProgressBar progressBar;
    private List<CongViec> category = new ArrayList<>();
    private List<CongViec> secondlist = new ArrayList<>();
    List<CongViec> tempArrayList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    int firstVisibleItem, visibleItemCount, totalItemCount,pastVisiblesItems;
    private boolean loading2= true;
    String[] arrcarrer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_new);
        //setContentView(R.layout.search_morehot);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);
        arrcarrer=getResources().getStringArray(R.array.nganhNghe);
        Intent i = getIntent();
        category= (List<CongViec>) i.getSerializableExtra("object");
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
      //  String[] split=category.get(0).getDiadiem().split(",");
     //   int count=split.length;
        String namecity=i.getStringExtra("namecity");
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.customtextview_actionbar, null);
        TextView customtv=((TextView)v.findViewById(R.id.txtcategory));
        customtv.setText(arrcarrer[Integer.parseInt(category.get(0).getNganhNghe())]+" in "+namecity);
        getSupportActionBar().setCustomView(v);
        addView();
        loading = true;


    }

    private void addView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new ListJobCompanyAdapter(MoreNew_Activity.this, category, 2, "", 1);
        recyclerView.setAdapter(adapter);
        mLayoutManager = new LinearLayoutManager(MoreNew_Activity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
//        {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
//            {
//
//                if(dy > 0) //check for scroll down
//                {
//                    visibleItemCount = mLayoutManager.getChildCount();
//                    totalItemCount = mLayoutManager.getItemCount();
//                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//
//                    if (loading2)
//                    {
//                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
//                        {
//                            loading2 = false;
//                            Log.v("...", "Last Item Wow !");
//                            asyncRequestObject = new AsyncDataClass();
//                            asyncRequestObject.execute(AppConfig.URL_MORENEW,beginloadmore+"");
//
//                        }
//                    }
//                }
//            }
//        });
//
//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(MoreNew_Activity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int i) {
//                        if(st==0)
//                        {
//                            intentdetail(celebrities,i);
//                        }else{
//                            intentdetail(tempArrayList,i);
//                        }
//
//                    }
//
//                    @Override
//                    public boolean onLongItemClick(View view, int position) {
//                        // do whatever
//                        return false;
//                    }
//                })
//        );
      //  mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshContent();
//            }
//
//        });
    }
    private void refreshContent() {
//        beginloadmore=0;
//        if(celebrities!= null)
//        {
//            celebrities.clear();
//            adapter.notifyDataSetChanged();
//        }
//        asyncRequestObject = new AsyncDataClass();
//        asyncRequestObject.execute(AppConfig.URL_MORENEW,beginloadmore+"");
       // mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MoreNew_Activity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                st=1;
                int textlength = newText.length();
                tempArrayList = new ArrayList<CongViec>();
                for(CongViec c: category){
                    if (textlength <= c.tencongviec.length()) {
                        if (c.tencongviec.toLowerCase().contains(newText.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }else{
                            // celebrities.remove(c);
                        }
                        // adapter.notifyDataSetChanged();
                        adapter = new ListJobCompanyAdapter(MoreNew_Activity.this, tempArrayList, 2, "", 1);
                        recyclerView.setAdapter(adapter);
                    }
                }

                return false;
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
