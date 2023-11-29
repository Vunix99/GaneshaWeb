package com.ganesha.diur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RiwayatItem> riwayatList;
    private Context context;

    public RiwayatAdapter(List<RiwayatItem> riwayatList, Context context) {
        this.riwayatList = riwayatList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case RiwayatItem.TYPE_ITEM:
                view = inflater.inflate(R.layout.item_card_riwayat, parent, false);
                return new RiwayatViewHolder(view);

            case RiwayatItem.TYPE_HEADER:
                view = inflater.inflate(R.layout.item_riwayat_header, parent, false);
                return new RiwayatHeaderViewHolder(view);

            default:
                throw new RuntimeException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();

        if (viewType == RiwayatItem.TYPE_ITEM && holder instanceof RiwayatViewHolder) {
            RiwayatModel riwayatModel = (RiwayatModel) riwayatList.get(position);
            ((RiwayatViewHolder) holder).bind(riwayatModel);
        } else if (viewType == RiwayatItem.TYPE_HEADER && holder instanceof RiwayatHeaderViewHolder) {
            RiwayatHeaderModel headerModel = (RiwayatHeaderModel) riwayatList.get(position);
            ((RiwayatHeaderViewHolder) holder).bind(headerModel);
        } else {
            // Handle unexpected view type or ViewHolder type
            throw new RuntimeException("Invalid view or ViewHolder type");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return riwayatList.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }

    public class RiwayatViewHolder extends RecyclerView.ViewHolder {

        public TextView jamKonsumsiTextView;
        public TextView tanggalKonsumsiTextView;
        public TextView judulResepTextView;
        public ImageView gambarResepImageView;
        public TextView jumlahKaloriResepTextView;

        public RiwayatViewHolder(@NonNull View itemView) {
            super(itemView);
            jamKonsumsiTextView = itemView.findViewById(R.id.jam_konsumsi);
            tanggalKonsumsiTextView = itemView.findViewById(R.id.tanggal_konsumsi);
            judulResepTextView = itemView.findViewById(R.id.judul_resep);
            gambarResepImageView = itemView.findViewById(R.id.image_riwayat);
            jumlahKaloriResepTextView = itemView.findViewById(R.id.riwayat_kalori_resep);
        }

        public void bind(RiwayatModel riwayatModel) {
            jamKonsumsiTextView.setText(riwayatModel.getJamKonsumsi());
            tanggalKonsumsiTextView.setText(riwayatModel.getTanggalKonsumsi());
            judulResepTextView.setText(riwayatModel.getJudulResep());
            jumlahKaloriResepTextView.setText(String.valueOf("+"+riwayatModel.getJumlahKaloriResep() + " Kcal"));

            // Load image using Picasso library
            Picasso.get().load(riwayatModel.getGambarResep()).into(gambarResepImageView);
        }
    }

    public class RiwayatHeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView tanggalRiwayatTextView;
        public TextView totalRiwayatTextView;

        public RiwayatHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tanggalRiwayatTextView = itemView.findViewById(R.id.tanggal_riwayat);
            totalRiwayatTextView = itemView.findViewById(R.id.total_riwayat);
        }

        public void bind(RiwayatHeaderModel headerModel) {
            tanggalRiwayatTextView.setText(headerModel.getTanggal());
            totalRiwayatTextView.setText(String.valueOf("+"+headerModel.getTotalKalori() + " Kcal"));
        }
    }
}
