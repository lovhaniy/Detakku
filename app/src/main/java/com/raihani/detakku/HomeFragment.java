package com.raihani.detakku;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    // ===== View =====
    private HeartRateGaugeView heartRateGauge;
    private TextView tvGreeting, tvWelcome;
    private ImageView btnNotif;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // ===== Inisialisasi View =====
        tvGreeting = view.findViewById(R.id.tvGreeting);
        tvWelcome  = view.findViewById(R.id.tvWelcome);
        btnNotif   = view.findViewById(R.id.btnNotif);
        heartRateGauge = view.findViewById(R.id.heartRateGauge);

        // ===== Contoh nilai BPM (dummy) =====
        int bpm = 92;

        // Set ke Gauge
        heartRateGauge.setBpm(bpm);

        // (Opsional) klik notifikasi
        btnNotif.setOnClickListener(v -> {
            // TODO: buka halaman notifikasi
        });

        return view;
    }
}
