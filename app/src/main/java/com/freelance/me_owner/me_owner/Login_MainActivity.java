package com.freelance.me_owner.me_owner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.freelance.me_owner.me_owner.app.AppController;

public class Login_MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView mNavigationView;
    View mHeaderView;

    Button btn_logout;
    TextView txt_id, txt_username,txt_email;
    String id, username,email,gambar2;
    CircularNetworkImageView gambar;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    SharedPreferences sharedpreferences;

    FragmentManager fragmentManager;
    Fragment fragment = null;

    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "nama";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_GAMBAR = "gambar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mHeaderView = mNavigationView.getHeaderView(0);

        txt_id = (TextView)mHeaderView.findViewById(R.id.txt_id);
        txt_username = (TextView)mHeaderView.findViewById(R.id.txt_username);
        txt_email = (TextView)mHeaderView.findViewById(R.id.txt_email);
        gambar = (CircularNetworkImageView) mHeaderView.findViewById(R.id.gambar);
        btn_logout = (Button)mHeaderView.findViewById(R.id.btn_logout);

        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        id = getIntent().getStringExtra(TAG_ID);
        username = getIntent().getStringExtra(TAG_USERNAME);
        email = getIntent().getStringExtra(TAG_EMAIL);
        gambar2 = getIntent().getStringExtra(TAG_GAMBAR);


        txt_id.setText(id);
        txt_username.setText(username);
        txt_email.setText(email);
        gambar.setImageUrl(gambar2, imageLoader);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    public void tambalban(View view) {
        Intent intent = new Intent(Login_MainActivity.this, datalokasi.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.data_lokasi) {

            fragment = new menu_datalokasi();
            callFragment(fragment);


        } else if (id == R.id.tambah_lokasi) {
            Intent intent = new Intent(Login_MainActivity.this, Login_MyLocationMaps.class);
            startActivity(intent);

        } else if (id == R.id.lokasi_saya) {
            Intent intent = new Intent(Login_MainActivity.this, MyLocationMaps.class);
            startActivity(intent);

        } else if (id == R.id.profil) {

        } else if (id == R.id.keluar) {
            Intent intent = new Intent(Login_MainActivity.this, Login_keluar.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // untuk mengganti isi kontainer menu yang dipiih

    private void callFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container_login, fragment)
                .commit();
    }
}
