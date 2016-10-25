package com.example.notebook.onlineshop.page;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook.onlineshop.R;
import com.example.notebook.onlineshop.adapter.OwnerAdapter;
import com.example.notebook.onlineshop.helper.PreferencesManager;
import com.example.notebook.onlineshop.model.ProdukModel;
import com.example.notebook.onlineshop.network.HttpRequestData;
import com.example.notebook.onlineshop.network.MultipartUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewUsername, textViewLogout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String id_user;

    List<ProdukModel> produkModelLis = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        id_user = PreferencesManager.getPreferanceValue(getApplicationContext(), PreferencesManager.userId);
        requestProduk(id_user);
    }

    private void initView(){
        textViewUsername = (TextView) findViewById(R.id.text_username);
        textViewLogout = (TextView) findViewById(R.id.text_logout);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        String username = PreferencesManager.getPreferanceValue(getApplicationContext(), PreferencesManager.username);
        textViewUsername.setText(username);

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle ketika logout
                logout();
            }
        });

        OwnerAdapter adapter = new OwnerAdapter(getApplicationContext(), produkModelLis);
        adapter.setOnItemClickListener(new OwnerAdapter.onItemClickListener() {
            @Override
            public void onClickItem(View view, int position) {
                //handle ketika item di klik
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("data", produkModelLis.get(position));
                startActivity(intent);
            }

            @Override
            public void onDeleteItem(View view, int position) {
                //hanle ketika tombol delet di klik
                String id_produk = produkModelLis.get(position).getId_produk();
                deleteProduk(id_user, id_produk, position);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    private void requestProduk(final String id_user){
        new HttpRequestData("http://rizmaulana.cloudapp.net/API/onlineshop/index.php/service/getownerdata", new HttpRequestData.MultipartCallback() {
            @Override
            public void onPreExecuted() {
                progressDialog = new ProgressDialog(ProfileActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Meminta data, harap tunggu ...");
                progressDialog.show();
            }

            @Override
            public MultipartUtility parameters(String url) {
                try {
                    MultipartUtility parameter = new MultipartUtility(url, "UTF-8", new HashMap<String, String>());
                    parameter.addFormField("id_user", id_user);
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
                        produkModelLis.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("produk");
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            ProdukModel produk = new ProdukModel();
                            produk.setId_produk(object.getString("id_produk"));
                            produk.setKategoti_nama(object.getString("kategori_nama"));
                            produk.setProduk_nama(object.getString("produk_nama"));
                            produk.setProduk_harga(object.getString("produk_harga"));
                            produk.setProduk_gambar(object.getString("produk_gambar"));
                            produk.setUser_name(object.getString("user_name"));
                            produk.setProduk_deskripsi(object.getString("produk_deskripsi"));
                            produkModelLis.add(produk);
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        progressDialog.dismiss();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Tidak ada data dteukan.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }).execute();
    }


    private void deleteProduk(final String id_user, final String id_produk, final int position){
        new HttpRequestData("http://rizmaulana.cloudapp.net/API/onlineshop/index.php/service/deleteproduk", new HttpRequestData.MultipartCallback() {
            @Override
            public void onPreExecuted() {
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Menghapus item harap tunggu ...");
                progressDialog.show();
            }

            @Override
            public MultipartUtility parameters(String url) {
                try {
                    MultipartUtility parameter = new MultipartUtility(url, "UTF-8", new HashMap<String, String>());
                    parameter.addFormField("id_user", id_user);
                    parameter.addFormField("id_produk", id_produk);
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
                        produkModelLis.remove(position);
                        recyclerView.getAdapter().notifyItemRemoved(position);
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Tidak ada data dteukan.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }).execute();
    }

    private void logout(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
        alertDialog.setMessage("Apakah Anda yakin akan keluar?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                PreferencesManager.setPreferenceValue(getApplicationContext(), PreferencesManager.loginStatus, "false");

                Intent i = getPackageManager()
                        .getLaunchIntentForPackage(getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.create().show();
    }
}
