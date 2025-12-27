package com.example.cravz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cravz.Adapter.SelectAddressAdapter;
import com.example.cravz.Domain.SelectedAddress;
import com.example.cravz.R;
import com.example.cravz.UtilClass;
import com.example.cravz.databinding.ActivitySelectAddressBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SelectAddressActivity extends AppCompatActivity {

    private ActivitySelectAddressBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference userAddressesRef;
    private List<SelectedAddress> addressList;
    private SelectAddressAdapter adapter;
    private SelectedAddress selectedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        addressList = new ArrayList<>();
        adapter = new SelectAddressAdapter(this, addressList, address -> selectedAddress = address);

        binding.rvAddress.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAddress.setAdapter(adapter);

        binding.btnAddAddress.setOnClickListener(v -> {
            // navigate to add address screen (implement AddAddressActivity)
            UtilClass.screenNavigation(SelectAddressActivity.this, AddAddressActivity.class);
        });

        binding.btnContinuePayment.setOnClickListener(v -> {
            if (selectedAddress == null) {
                // try to retrieve from adapter if user previously selected
                selectedAddress = adapter.getSelectedAddress();
            }
            if (selectedAddress == null) {
                Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show();
                return;
            }

            double subTotal = getIntent().getDoubleExtra("subTotal", 0);

            Intent intent = new Intent(SelectAddressActivity.this, PaymentActivity.class);
            intent.putExtra("selectedAddress", selectedAddress);
            intent.putExtra("subTotal", subTotal);
            startActivity(intent);

        });

        loadAddresses();
    }

    private void loadAddresses() {
        if (mAuth.getCurrentUser() == null) return;
        String uid = mAuth.getCurrentUser().getUid();
        userAddressesRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(uid)
                .child("Address");

        userAddressesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snap = task.getResult();
                addressList.clear();
                for (DataSnapshot ds : snap.getChildren()) {
                    SelectedAddress a = ds.getValue(SelectedAddress.class);
                    if (a != null) {
                        // ensure id field (if not present set key)
                        if (a.getId() == null) a.setId(ds.getKey());
                        addressList.add(a);
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed loading addresses", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
