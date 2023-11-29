package com.ganesha.diur;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ResepViewHolder> {
    private List<ResepModel> resepList;
    private Context context;

    public ResepAdapter(List<ResepModel> resepList, Context context) {
        this.resepList = resepList;
        this.context = context;
    }

    @NonNull
    @Override
    public ResepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resep, parent, false);
        return new ResepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResepViewHolder holder, int position) {
        ResepModel currentResep = resepList.get(position);

        holder.titleResep.setText(currentResep.getJudul_resep());
        holder.detailCalorie.setText(currentResep.getJumlah_kalori_resep() + " kcal");

        // Memuat gambar dari URL menggunakan Picasso
        Picasso.get()
                .load(currentResep.getGambar_resep())
                .placeholder(R.drawable.image_resep_01)
                .into(holder.imageResep);

        // Menambahkan onClickListener ke buttonCobaResep
        holder.buttonCobaResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengambil id_resep dari objek ResepModel
                String idResep = currentResep.getId_resep();
                String namaResep = currentResep.getJudul_resep();

                // Log untuk memeriksa nilai id_resep
                Log.d("ResepAdapter", "ID Resep: " + idResep);

                Intent intent = new Intent(view.getContext(), detail_resep.class);
                intent.putExtra("id_resep", idResep);
                intent.putExtra("nama_resep", namaResep);
                view.getContext().startActivity(intent);

                Toast.makeText(context, "ID Resep : " + idResep, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return resepList.size();
    }

    // Metode untuk memperbarui data set pada adapter
    public void setResepList(List<ResepModel> resepList) {
        this.resepList = resepList;
        notifyDataSetChanged();
    }

    public static class ResepViewHolder extends RecyclerView.ViewHolder {
        ImageView imageResep;
        TextView titleResep;
        TextView detailCalorie;
        Button buttonCobaResep;

        public ResepViewHolder(@NonNull View itemView) {
            super(itemView);
            imageResep = itemView.findViewById(R.id.imageResep);
            titleResep = itemView.findViewById(R.id.title_resep);
            detailCalorie = itemView.findViewById(R.id.detail_calorie);
            buttonCobaResep = itemView.findViewById(R.id.buttonCobaResep);
        }
    }
}
