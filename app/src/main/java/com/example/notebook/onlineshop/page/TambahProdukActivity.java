package com.example.notebook.onlineshop.page;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notebook.onlineshop.R;
import com.example.notebook.onlineshop.helper.ImageFilePath;
import com.example.notebook.onlineshop.helper.PreferencesManager;
import com.example.notebook.onlineshop.model.KategoriModel;
import com.example.notebook.onlineshop.network.HttpRequestData;
import com.example.notebook.onlineshop.network.MultipartUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class TambahProdukActivity extends AppCompatActivity {

    Spinner spinnerKategori;
    EditText editTextNama, editTextHarga, editTextDeskripsi;
    ImageView imagePhoto;
    Button buttonTambahkan;
    ProgressDialog progressDialog;

    List<KategoriModel> spinnerModelLis = new ArrayList<>();
    List<String> spinnerItem = new ArrayList<String>();
    File photo;

    int spinerPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);
        initView();
        requestKategoriData();
    }

    private void initView(){
        spinnerKategori = (Spinner) findViewById(R.id.spinner_kategori);
        editTextNama = (EditText) findViewById(R.id.text_nama);
        editTextHarga = (EditText) findViewById(R.id.text_harga);
        editTextDeskripsi = (EditText) findViewById(R.id.text_deskripsi);
        imagePhoto = (ImageView) findViewById(R.id.image_photo);
        buttonTambahkan = (Button) findViewById(R.id.button_tambahkan);


        buttonTambahkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle ketika tombol tambahkan di klik
                String nama, harga, deskripsi;
                nama = editTextNama.getText().toString();
                harga = editTextHarga.getText().toString();
                deskripsi = editTextDeskripsi.getText().toString();
                if (nama.equals("") || harga.equals("") || deskripsi.equals("") || photo == null ){
                    Toast.makeText(TambahProdukActivity.this, "Semua data harus diisi dengan lengkap", Toast.LENGTH_SHORT).show();
                }else {
                    String id_user = PreferencesManager.getPreferanceValue(getApplicationContext(), PreferencesManager.userId);
                    String id_kategori = spinnerModelLis.get(spinerPosition).getId();
                    kirimData(id_user, id_kategori, nama, harga, deskripsi, photo);
                }
            }
        });

        imagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle ketika tombol foto di klik
                openImageLibrary();
            }
        });

        spinnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //handle ketika item pada spinner di klik
                spinerPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void requestKategoriData(){
        new HttpRequestData("http://rizmaulana.cloudapp.net/API/onlineshop/index.php/service/getkategori", new HttpRequestData.MultipartCallback() {
            @Override
            public void onPreExecuted() {
                progressDialog = new ProgressDialog(TambahProdukActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Meminta data kategori, harap tunggu ...");
                progressDialog.show();
            }

            @Override
            public MultipartUtility parameters(String url) {
                try {
                    MultipartUtility parameter = new MultipartUtility(url, "UTF-8", new HashMap<String, String>());
                    return  parameter;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onResponseServer(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String result = jsonObject.getString("result");
                    if (result.equals("true")){
                        JSONArray jsonArray = jsonObject.getJSONArray("kategori");
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject data = jsonArray.getJSONObject(i);
                            KategoriModel model = new KategoriModel();
                            model.setId(data.getString("id_kategori"));
                            model.setNama(data.getString("kategori_nama"));
                            spinnerModelLis.add(model);
                            spinnerItem.add(model.getNama());
                        }
                        progressDialog.dismiss();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, spinnerItem);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerKategori.setAdapter(adapter);
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(TambahProdukActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(TambahProdukActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }).execute();
    }

    private void openImageLibrary(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }else {
                showImageLibrary();
            }
        }else {
            showImageLibrary();
        }

    }

    private void showImageLibrary(){
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(TambahProdukActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        photo = new File(ImageFilePath.getPath(getApplicationContext(), uri));
                        Bitmap bitmap = BitmapFactory.decodeFile(ImageFilePath.getPath(getApplicationContext(), uri));
                        imagePhoto.setImageBitmap(bitmap);
                    }
                })
                .create();
        tedBottomPicker.show(getSupportFragmentManager());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 0:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    showImageLibrary();
                }else {
                    Toast.makeText(TambahProdukActivity.this, "Izin ditolak, tidak dapat membuka Gallery", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void kirimData(final String id_user, final String id_kategori, final String nama, final String harga, final String deskripsi, final File photo){
        new HttpRequestData("http://rizmaulana.cloudapp.net/API/onlineshop/index.php/service/postproduk", new HttpRequestData.MultipartCallback() {
            @Override
            public void onPreExecuted() {
                progressDialog.setMessage("Mengirim data, harap tunggu ...");
                progressDialog.show();
            }

            @Override
            public MultipartUtility parameters(String url) {
                try {
                    MultipartUtility parameter = new MultipartUtility(url, "UTF-8", new HashMap<String, String>());
                    parameter.addFormField("id_user", id_user);
                    parameter.addFormField("id_kategori", id_kategori);
                    parameter.addFormField("nama", nama);
                    parameter.addFormField("harga", harga);
                    parameter.addFormField("deskripsi", deskripsi);
                    parameter.addFilePart("gambar", photo);
                    return  parameter;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onResponseServer(String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String result = jsonObject.getString("result");
                    if (result.equals("true")){
                        progressDialog.dismiss();
                        Toast.makeText(TambahProdukActivity.this, "Data berhasil dikirim", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(TambahProdukActivity.this, "Terjadi kesalahan server.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(TambahProdukActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }).execute();
    }
}
