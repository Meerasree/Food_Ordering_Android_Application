package com.example.cravz.Activity;

import static com.example.cravz.UtilClass.screenNavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.cravz.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
         navigationOfViews();    }

    private void navigationOfViews() {
        binding.loginbtn.setOnClickListener(v -> {
            checkLoginValidation();
        });
        binding.tvSignUp.setOnClickListener(v -> {
            screenNavigation(LoginActivity.this, SignUpActivity.class);
            finish();
        });
    }
    private void checkLoginValidation() {
        String userEmail = Objects.requireNonNull(binding.userEdt.getText()).toString();
        String userPassword = Objects.requireNonNull(binding.passEdt.getText()).toString();

        if(!userEmail.isEmpty() && !userPassword.isEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    screenNavigation(LoginActivity.this, MainActivity.class);
                    finish();
                }

                else{
                    Toast.makeText(LoginActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this, "Please fill Email and Password", Toast.LENGTH_SHORT).show();
        }
    }
}