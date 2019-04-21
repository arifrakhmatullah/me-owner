package com.freelance.me_owner.me_owner;

/**
 * Created by UutBT on 7/28/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.freelance.me_owner.me_owner.adapter.NewsAdapter;
import com.freelance.me_owner.me_owner.app.AppController;
import com.freelance.me_owner.me_owner.module.NewsData;
import com.freelance.me_owner.me_owner.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class datalokasi extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    ListView list;
    SwipeRefreshLayout swipe;
    TextView id_pemiliktambalban1;
    List<NewsData> newsList = new ArrayList<NewsData>();

    private static final String TAG = datalokasi.class.getSimpleName();

    private static String url_list   = Server.URL + "datalokasi.php?offset=";

    private int offSet = 0;

    int no;

    NewsAdapter adapter;
    public static final String TAG_NO       = "no";
    public static final String TAG_ID_TAMBALBAN       = "id";
    public static final String TAG_NAMA    = "nama";
    public static final String TAG_ALAMAT      = "alamat";
    public static final String TAG_JAM_OPERASIONAL      = "jam_operasional";
    public static final String TAG_JENIS_USAHA      = "jenis_usaha";
    public static final String TAG_GAMBAR   = "gambar";
    private String TAG_ID = "id";
    private String KEY_ID_PEMILIK = "id_pemilik";
    String tag_json_obj = "json_obj_req";
    SharedPreferences sharedpreferences;
    String id;

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datalokasi_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Data Lokasi Tambal Ban");

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list = (ListView) findViewById(R.id.list_view);



        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(TAG_ID, null);
        id_pemiliktambalban1 = (TextView) findViewById(R.id.txt_id_pemiliktambalban);
        id_pemiliktambalban1.setText(id);

        newsList.clear();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(datalokasi.this, detail_datalokasi.class);
                intent.putExtra(TAG_ID, newsList.get(position).getId());
                startActivity(intent);
           }
       });

        adapter = new NewsAdapter(datalokasi.this, newsList);
        list.setAdapter(adapter);

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           newsList.clear();
                           adapter.notifyDataSetChanged();
                           callNews(0);
                       }
                   }
        );


        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {

                    swipe.setRefreshing(true);
                    handler = new Handler();

                    runnable = new Runnable() {
                        public void run() {
                            callNews(offSet);
                        }
                    };

                    handler.postDelayed(runnable, 3000);
                }
            }

        });



    }

    @Override
    public void onRefresh() {
        newsList.clear();
        adapter.notifyDataSetChanged();
        callNews(0);
    }

    private void callNews(int page){

        swipe.setRefreshing(true);

        // Creating volley request obj
        StringRequest arrReq = new StringRequest(Request.Method.POST, url_list + page, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());
                            // Parsing json

                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            String getObject = obj.getString("tambalban");
                            JSONArray jsonArray = new JSONArray(getObject);

                            NewsData news = new NewsData();


                            JSONObject obj2 = jsonArray.getJSONObject(i);

                            no = obj2.getInt(TAG_NO);
                            news.setId(obj2.getString(TAG_ID_TAMBALBAN));
                            news.setNama(obj2.getString(TAG_NAMA));
                            news.setAlamat(obj2.getString(TAG_ALAMAT));
                            news.setJam_operasional(obj2.getString(TAG_JAM_OPERASIONAL));
                            news.setJenis_usaha(obj2.getString(TAG_JENIS_USAHA));


                            if (obj2.getString(TAG_GAMBAR) != "") {
                                news.setGambar(obj2.getString(TAG_GAMBAR));
                            }

                            // adding news to news array
                            newsList.add(news);

                            if (no > offSet)
                                offSet = no;

                            Log.d(TAG, "offSet " + offSet);

                        } catch (JSONException e) {
                            Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }

                        swipe.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(KEY_ID_PEMILIK, id_pemiliktambalban1.getText().toString().trim());

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq, tag_json_obj);
    }

    //aksi untuk menjalankan button back di toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}