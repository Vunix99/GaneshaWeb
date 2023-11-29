package com.ganesha.diur;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ganesha.diur.ArtikelModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtikelNewAdapter extends RecyclerView.Adapter<ArtikelNewAdapter.ArtikelViewHolder> {

    private List<ArtikelModel> artikelList;
    private Context context;
    private int limit = Integer.MAX_VALUE;
    private OnItemClickListener onItemClickListener;

    public ArtikelNewAdapter(Context context, List<ArtikelModel> artikelList) {
        this.context = context;
        this.artikelList = artikelList;
    }

    @NonNull
    @Override
    public ArtikelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artikel_new, parent, false);
        return new ArtikelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtikelViewHolder holder, int position) {
        ArtikelModel artikel = artikelList.get(position);

        // Set image using Picasso
        Picasso.get().load(artikel.getGambar_artikel()).into(holder.imageView);

        // Set title
        holder.titleTextView.setText(artikel.getJudul());

        // Set OnClickListener untuk card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Panggil metode onItemClick dari listener jika ada
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(artikel.getId_artikel());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // Batasi jumlah item yang ditampilkan
        return Math.min(limit, artikelList.size());
    }

    // Setter untuk mengatur batasan jumlah artikel
    public void setLimit(int limit) {
        this.limit = limit;
        notifyDataSetChanged();
    }

    // Interface untuk menangani aksi klik pada item
    public interface OnItemClickListener {
        void onItemClick(String idArtikel);
    }

    // Setter untuk listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ArtikelViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView;

        public ArtikelViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_artikel_new01);
            titleTextView = itemView.findViewById(R.id.judul_artikel_new01);
        }
    }
}

