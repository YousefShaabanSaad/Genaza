package com.yousef.genaza.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
        dead.setZPhoto(Objects.requireNonNull(getIntent().getExtras()).getString(PHOTO));
         bitmap = dead.getPhotoBitmap();
        if(bitmap != null)
            binding.imageView.setImageBitmap(bitmap);
        else
            binding.imageView.setImageResource(R.drawable.bg_image);

        binding.download.setOnClickListener(v ->  saveToGallery());
    }

    private void saveToGallery() {

        try {
            // تأكد من الصورة الأصلية
            if (bitmap == null) {
                Toast.makeText(this, "❌ الصورة الأصلية غير موجودة", Toast.LENGTH_SHORT).show();
                return;
            }

            // تحميل اللوجو من drawable
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

            if (logo == null) {
                Toast.makeText(this, "❌ اللوجو غير موجود داخل drawable", Toast.LENGTH_LONG).show();
                return;
            }

            // إنشاء صورة جديدة للرسم
            Bitmap result = Bitmap.createBitmap(
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    Bitmap.Config.ARGB_8888
            );

            Canvas canvas = new Canvas(result);

            // رسم الصورة الأصلية
            canvas.drawBitmap(bitmap, 0, 0, null);

            // تصغير اللوجو تلقائيًا
            int logoWidth = bitmap.getWidth() / 5; // 20% من عرض الصورة
            int logoHeight = logo.getHeight() * logoWidth / logo.getWidth();

            Bitmap resizedLogo = Bitmap.createScaledBitmap(logo, logoWidth, logoHeight, true);

            // مكان اللوجو (أسفل يمين)
            int margin = 25;
            int left = bitmap.getWidth() - logoWidth - margin;

            canvas.drawBitmap(resizedLogo, left, margin, null);

            // مسار الحفظ
            File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File appDir = new File(picturesDir, APP);

            if (!appDir.exists() && !appDir.mkdirs()) {
                Toast.makeText(this, "❌ فشل إنشاء مجلد", Toast.LENGTH_SHORT).show();
                return;
            }

            // اسم الصورة
            Bundle extras = getIntent().getExtras();
            String fileName = "Image_" + System.currentTimeMillis();

            if (extras != null && extras.containsKey(NAME)) {
                fileName = extras.getString(NAME, fileName);
            }

            fileName += ".jpg";

            File file = new File(appDir, fileName);

            FileOutputStream out = new FileOutputStream(file);
            result.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            // تحديث المعرض
            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            scanIntent.setData(Uri.fromFile(file));
            sendBroadcast(scanIntent);

            Toast.makeText(this, "✅ تم حفظ الصورة", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "❌ فشل حفظ الصورة", Toast.LENGTH_SHORT).show();
        }
    }

}