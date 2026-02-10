package com.yousef.genaza.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.yousef.genaza.R;
import com.yousef.genaza.adapter.DeadAdapter;
import com.yousef.genaza.database.Repository;
import com.yousef.genaza.databinding.ActivityMainBinding;
import com.yousef.genaza.listener.Constants;
import com.yousef.genaza.listener.ItemsListener;
import com.yousef.genaza.models.Dead;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Constants, ItemsListener<Dead> {

    private Repository repository;
    private ActivityMainBinding binding;
    private DeadAdapter adapter;
    private List<Dead> list;
    private String idOwner;
    private Dead dead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new Repository(this);
        idOwner =repository.getString(ID_USER, "");
        if(idOwner.isEmpty())
            repository.putString(ID_USER, repository.generateRandomID());

        dead = new Dead();
        list = new ArrayList<>();
        adapter = new DeadAdapter(this, list);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repository.getDead(this);

        binding.add.setOnClickListener(v -> repository.setIntent(AddActivity.class));

        binding.myItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddActivity.class);
            intent.putExtra(DEAD, dead);
            startActivity(intent);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void getItems(List<Dead> items) {
        list.clear();
        list.addAll(items);
        if (list.isEmpty()){
            binding.noDataAvailable.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }
        else {
            boolean found = false;
            for (Dead item : list) {
                if (item.getIdOwner().equals(idOwner)) {
                    found = true;
                    dead = item;
                    break;
                }
            }
            binding.myItem.setVisibility(found ? View.VISIBLE : View.GONE);
            binding.noDataAvailable.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
        binding.progress.setVisibility(View.GONE);
    }

    @Override
    public void failGetItems(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}