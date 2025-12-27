package com.example.cravz.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.cravz.Adapter.FoodListAdapter;
import com.example.cravz.Domain.Foods;
import com.example.cravz.databinding.ActivityListFoodsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodsActivity extends BaseActivity {
    ActivityListFoodsBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentExtra();
        initList();
        setVariable();
    }

    private void setVariable() {
    }

    private void initList() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBar.setVisibility(android.view.View.VISIBLE);
        ArrayList<Foods> list = new ArrayList<>();

        Query query;
        if (isSearch) {
            query = myRef.orderByChild("Title")
                    .startAt(searchText)
                    .endAt(searchText + '\uf8ff');
        } else {
            // Prefer server-side filtering if CategoryId values in DB are numeric (they are).
            // Pass as Long to be safe (Realtime DB stores numbers as Long).
            query = myRef.orderByChild("CategoryId").equalTo((long) categoryId);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                // If snapshot is null / doesn't exist, fall back to fetching all and manually filtering.
                if (!snapshot.exists()) {
                    Log.d("LIST_FOODS", "Query returned no children — falling back to client-side filter (fetch all).");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot allSnap) {
                            list.clear();
                            if (allSnap.exists()) {
                                for (DataSnapshot issue : allSnap.getChildren()) {
                                    Foods food = issue.getValue(Foods.class);
                                    if (food == null) continue;
                                    // filter client-side
                                    if (food.getCategoryId() == categoryId) {
                                        list.add(food);
                                    }
                                }
                            }
                            showList(list);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            binding.progressBar.setVisibility(android.view.View.GONE);
                        }
                    });
                    return;
                }

                // Normal path: snapshot contains matching children (server-side filtered)
                for (DataSnapshot issue : snapshot.getChildren()) {
                    Foods food = issue.getValue(Foods.class);
                    if (food == null) continue;
                    list.add(food);
                }

                showList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(android.view.View.GONE);
            }
        });
    }

    private void showList(ArrayList<Foods> list) {
        binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this, 2));
        adapterListFood = new FoodListAdapter(list);
        binding.foodListView.setAdapter(adapterListFood);
        binding.progressBar.setVisibility(android.view.View.GONE);
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId", 0);
        categoryName = getIntent().getStringExtra("CategoryName");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);

        binding.titleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());

        Log.d("LIST_FOODS", "Opened ListFoodsActivity for CategoryId = " + categoryId + ", isSearch=" + isSearch);
    }
}
