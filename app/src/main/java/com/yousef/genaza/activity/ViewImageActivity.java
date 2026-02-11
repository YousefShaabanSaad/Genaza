package com.yousef.genaza.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.yousef.genaza.R;
import com.yousef.genaza.databinding.ActivityViewImageBinding;
import com.yousef.genaza.listener.Constants;
import com.yousef.genaza.models.Dead;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class ViewImageActivity extends AppCompatActivity implements Constants {

    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityViewImageBinding binding = ActivityViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Dead dead= new Dead();
        dead.setPhoto(Objects.requireNonNull(getIntent().getExtras()).getString(PHOTO));
         bitmap = dead.getPhotoBitmap();
        if(bitmap != null)
            binding.imageView.setImageBitmap(bitmap);
        else
            binding.imageView.setImageResource(R.drawable.bg_image);

        binding.download.setOnClickListener(v ->  saveToGallery());
    }

    private void saveToGallery() {
        try {
            File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File appDir = new File(picturesDir, APP);

            // تحقق من وجود المجلد أو إنشاءه
            if (!appDir.exists()) {
                boolean created = appDir.mkdirs();
                if (!created) {
                    Toast.makeText(this, "❌ فشل إنشاء مجلد التطبيق", Toast.LENGTH_SHORT).show();
                    return; // اوقف العملية
                }
            }

            // اسم جديد للصورة
            String fileName = Objects.requireNonNull(getIntent().getExtras())
                    .getString(NAME,"Image_"+System.currentTimeMillis()) + ".jpg";
            File destFile = new File(appDir, fileName);

            FileOutputStream out = new FileOutputStream(destFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();

            // أحيانًا تحتاج إعلام المعرض لتحديث الصورة
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(Uri.fromFile(destFile));
            sendBroadcast(mediaScanIntent);

            Toast.makeText(this, "✅ تم حفظ الصورة في المعرض", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "❌ فشل حفظ الصورة", Toast.LENGTH_SHORT).show();
        }
    }


}