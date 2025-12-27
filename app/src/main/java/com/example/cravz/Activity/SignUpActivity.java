package com.example.cravz.Activity;

import static com.example.cravz.UtilClass.screenNavigation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cravz.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        navigationOfViews();
    }
    private void navigationOfViews() {
        binding.signbtn.setOnClickListener(v -> {
            signUpValidation();
        });
      binding.tvLogin.setOnClickListener(v -> {
          screenNavigation(SignUpActivity.this, LoginActivity.class);
      });
    }

    private void signUpValidation() {
        String userEmail = Objects.requireNonNull(binding.etEmail.getText()).toString();
        String userPassword = Objects.requireNonNull(binding.etPassword.getText()).toString();
        if ( !userEmail.isEmpty() && !userPassword.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                    screenNavigation(SignUpActivity.this, MainActivity.class);
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(SignUpActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();

        }
    }

}