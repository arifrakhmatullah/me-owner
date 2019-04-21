package com.freelance.me_owner.me_owner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by UutBT on 1/14/2019.
 */

public class Login_keluar extends AppCompatActivity {

    CardView btn_logout;
    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "nama";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_GAMBAR = "gambar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_keluar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Keluar");

        btn_logout = (CardView) findViewById(R.id.btn_logout);
        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // update login session ke FALSE dan mengosongkan nilai id dan username
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(Login.session_status, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_USERNAME, null);
                editor.putString(TAG_EMAIL, null);
                editor.putString(TAG_GAMBAR, null);
                editor.commit();

                Intent intent = new Intent(Login_keluar.this, Login.class);
                finish();
                startActivity(intent);
            }
        });

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