package com.freelance.me_owner.me_owner;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.freelance.me_owner.me_owner.app.AppController;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Register extends AppCompatActivity {


    //button image view+save
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Button btn_register;
    Intent intent;
    Uri fileUri;
    CardView mtakepicture;
    ImageView imageView;
    Bitmap bitmap, decoded;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    int bitmap_size = 40; // image quality 1 - 100;
    int max_resolution_image = 800;
    EditText nik;
    EditText no_kk;
    EditText nama;
    EditText tempat_lahir;
    EditText tanggal_lahir;
    Spinner jenis_kelamin;
    EditText alamat;
    EditText username;
    EditText password;
    EditText confirm_password;
    EditText email;
    EditText no_hp;
    EditText kota;
    int success;

    //save
    private static final String TAG = Register.class.getSimpleName();
    private String UPLOAD_URL = "https://official-menambal.000webhostapp.com/android/me-nambal/register.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "foto_profil";
    private String KEY_NIK = "nik";
    private String KEY_NO_KK = "no_kk";
    private String KEY_NAME = "nama";
    private String KEY_TEMPAT_LAHIR = "tempat_lahir";
    private String KEY_TANGGAL_LAHIR= "tanggal_lahir";
    private String KEY_JENIS_KELAMIN = "jenis_kelamin";
    private String KEY_ALAMAT = "alamat";
    private String KEY_USERNAME = "username";
    private String KEY_PASSWORD = "password";
    private String KEY_CONFIRM_PASSOWRD = "confirm_password";
    private String KEY_EMAIL = "email";
    private String KEY_NO_HP = "no_hp";
    private String KEY_KOTA = "kota";

    String tag_json_objsave = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        btn_register = (Button) findViewById(R.id.btn_register);
        nik = (EditText) findViewById(R.id.nik);
        no_kk = (EditText) findViewById(R.id.no_kk);
        nama = (EditText) findViewById(R.id.nama);
        tempat_lahir = (EditText) findViewById(R.id.tempat_lahir);
        tanggal_lahir = (EditText) findViewById(R.id.tanggal_lahir);
        jenis_kelamin = (Spinner) findViewById(R.id.jenis_kelamin);
        alamat = (EditText) findViewById(R.id.alamat);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        email = (EditText) findViewById(R.id.email);
        no_hp = (EditText) findViewById(R.id.no_hp);
        kota = (EditText) findViewById(R.id.kota);

        mtakepicture = (CardView) findViewById(R.id.takepicture);
        imageView = (ImageView) findViewById(R.id.imageViewtakepicture);

        mtakepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        tanggal_lahir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Register.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tanggal_lahir.setText(sdf.format(myCalendar.getTime()));
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

                                Toast.makeText(Register.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                kosong();
                                onBackPressed();

                            } else {
                                Toast.makeText(Register.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Register.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(KEY_IMAGE, getStringImage(decoded));
                params.put(KEY_NIK, nik.getText().toString().trim());
                params.put(KEY_NO_KK, no_kk.getText().toString().trim());
                params.put(KEY_NAME, nama.getText().toString().trim());
                params.put(KEY_TEMPAT_LAHIR, tempat_lahir.getText().toString().trim());
                params.put(KEY_TANGGAL_LAHIR, tanggal_lahir.getText().toString().trim());
                params.put(KEY_JENIS_KELAMIN, jenis_kelamin.getSelectedItem().toString().trim());
                params.put(KEY_ALAMAT, alamat.getText().toString().trim());
                params.put(KEY_USERNAME, username.getText().toString().trim());
                params.put(KEY_PASSWORD, password.getText().toString().trim());
                params.put(KEY_CONFIRM_PASSOWRD, confirm_password.getText().toString().trim());
                params.put(KEY_EMAIL, email.getText().toString().trim());
                params.put(KEY_NO_HP, no_hp.getText().toString().trim());
                params.put(KEY_KOTA, kota.getText().toString().trim());

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_objsave);
    }

    private void kosong() {
        imageView.setImageResource(0);
        nik.setText(null);

    }

    private void selectImage() {
        imageView.setImageResource(0);
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.mipmap.logo);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
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
                    bitmap = MediaStore.Images.Media.getBitmap(Register.this.getContentResolver(), data.getData());
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
    public void onBackPressed() {
        intent = new Intent(Register.this, Login.class);
        finish();
        startActivity(intent);
    }

}