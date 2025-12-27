package com.example.cravz.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cravz.Domain.SelectedAddress;
import com.example.cravz.databinding.ActivityPaymentBinding;

public class PaymentActivity extends AppCompatActivity {

    ActivityPaymentBinding binding;

    SelectedAddress selectedAddress;

    double subTotal = 0, tax = 0, delivery = 10, total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getPassedData();
        calculateCorrectValues();
        updateUI();
        handleClicks();
    }

    private void getPassedData() {

        selectedAddress = (SelectedAddress) getIntent().getSerializableExtra("selectedAddress");

        if (selectedAddress != null) {
            binding.tvSelectedAddress.setText(selectedAddress.getFullAddress());
        }

        // Might be passed
        subTotal = getIntent().getDoubleExtra("subTotal", 0);
        tax = getIntent().getDoubleExtra("tax", 0);
        total = getIntent().getDoubleExtra("totalAmount", 0);
    }

    private void calculateCorrectValues() {


        if (subTotal == 0 && total > 0) {



            subTotal = (total - delivery) / 1.1;
            tax = subTotal * 0.10;
        }

        else if (subTotal > 0) {
            tax = subTotal * 0.10;
            total = subTotal + tax + delivery;
        }
    }

    private void updateUI() {

        binding.tvSubtotal.setText("Subtotal: ₹" + Math.round(subTotal));
        binding.tvTax.setText("Tax: ₹" + Math.round(tax));
        binding.tvDelivery.setText("Delivery: ₹" + Math.round(delivery));
        binding.tvTotalAmount.setText("Total: ₹" + Math.round(total));
    }

    private void handleClicks() {

        binding.backBtn.setOnClickListener(v -> finish());

        binding.btnPayNow.setOnClickListener(v -> {

            int selectedId = binding.rgPayment.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Please select a payment option", Toast.LENGTH_SHORT).show();
                return;
            }

            // Payment option selected – move to success screen


            Intent intent = new Intent(PaymentActivity.this, SuccessActivity.class);
            startActivity(intent);

            // Close Payment screen
            finish();
        });
    }

}
