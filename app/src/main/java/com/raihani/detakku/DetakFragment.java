package com.raihani.detakku;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.ArrayList;

import android.app.DatePickerDialog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DetakFragment extends Fragment {

    private EditText edtStartDate, edtEndDate;
    private Button btnFilter;
    private LinearLayout paginationLayout;


    private ArrayList<RiwayatDetak> semuaData = new ArrayList<>();

    private ArrayList<RiwayatDetak> dataFilter = new ArrayList<>();

    // ===== pagination =====
    private int currentPage = 1;

    private final int ITEM_PER_PAGE = 10;

    // ===== View =====
    private RecyclerView recyclerRiwayat;
    private TextView txtEmpty;

    private RiwayatAdapter adapter;

    // ===== Firebase Firestore =====
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_detak,
                container,
                false
        );

        // ===== Inisialisasi View =====
        edtStartDate = view.findViewById(R.id.edt_start_date);
        edtEndDate = view.findViewById(R.id.edt_end_date);
        btnFilter = view.findViewById(R.id.btn_filter);

        edtStartDate.setOnClickListener(v -> {
            showDatePicker(edtStartDate);
        });

        edtEndDate.setOnClickListener(v -> {
            showDatePicker(edtEndDate);
        });

        paginationLayout = view.findViewById(
                R.id.paginationLayout
        );

        recyclerRiwayat =
                view.findViewById(R.id.recyclerRiwayat);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        // Layout vertikal
        recyclerRiwayat.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        // Adapter awal kosong
        adapter = new RiwayatAdapter(
                new ArrayList<>()
        );

        recyclerRiwayat.setAdapter(adapter);

        // ===== Inisialisasi Firestore =====
        db = FirebaseFirestore.getInstance();

        // ===== Ambil data riwayat =====
        loadRiwayatDetak();

        btnFilter.setOnClickListener(v -> {
            filterTanggal();
        });

        return view;
    }

    // ===== Ambil data dari Cloud Firestore =====
    private void loadRiwayatDetak() {

        db.collection("RiwayatDetak")
                .orderBy(
                        "Timestamp",
                        Query.Direction.DESCENDING
                )
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {


                    // Tambahkan data baru
                    semuaData.clear();

                    for (DocumentSnapshot document : queryDocumentSnapshots) {

                        Long bpmValue = document.getLong("BPM");
                        Long timestampValue = document.getLong("Timestamp");

                        if (bpmValue == null || timestampValue == null) {
                            continue;
                        }

                        int bpm = bpmValue.intValue();
                        long timestamp = timestampValue;

                        String status = document.getString("Status");

                        String waktu = document.getString("Waktu");


                        semuaData.add(
                                new RiwayatDetak(
                                        bpm,
                                        status,
                                        waktu,
                                        timestamp
                                )
                        );
                    }

                    // awalnya tampilkan semua
                    dataFilter.clear();
                    dataFilter.addAll(semuaData);

                    // halaman pertama
                    currentPage = 1;

                    tampilkanHalaman();
                    buatPagination();
                    cekDataKosong();

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            getContext(),
                            "Gagal mengambil data: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                });
    }

    private void tampilkanHalaman() {

        int start =
                (currentPage - 1) * ITEM_PER_PAGE;


        int end = Math.min(
                start + ITEM_PER_PAGE,
                dataFilter.size()
        );


        ArrayList<RiwayatDetak> dataHalaman =
                new ArrayList<>();


        for (int i = start; i < end; i++) {

            dataHalaman.add(
                    dataFilter.get(i)
            );
        }


        adapter.setData(dataHalaman);
    }

    private void filterTanggal() {

        String start = edtStartDate.getText()
                .toString()
                .trim();

        String end = edtEndDate.getText()
                .toString()
                .trim();

        // Validasi tanggal harus dipilih
        if (start.isEmpty() || end.isEmpty()) {

            Toast.makeText(
                    getContext(),
                    "Pilih rentang tanggal terlebih dahulu",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        try {

            SimpleDateFormat sdf =
                    new SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                    );

            long startTime =
                    sdf.parse(start).getTime() / 1000;

            // sampai akhir hari
            long endTime =
                    (sdf.parse(end).getTime() / 1000)
                            + 86399;

            // Validasi tanggal awal > tanggal akhir
            if (startTime > endTime) {

                Toast.makeText(
                        getContext(),
                        "Tanggal awal tidak boleh lebih besar dari tanggal akhir",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            dataFilter.clear();

            for (RiwayatDetak data : semuaData) {

                if (data.getTimestamp() >= startTime &&
                        data.getTimestamp() <= endTime) {

                    dataFilter.add(data);
                }
            }

            // Kembali ke halaman pertama
            currentPage = 1;

            // Refresh tampilan
            tampilkanHalaman();
            buatPagination();

            // Cek empty state
            cekDataKosong();

        } catch (Exception e) {

            Toast.makeText(
                    getContext(),
                    "Format tanggal tidak valid",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void cekDataKosong() {

        if (dataFilter.isEmpty()) {

            txtEmpty.setVisibility(View.VISIBLE);
            recyclerRiwayat.setVisibility(View.GONE);

        } else {

            txtEmpty.setVisibility(View.GONE);
            recyclerRiwayat.setVisibility(View.VISIBLE);
        }
    }

    private void showDatePicker(EditText editText) {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                (view, year, month, day) -> {

                    String tanggal = String.format(
                            Locale.getDefault(),
                            "%02d-%02d-%04d",
                            day,
                            month + 1,
                            year
                    );

                    editText.setText(tanggal);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void buatPagination() {

        paginationLayout.removeAllViews();

        int totalPage =
                (int) Math.ceil(
                        (double) dataFilter.size() / ITEM_PER_PAGE
                );

        // Jika hanya satu halaman, tidak perlu tampil
        if (totalPage <= 1) {
            paginationLayout.setVisibility(View.GONE);
            return;
        }

        paginationLayout.setVisibility(View.VISIBLE);


        // Tombol Previous
        tambahTombolPagination(
                "<",
                currentPage > 1,
                currentPage - 1
        );


        // Tombol nomor halaman
        for (int i = 1; i <= totalPage; i++) {

            final int halaman = i;

            tambahTombolPagination(
                    String.valueOf(i),
                    true,
                    halaman
            );
        }


        // Tombol Next
        tambahTombolPagination(
                ">",
                currentPage < totalPage,
                currentPage + 1
        );
    }

    private void tambahTombolPagination(
            String text,
            boolean enabled,
            int halaman
    ) {

        Button button = new Button(getContext());

        button.setText(text);


        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(8, 0, 8, 0);

        button.setLayoutParams(params);


        button.setEnabled(enabled);


        // Halaman aktif
        if (text.equals(String.valueOf(currentPage))) {

            button.setBackgroundColor(
                    getResources().getColor(R.color.darkRed)
            );

            button.setTextColor(
                    getResources().getColor(android.R.color.white)
            );

        } else {

            button.setBackgroundColor(
                    getResources().getColor(R.color.grey)
            );
        }


        button.setOnClickListener(v -> {
            if (!enabled) return;

            currentPage = halaman;

            tampilkanHalaman();

            buatPagination();

            // kembali ke atas
            recyclerRiwayat.scrollToPosition(0);

        });

        paginationLayout.addView(button);
    }

}