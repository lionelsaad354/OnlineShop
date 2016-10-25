package com.example.notebook.onlineshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.notebook.onlineshop.R;
import com.example.notebook.onlineshop.model.ProdukModel;

import java.util.List;

/**
 * Created by NOTEBOOK on 18/10/2016.
 */

public class OwnerAdapter extends RecyclerView.Adapter<OwnerAdapter.MyViewHolder> {
    private List<ProdukModel> models;
    private Context context;
    private OwnerAdapter.onItemClickListener onItemClickListener;


    public OwnerAdapter(Context context, List<ProdukModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public OwnerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_owner, parent, false);
        return new OwnerAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final OwnerAdapter.MyViewHolder holder, final int position) {
        ProdukModel data = models.get(position);

        holder.textTitle.setText(data.getProduk_nama());
        holder.textDescription.setText(data.getProduk_deskripsi());
        holder.textHarga.setText(data.getProduk_harga());;
        Glide.with(context)
                .load("http://rizmaulana.cloudapp.net/API/onlineshop/images/"+data.getProduk_gambar())
                .centerCrop()
                .placeholder(R.drawable.thumb)
                .crossFade()
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle, textHarga, textDescription, textHapus;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClickItem(view, getAdapterPosition());
                }
            });
            textHarga = (TextView) itemView.findViewById(R.id.text_harga);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            textDescription = (TextView) itemView.findViewById(R.id.text_description);
            imageView = (ImageView) itemView.findViewById(R.id.img_product);
            textHapus = (TextView) itemView.findViewById(R.id.text_hapus);
            textHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onDeleteItem(view, getAdapterPosition());
                }
            });

        }
    }

    public void setOnItemClickListener(OwnerAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface onItemClickListener {
        void onClickItem(View view, int position);

        void onDeleteItem(View view, int position);
    }
}
