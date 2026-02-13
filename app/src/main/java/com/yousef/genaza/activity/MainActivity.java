package com.yousef.genaza.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.Timestamp;
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
    private List<Dead> allList, list;
    private String uid;
    private Dead dead;
    private String search ="";

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
        uid =repository.getString(UID, "");
        if(uid.isEmpty())
            repository.putString(UID, repository.generateRandomID());

        binding.hijriDate.setText(repository.getHijriDate());

        // طلب إذن الإشعارات (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
            else
                startService();
        }


        dead = new Dead();
        allList = new ArrayList<>();
        list = new ArrayList<>();
        adapter = new DeadAdapter(this, list, search,this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repository.getDead(this);

        binding.add.setOnClickListener(v -> repository.setIntent(AddActivity.class));

        binding.myItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddActivity.class);
            intent.putExtra(DEAD_ID, dead.getId());
            startActivity(intent);
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search = newText.trim();
                filter();
                return false;
            }
        });
    }
    private void startService(){
        repository.subscribeToTopic();
        repository.createNotificationChannel();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService();
                Toast.makeText(this, "تم السماح بالإشعارات", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "لم يتم السماح بالإشعارات", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void getItems(List<Dead> items) {
        allList.clear();
        allList.addAll(items);
        filter();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filter(){
        list.clear();
        Timestamp now = Timestamp.now();
        boolean hasMyPending = false;

        for (Dead item : allList) {
            if(item.getStatus() == -1 && uid.equals(item.getUid())){
                dead = item;
                hasMyPending = true;
                continue;
            }

            if(item.getStatus() == 1 && item.getTimestamp() != null && item.getTimestamp().compareTo(now) >= 0){
                if(search.isEmpty())
                    list.add(item);
                else if(item.getName().contains(search))
                    list.add(item);
            }
        }

        binding.myItem.setVisibility(hasMyPending ? View.VISIBLE : View.GONE);

        if(list.isEmpty()){
            binding.noDataAvailable.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.noDataAvailable.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged(search);
        binding.searchCard.setVisibility(View.VISIBLE);
        binding.progressContainer.setVisibility(View.GONE);
    }

    @Override
    public void clickItem(Dead item) {
        Intent intent = new Intent(this, ViewImageActivity.class);
        intent.putExtra(PHOTO, item.getZPhoto());
        intent.putExtra(NAME, item.getName());
        startActivity(intent);
    }


    @Override
    public void failGetItems(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        System.out.println(error);
    }
}