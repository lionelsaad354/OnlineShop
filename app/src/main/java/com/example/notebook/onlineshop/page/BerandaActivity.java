package com.example.notebook.onlineshop.page;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notebook.onlineshop.R;
import com.example.notebook.onlineshop.adapter.BerandaAdapter;
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

public class BerandaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ProdukModel> models = new ArrayList<>();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        initView();
        requestData();

    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        BerandaAdapter adapter = new BerandaAdapter(getApplicationContext(), models);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setOnItemClickListener(new BerandaAdapter.onItemClickListener() {
            @Override
            public void onClickItem(View view, int position) {
                //ketika item diklik
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("data", models.get(position));
                startActivity(intent);
            }
        });
    }

    private void requestData(){
        new HttpRequestData("http://rizmaulana.cloudapp.net/API/onlineshop/index.php/service/getberandadata", new HttpRequestData.MultipartCallback() {
            @Override
            public void onPreExecuted() {
                progressDialog = new ProgressDialog(BerandaActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Meminta data, harap tunggu ...");
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
                        models.clear();
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
                            models.add(produk);
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        progressDialog.dismiss();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(BerandaActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(BerandaActivity.this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beranda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                //ketika icon add di klik
                Intent intent = new Intent(getApplicationContext(), TambahProdukActivity.class);
                startActivity(intent);
                break;

            case R.id.action_profile:
                //ketika icon profil di klik
                Intent intent2 = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent2);
                break;
        }
        return true;
    }
}
