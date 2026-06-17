package com.raihani.detakku;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private CircularProgressIndicator circularBpm;
    private DatabaseReference detakRef;
    private TextView txtBpm;
    private TextView txtStatus;

    private TextView tvGreeting;
    private TextView tvWelcome;

    private ImageView btnNotif;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_home,
                container,
                false
        );

        tvGreeting = view.findViewById(R.id.tvGreeting);
        tvWelcome = view.findViewById(R.id.tvWelcome);
        btnNotif = view.findViewById(R.id.btnNotif);

        circularBpm = view.findViewById(R.id.circularBpm);
        txtBpm = view.findViewById(R.id.txtBpm);
        txtStatus = view.findViewById(R.id.txtStatus);

        DatabaseReference detakRef =
                FirebaseDatabase.getInstance()
                        .getReference("Detakku/Detak");

        detakRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Integer bpm =
                        snapshot.child("BPM")
                                .getValue(Integer.class);

                String status =
                        snapshot.child("Status")
                                .getValue(String.class);

                if (bpm != null && !"No Finger".equals(status)) {
                    updateBpm(bpm);
                }

                if ("No Finger".equals(status)) {

                    txtBpm.setText("--");
                    txtStatus.setText("Tidak Terdeteksi");
                    circularBpm.setProgress(0);

                } else if ("Measuring".equals(status)) {

                    txtStatus.setText("Sedang Mengukur");

                } else {

                    txtStatus.setText(status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Contoh BPM awal
        updateBpm(72);

        btnNotif.setOnClickListener(v -> {
            // TODO
        });

        return view;
    }

    private void updateBpm(int bpm) {

        txtBpm.setText(String.valueOf(bpm));

        circularBpm.setMax(150);
        circularBpm.setProgress(bpm);

        if (bpm < 60) {

            txtStatus.setText("Rendah");

            circularBpm.setIndicatorColor(
                    ContextCompat.getColor(
                            requireContext(),
                            R.color.slow_color
                    )
            );

        } else if (bpm <= 100) {

            txtStatus.setText("Normal");

            circularBpm.setIndicatorColor(
                    ContextCompat.getColor(
                            requireContext(),
                            R.color.normal_color
                    )
            );

        } else {

            txtStatus.setText("Tinggi");

            circularBpm.setIndicatorColor(
                    ContextCompat.getColor(
                            requireContext(),
                            R.color.fast_color
                    )
            );
        }
    }
}