package com.example.notebook.onlineshop.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NOTEBOOK on 18/10/2016.
 */

public class ProdukModel implements Parcelable {
    String id_produk;
    String produk_nama;
    String produk_harga;
    String produk_deskripsi;
    String produk_gambar;
    String user_name;
    String kategoti_nama;

    public ProdukModel(){}

    protected ProdukModel(Parcel in) {
        id_produk = in.readString();
        produk_nama = in.readString();
        produk_harga = in.readString();
        produk_deskripsi = in.readString();
        produk_gambar = in.readString();
        user_name = in.readString();
        kategoti_nama = in.readString();
    }

    public static final Creator<ProdukModel> CREATOR = new Creator<ProdukModel>() {
        @Override
        public ProdukModel createFromParcel(Parcel in) {
            return new ProdukModel(in);
        }

        @Override
        public ProdukModel[] newArray(int size) {
            return new ProdukModel[size];
        }
    };

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getProduk_nama() {
        return produk_nama;
    }

    public void setProduk_nama(String produk_nama) {
        this.produk_nama = produk_nama;
    }

    public String getProduk_harga() {
        return produk_harga;
    }

    public void setProduk_harga(String produk_harga) {
        this.produk_harga = produk_harga;
    }

    public String getProduk_deskripsi() {
        return produk_deskripsi;
    }

    public void setProduk_deskripsi(String produk_deskripsi) {
        this.produk_deskripsi = produk_deskripsi;
    }

    public String getProduk_gambar() {
        return produk_gambar;
    }

    public void setProduk_gambar(String produk_gambar) {
        this.produk_gambar = produk_gambar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getKategoti_nama() {
        return kategoti_nama;
    }

    public void setKategoti_nama(String kategoti_nama) {
        this.kategoti_nama = kategoti_nama;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id_produk);
        parcel.writeString(produk_nama);
        parcel.writeString(produk_harga);
        parcel.writeString(produk_deskripsi);
        parcel.writeString(produk_gambar);
        parcel.writeString(user_name);
        parcel.writeString(kategoti_nama);
    }
}
