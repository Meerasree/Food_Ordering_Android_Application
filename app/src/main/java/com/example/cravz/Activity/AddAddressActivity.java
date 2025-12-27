package com.example.cravz.Activity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.cravz.Domain.SelectedAddress;

import com.example.cravz.UtilClass;
import com.example.cravz.databinding.ActivityAddAddressBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAddressActivity extends BaseActivity {

    ActivityAddAddressBinding binding;
    FirebaseAuth auth;
    DatabaseReference addressRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        addressRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(auth.getUid())
                .child("Address");

        binding.btnConfirmAddress.setOnClickListener(v -> saveAddress());
    }

    private void saveAddress() {

        String name = binding.etAddName.getText().toString().trim();
        String address = binding.etFullAddress.getText().toString().trim();
        String city = binding.etAddCity.getText().toString().trim();
        String postal = binding.etAddPostalCode.getText().toString().trim();
        String phone = binding.etAddPhoneNumber.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || city.isEmpty() || postal.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SelectedAddress model = new SelectedAddress(name, address, city, postal, phone);

        String key = addressRef.push().getKey();

        addressRef.child(key).setValue(model).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Address Added Successfully", Toast.LENGTH_SHORT).show();
                UtilClass.screenNavigation(this, SelectAddressActivity.class);
                finish();
            }
        });
    }
}
