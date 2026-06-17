package com.raihani.detakku;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RiwayatAdapter
        extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {

    private ArrayList<RiwayatDetak> listRiwayat;

    public RiwayatAdapter(ArrayList<RiwayatDetak> listRiwayat) {
        this.listRiwayat = listRiwayat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(
                parent.getContext()
        ).inflate(
                R.layout.item_riwayat_detak,
                parent,
                false
        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        RiwayatDetak data = listRiwayat.get(position);

        // BPM
        holder.tvBpm.setText(
                data.getBpm() + " BPM"
        );

        // Status
        holder.tvStatus.setText(
                data.getStatus()
        );

        // Warna status
        switch (data.getStatus()) {

            case "Normal":
                holder.tvStatus.setTextColor(
                        Color.parseColor("#4CAF50")
                );
                break;

            case "Lambat":
                holder.tvStatus.setTextColor(
                        Color.parseColor("#FFC107")
                );
                break;

            case "Tinggi":
                holder.tvStatus.setTextColor(
                        Color.parseColor("#F44336")
                );
                break;

            default:
                holder.tvStatus.setTextColor(
                        Color.BLACK
                );
        }

        // Pecah tanggal dan jam
        String waktu = data.getWaktu();

        if (waktu != null && waktu.contains(" ")) {

            String[] bagian = waktu.split(" ");

            holder.tvTanggal.setText(
                    bagian[0]
            );

            holder.tvJam.setText(
                    bagian[1]
            );

        } else {

            holder.tvTanggal.setText(waktu);

            holder.tvJam.setText("");
        }
    }

    @Override
    public int getItemCount() {

        return listRiwayat.size();
    }

    public void setData(ArrayList<RiwayatDetak> dataBaru) {

        listRiwayat.clear();

        listRiwayat.addAll(dataBaru);

        notifyDataSetChanged();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvBpm;
        TextView tvStatus;
        TextView tvTanggal;
        TextView tvJam;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvBpm = itemView.findViewById(R.id.tvBpm);

            tvStatus = itemView.findViewById(R.id.tvStatus);

            tvTanggal = itemView.findViewById(R.id.tvTanggal);

            tvJam = itemView.findViewById(R.id.tvJam);
        }
    }
}