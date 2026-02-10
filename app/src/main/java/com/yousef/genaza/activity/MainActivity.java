package com.yousef.genaza.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.yousef.genaza.R;
import com.yousef.genaza.database.Repository;
import com.yousef.genaza.databinding.ActivityMainBinding;
import com.yousef.genaza.listener.Constants;

public class MainActivity extends AppCompatActivity implements Constants {

    private Repository repository;
    private ActivityMainBinding binding;
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

        Repository repository = new Repository(this);
        if(repository.getString(ID_USER, "").isEmpty())
            repository.putString(ID_USER, repository.generateRandomID());

    }
}