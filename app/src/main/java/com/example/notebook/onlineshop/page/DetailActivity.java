package com.example.notebook.onlineshop.page;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.notebook.onlineshop.R;
import com.example.notebook.onlineshop.model.ProdukModel;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView textSeller, textTitle, textHarga, textDescription;
        ImageView imageView;
        textHarga = (TextView) findViewById(R.id.text_harga);
        textSeller = (TextView) findViewById(R.id.text_seller);
        textTitle = (TextView) findViewById(R.id.text_title);
        textDescription = (TextView) findViewById(R.id.text_description);
        imageView = (ImageView) findViewById(R.id.img_product);

        ProdukModel data = getIntent().getParcelableExtra("data");

        textSeller.setText(data.getUser_name());
        textTitle.setText(data.getProduk_nama());
        textDescription.setText(data.getProduk_deskripsi());
        textHarga.setText(data.getProduk_harga());;

        Glide.with(getApplicationContext())
                .load("http://rizmaulana.cloudapp.net/API/onlineshop/images/"+data.getProduk_gambar())
                .centerCrop()
                .placeholder(R.drawable.thumb)
                .crossFade()
                .into(imageView);
    }
}
