package com.freelance.me_owner.me_owner;

/**
 * Created by UutBT on 7/28/2018.
 */


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.freelance.me_owner.me_owner.adapter.NewsAdapter;
import com.freelance.me_owner.me_owner.app.AppController;
import com.freelance.me_owner.me_owner.module.NewsData;
import com.freelance.me_owner.me_owner.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class detail_datalokasi extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    NetworkImageView thumb_image;
    TextView nama, jenis_usaha, alamat, jam_operasional, harga, no_hp, deskripsi, lattmb, lngtmb, id_tmb;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SwipeRefreshLayout swipe;
    String id_tambalban,idtmbl;
    NewsAdapter adapter;
    CardView hapus;
    List<NewsData> newsList = new ArrayList<NewsData>();


    /*Deklarasi variable*/

    private static final String TAG = detail_datalokasi.class.getSimpleName();

    public static final String TAG_ID               = "id";
    public static final String TAG_NAMA             = "nama";
    public static final String TAG_JENIS_USAHA      = "jenis_usaha";
    public static final String TAG_ALAMAT           = "alamat";
    public static final String TAG_JAM_OPERASIONAL  = "jam_operasional";
    public static final String TAG_HARGA            = "harga";
    public static final String TAG_NO_HP            = "no_hp";
    public static final String TAG_DESKRIPSI        = "deskripsi";
    public static final String TAG_GAMBAR           = "gambar";
    public static final String TAG_lAT              = "lat";
    public static final String TAG_LNG              = "lng";

    private static final String url_detail  = Server.URL + "detail_tambalban.php";
    private static final String url_delete     = Server.URL + "hapus.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_datalokasi);
        //Menampilkan Button Back di Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Tambal Ban");

        thumb_image        = (NetworkImageView) findViewById(R.id.gambar_tmb);
        nama               = (TextView) findViewById(R.id.nama_tmb);
        jenis_usaha        = (TextView) findViewById(R.id.jenis_usaha_tmb);
        alamat             = (TextView) findViewById(R.id.alamat_tmb);
        jam_operasional    = (TextView) findViewById(R.id.jam_operasional_tmb);
        harga              = (TextView) findViewById(R.id.harga_tmb);
        no_hp              = (TextView) findViewById(R.id.no_hp_tmb);
        lattmb             = (TextView) findViewById(R.id.lattmb);
        lngtmb             = (TextView) findViewById(R.id.lngtmb);
        deskripsi          = (TextView) findViewById(R.id.deskripsi_tmb);
        id_tmb           = (TextView) findViewById(R.id.id_tmb);
        swipe              = (SwipeRefreshLayout) findViewById(R.id.swipe_detail);


        id_tambalban = getIntent().getStringExtra(TAG_ID);

        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           if (!id_tambalban.isEmpty()) {
                               callDetailtambalban(id_tambalban);
                           }
                       }
                   }
        );



        // menyamakan variable pada layout detail_tambalban.xml
        hapus    = (CardView) findViewById(R.id.hapus);
        hapus.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                AlertDialog.Builder builder = new AlertDialog.Builder(detail_datalokasi.this);
                builder.setCancelable(false);
                builder.setTitle("Me-Owner");
                builder.setMessage("Apakah Anda Yakin Ingin Menghapus Data ini?");
                builder.setIcon(R.mipmap.logo_me_owner);
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        delete(id_tambalban);

                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    // fungsi untuk menghapus
    private void delete(final String id){
        final ProgressDialog loading = ProgressDialog.show(this, "Hapus Data...", "Tunggu Sebentar...", false, false);
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());

                        Toast.makeText(detail_datalokasi.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        onBackPressed();

                    } else {
                        Toast.makeText(detail_datalokasi.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
                //menghilangkan progress dialog
                loading.dismiss();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //menghilangkan progress dialog
                loading.dismiss();
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(detail_datalokasi.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    public void editdata(View view) {
        idtmbl = id_tmb.getText().toString();
        Intent intent = new Intent(detail_datalokasi.this, Login_editdata.class);
        intent.putExtra(TAG_ID, idtmbl);
        startActivity(intent);

    }

    private void callDetailtambalban(final String id){
        swipe.setRefreshing(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, url_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response.toString());
                swipe.setRefreshing(false);


                try {
                    JSONObject obj = new JSONObject(response);

                    String Nama               = "Nama                      : "+obj.getString(TAG_NAMA);
                    String Jenis_usaha        = "Jenis Usaha           : "+obj.getString(TAG_JENIS_USAHA);
                    String Alamat             = "Alamat                    : " +obj.getString(TAG_ALAMAT);
                    String Jam_operasional    = "Jam Operasional   : "+obj.getString(TAG_JAM_OPERASIONAL);
                    String Harga              = "Harga                      : "+obj.getString(TAG_HARGA);
                    String No_hp              = "No Handphone      : "+obj.getString(TAG_NO_HP);
                    String Deskripsi          = obj.getString(TAG_DESKRIPSI);
                    String Gambar             = obj.getString(TAG_GAMBAR);
                    String Idtmb              = obj.getString(TAG_ID);

                    nama.setText(Nama);
                    jenis_usaha.setText(Jenis_usaha);
                    alamat.setText(Alamat);
                    jam_operasional.setText(Jam_operasional);
                    harga.setText(Harga);
                    no_hp.setText(No_hp);
                    deskripsi.setText(Deskripsi);
                    id_tmb.setText(Idtmb);

                    if (obj.getString(TAG_GAMBAR)!=""){
                        thumb_image.setImageUrl(Gambar, imageLoader);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Detail Data Lokasi Error: " + error.getMessage());
                Toast.makeText(detail_datalokasi.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                swipe.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        finish();
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

    @Override
    public void onRefresh() {
        callDetailtambalban(id_tambalban);
    }
}