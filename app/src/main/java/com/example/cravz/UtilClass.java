package com.example.cravz;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UtilClass {
public static void screenNavigation(Context context, Class<?> destination){
    Intent intent = new Intent(context,destination);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    context.startActivity(intent);
}

    public static void setUpCustomToolbar(AppCompatActivity activity){
        Toolbar toolbar = activity.findViewById(R.id.toolbar_custom);
        activity.setSupportActionBar(toolbar);
        activity.setTitle("Cravz");

        ActionBar actionBar = activity.getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> {
            activity.getOnBackPressedDispatcher().onBackPressed();
            activity.finish();
        });
    }
}
