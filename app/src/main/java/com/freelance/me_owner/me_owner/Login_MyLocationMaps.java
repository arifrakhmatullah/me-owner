package com.freelance.me_owner.me_owner;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.freelance.me_owner.me_owner.app.AppController;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by UutBT on 11/2/2018.
 */

public class Login_MyLocationMaps extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    MapFragment mapFragment;
    LinearLayout view;
    GoogleMap gMap;
    GoogleApiClient mGoogleApiClient;
    MarkerOptions markerOptions = new MarkerOptions();
    MarkerOptions markerOptionsBensin = new MarkerOptions();
    LatLng latLng_b;
    LatLng latLng_c;
    String title;
    String title_c;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;

    //button image view+save
    Intent intent;
    Uri fileUri;
    CardView mtakepicture;
    ImageView imageView;
    Bitmap bitmap, decoded;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    int bitmap_size = 40; // image quality 1 - 100;
    int max_resolution_image = 800;
    CardView msave;
    EditText nama;
    Spinner jenis_usaha;
    EditText alamat;
    EditText jamoperasional;
    EditText harga;
    EditText no_hp;
    EditText deskripsi;
    TextView id_pemilik,id_pemilik_bns;
    int success;
    SharedPreferences sharedpreferences;
    String id;


    //button set coordinates
    CardView mtakelatlng;
    TextView mlat;
    TextView mlng;

    private FusedLocationProviderClient client;

    //tambalban
    public static final String TITLE = "nama";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    //bensineceran
    public static final String TITLE_C = "nama_b";
    public static final String LAT_B = "lat_b";
    public static final String LNG_B = "lng_b";

    private String url = "https://official-menambal.000webhostapp.com/android/me-nambal/mylocation_tambalban_pemilik.php";
    private String url_bensin = "https://official-menambal.000webhostapp.com/android/me-nambal/mylocation_bensineceran_pemilik.php";

    //save
    private static final String TAG = Login_MyLocationMaps.class.getSimpleName();
    private String UPLOAD_URL = "https://official-menambal.000webhostapp.com/android/me-nambal/upload_pemilik.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "gambar";
    private String KEY_NAME = "nama";
    private String KEY_JENIS_USAHA = "jenis_usaha";
    private String KEY_ALAMAT= "alamat";
    private String KEY_JAM_OPERASIONAL = "jam_operasional";
    private String KEY_HARGA = "harga";
    private String KEY_NO_HP = "no_hp";
    private String KEY_DESKRIPSI = "deskripsi";
    private String KEY_LAT = "lat";
    private String KEY_LNG = "lng";
    private String TAG_ID = "id";
    private String KEY_ID_PEMILIK = "id_pemilik";


    //bensinsave
    private String KEY_IMAGE_B = "gambar_b";
    private String KEY_NAME_B = "nama_b";
    private String KEY_JENIS_USAHA_B = "jenis_usaha_b";
    private String KEY_ALAMAT_B= "alamat_b";
    private String KEY_JAM_OPERASIONAL_B = "jam_operasional_b";
    private String KEY_HARGA_B = "harga_b";
    private String KEY_NO_HP_B = "no_hp_b";
    private String KEY_DESKRIPSI_B = "deskripsi_b";
    private String KEY_LAT_B = "lat_b";
    private String KEY_LNG_B = "lng_b";
    private String KEY_ID_PEMILIK_BNS = "id_pemilik_bns";


    String tag_json_objsave = "json_obj_req";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_mylocationmaps_main);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_login);
        mapFragment.getMapAsync(this);

        msave = (CardView) findViewById(R.id.save);
        nama = (EditText) findViewById(R.id.nama);
        jenis_usaha = (Spinner) findViewById(R.id.jenis_usaha_option);
        alamat = (EditText) findViewById(R.id.alamat);
        jamoperasional = (EditText) findViewById(R.id.jam_operasional);
        harga = (EditText) findViewById(R.id.harga);
        no_hp = (EditText) findViewById(R.id.no_hp);
        deskripsi = (EditText) findViewById(R.id.deskripsi);

        mtakepicture = (CardView) findViewById(R.id.takepicture);
        imageView = (ImageView) findViewById(R.id.imageViewtakepicture);
        id_pemilik = (TextView) findViewById(R.id.txt_id_pemilik);
        id_pemilik_bns = (TextView) findViewById(R.id.txt_id_pemilik_bns);

        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(TAG_ID, null);


        id_pemilik.setText(id);
        id_pemilik_bns.setText(id);

        mtakepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        msave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        client = LocationServices.getFusedLocationProviderClient(this);
        mtakelatlng = (CardView) findViewById(R.id.takelatlng);
        mtakelatlng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(Login_MyLocationMaps.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                client.getLastLocation().addOnSuccessListener(Login_MyLocationMaps.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if(location != null) {
                            mlat = (TextView) findViewById(R.id.lat);
                            mlng = (TextView) findViewById(R.id.lng);

                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            mlat.setText(""+latitude);
                            mlng.setText(""+longitude);

                        }

                    }
                });
            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void uploadImage() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());

                                Toast.makeText(Login_MyLocationMaps.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                kosong();
                                onBackPressed();

                            } else {
                                Toast.makeText(Login_MyLocationMaps.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                onBackPressed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(Login_MyLocationMaps.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(KEY_IMAGE, getStringImage(decoded));
                params.put(KEY_NAME, nama.getText().toString().trim());
                params.put(KEY_JENIS_USAHA, jenis_usaha.getSelectedItem().toString().trim());
                params.put(KEY_ALAMAT, alamat.getText().toString().trim());
                params.put(KEY_JAM_OPERASIONAL, jamoperasional.getText().toString().trim());
                params.put(KEY_HARGA, harga.getText().toString().trim());
                params.put(KEY_NO_HP, no_hp.getText().toString().trim());
                params.put(KEY_DESKRIPSI, deskripsi.getText().toString().trim());
                params.put(KEY_LAT, mlat.getText().toString().trim());
                params.put(KEY_LNG, mlng.getText().toString().trim());
                params.put(KEY_ID_PEMILIK, id_pemilik.getText().toString().trim());

                //paramaterbensin
                params.put(KEY_IMAGE_B, getStringImage(decoded));
                params.put(KEY_NAME_B, nama.getText().toString().trim());
                params.put(KEY_JENIS_USAHA_B, jenis_usaha.getSelectedItem().toString().trim());
                params.put(KEY_ALAMAT_B, alamat.getText().toString().trim());
                params.put(KEY_JAM_OPERASIONAL_B, jamoperasional.getText().toString().trim());
                params.put(KEY_HARGA_B, harga.getText().toString().trim());
                params.put(KEY_NO_HP_B, no_hp.getText().toString().trim());
                params.put(KEY_DESKRIPSI_B, deskripsi.getText().toString().trim());
                params.put(KEY_LAT_B, mlat.getText().toString().trim());
                params.put(KEY_LNG_B, mlng.getText().toString().trim());
                params.put(KEY_ID_PEMILIK_BNS, id_pemilik_bns.getText().toString().trim());

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_objsave);
    }

    private void kosong() {
        imageView.setImageResource(0);
        nama.setText(null);
    }

    private void selectImage() {
        imageView.setImageResource(0);
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Login_MyLocationMaps.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.mipmap.logo_me_owner);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "requestCode " + requestCode + ", resultCode " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    Log.e("CAMERA", fileUri.getPath());

                    bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
                try {
                    // mengambil gambar dari Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(Login_MyLocationMaps.this.getContentResolver(), data.getData());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Untuk menampilkan bitmap pada ImageView
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);
    }

    // Untuk resize bitmap
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Me-Owner");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_Me-Owner_" + timeStamp + ".jpg");

        return mediaFile;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(true);

        //Memulai Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                gMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            gMap.setMyLocationEnabled(true);
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //move map camera
        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

        getMarkers();
        getMarkerBensin();
        //menghentikan pembaruan lokasi
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Izin diberikan.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        gMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Izin ditolak.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //untuk tambalban
    private void addMarker(LatLng latlng_b, final String title) {
        //tambalban
        markerOptions.position(latlng_b);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.tambalban1));
        gMap.addMarker(markerOptions);


        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    //untuk bensin eceran
    private void addMarkerBensin(LatLng latlng_c, final String title_c) {
        //bensin
        markerOptionsBensin.position(latlng_c);
        markerOptionsBensin.title(title_c);
        markerOptionsBensin.icon(BitmapDescriptorFactory.fromResource(R.drawable.minyakecer1));
        gMap.addMarker(markerOptionsBensin);

        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    // Fungsi get JSON marker
    private void getMarkers() {
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString("tambalban");
                    JSONArray jsonArray = new JSONArray(getObject);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        title = jsonObject.getString(TITLE);
                        latLng_b = new LatLng(Double.parseDouble(jsonObject.getString(LAT)), Double.parseDouble(jsonObject.getString(LNG)));
                        // Menambah data marker untuk di tampilkan ke google map
                        addMarker(latLng_b, title);
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(Login_MyLocationMaps.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(KEY_ID_PEMILIK, id_pemilik.getText().toString().trim());

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };


        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    // Fungsi get JSON marker
    private void getMarkerBensin() {
        StringRequest strReq = new StringRequest(Request.Method.POST, url_bensin, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getObject = jObj.getString("bensineceran");
                    JSONArray jsonArray = new JSONArray(getObject);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        title_c = jsonObject.getString(TITLE_C);
                        latLng_c = new LatLng(Double.parseDouble(jsonObject.getString(LAT_B)), Double.parseDouble(jsonObject.getString(LNG_B)));
                        // Menambah data marker untuk di tampilkan ke google map
                        addMarkerBensin(latLng_c, title_c);
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast.makeText(Login_MyLocationMaps.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(KEY_ID_PEMILIK_BNS, id_pemilik_bns.getText().toString().trim());

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}

