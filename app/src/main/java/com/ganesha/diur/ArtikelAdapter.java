package com.ganesha.diur;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder> {
    private Context context;
    private List<ArtikelModel> artikelList;

    public ArtikelAdapter(Context context, List<ArtikelModel> artikelList) {
        this.context = context;
        this.artikelList = artikelList;
    }

    @NonNull
    @Override
    public ArtikelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artikel, parent, false);
        return new ArtikelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtikelViewHolder holder, int position) {
        ArtikelModel artikel = artikelList.get(position);

        // Menggunakan Picasso untuk menampilkan gambar dari URL
        Picasso.get()
                .load(artikel.getGambar_artikel())
                .into(holder.imageArtikel);

        holder.titleArtikel.setText(artikel.getJudul());

        // Ambil 7 kata pertama dari isi_artikel dan tambahkan titik sebanyak 3 di belakangnya
        String isiArtikel = artikel.getIsi_artikel();
        String[] words = isiArtikel.split("\\s+");

        StringBuilder shortDescription = new StringBuilder();
        int maxWords = Math.min(words.length, 5);
        for (int i = 0; i < maxWords; i++) {
            shortDescription.append(words[i]).append(" ");
        }

        if (maxWords < words.length) {
            shortDescription.append("...");
        }

        holder.deskripsiSingkat.setText(shortDescription.toString().trim());

        // Atur onClickListener untuk tombol "Read More"
        holder.buttonReadArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implementasi aksi ketika tombol "Read More" diklik
                // Contohnya, membuka aktivitas baru (DetailArtikelActivity)
                openDetailArtikelActivity(artikel.getId_artikel());
            }
        });
    }

    @Override
    public int getItemCount() {
        return artikelList.size();
    }

    public class ArtikelViewHolder extends RecyclerView.ViewHolder {
        ImageView imageArtikel;
        TextView titleArtikel;
        TextView deskripsiSingkat;
        Button buttonReadArtikel;

        public ArtikelViewHolder(@NonNull View itemView) {
            super(itemView);

            imageArtikel = itemView.findViewById(R.id.imageArtikel);
            titleArtikel = itemView.findViewById(R.id.title_artikel);
            deskripsiSingkat = itemView.findViewById(R.id.deskripsi_singkat);
            buttonReadArtikel = itemView.findViewById(R.id.buttonReadArtikel);
        }
    }

    // Metode untuk membuka DetailArtikelActivity dan mengirim ID artikel
    private void openDetailArtikelActivity(String artikelId) {
        Intent intent = new Intent(context, detail_artikel.class);
        intent.putExtra("ARTIKEL_ID", artikelId);
        context.startActivity(intent);
    }
}
