package com.freelance.me_owner.me_owner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.freelance.me_owner.me_owner.app.AppController;


public class MainActivity extends AppCompatActivity {

    Button btn_logout;
    TextView txt_id, txt_username,txt_email;
    String id, username,email,gambar2;
    NetworkImageView gambar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SharedPreferences sharedpreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "nama";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_GAMBAR = "gambar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_id = (TextView) findViewById(R.id.txt_id);
        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_email = (TextView) findViewById(R.id.txt_email);
        gambar = (NetworkImageView) findViewById(R.id.gambar);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        email = getIntent().getStringExtra(TAG_EMAIL);
        username = getIntent().getStringExtra(TAG_USERNAME);
        gambar2 = getIntent().getStringExtra(TAG_GAMBAR);


        txt_id.setText("ID : " + id);
        txt_username.setText("USERNAME : " + username);
        txt_email.setText("EMAIL : " + email);
        gambar.setImageUrl(gambar2, imageLoader);

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

                Intent intent = new Intent(MainActivity.this, Login.class);
                      finish();
              startActivity(intent);
          }
        });

    }
}