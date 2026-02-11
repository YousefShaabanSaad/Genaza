package com.yousef.genaza.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.yousef.genaza.R;
import com.yousef.genaza.database.Repository;
import com.yousef.genaza.databinding.ActivityAddBinding;
import com.yousef.genaza.listener.Constants;
import com.yousef.genaza.listener.ItemListener;
import com.yousef.genaza.models.Dead;

public class AddActivity extends AppCompatActivity implements Constants, ItemListener<Dead> {

    private ActivityAddBinding binding;
    private Dead dead;
    private Repository repository;
    private Dialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new Repository(this);
        dead = new Dead();
        dead.setId("");
        dead.setStatus(-1);
        progress = repository.createProgress();

        if (getIntent().getExtras() != null) {
            progress.show();
            repository.getDeadByID(getIntent().getExtras().getString(DEAD_ID, ""), this);
        }
        else
            binding.main.setVisibility(View.VISIBLE);

        binding.card.setOnClickListener(v ->
                ImagePicker.with(this)
                        .crop(3f, 2f)
                        .compress(512)
                        .maxResultSize(900, 600)
                        .start()
        );
        binding.send.setOnClickListener(v -> save());
    }

    private void save() {
        progress.show();
        dead.setUid(repository.getString(UID, ""));
        dead.setName(binding.name.getText().toString());
        dead.setPhone(binding.phone.getText().toString());
        dead.setTimePlace(binding.timePlace.getText().toString());
        dead.setNotes(binding.notes.getText().toString());
        repository.addOrEditDead(dead, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            try {
                assert data != null;
                Uri uri = data.getData();
                binding.photo.setImageURI(uri);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                dead.setPhoto(repository.bitmapToBase64(bitmap));
            } catch (Exception error){
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void getItem(Dead item) {
        dead = item;
        Bitmap bmp = dead.getPhotoBitmap();
        if(bmp != null){
            binding.photo.setImageBitmap(bmp);
        }else{
            binding.photo.setImageResource(R.drawable.bg_image);
        }

        binding.name.setText(dead.getName() != null ? dead.getName() : "");
        binding.phone.setText(dead.getPhone() != null ? dead.getPhone() : "");
        binding.timePlace.setText(dead.getTimePlace() != null ? dead.getTimePlace() : "");
        binding.notes.setText(dead.getNotes() != null ? dead.getNotes() : "");

        binding.send.setText(getString(R.string.edit));
        progress.dismiss();
        binding.main.setVisibility(View.VISIBLE);
    }

    @Override
    public void failGetItem(String error) {
        progress.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void successAddOrEditItem() {
        progress.dismiss();
        Toast.makeText(this, getString(R.string.sendDone), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void failAddOrEditItem(String error) {
        progress.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}