package com.yousef.genaza.database;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.util.IslamicCalendar;
import android.icu.util.ULocale;
import android.os.Build;
import android.util.Base64;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.yousef.genaza.R;
import com.yousef.genaza.activity.MainActivity;
import com.yousef.genaza.listener.Constants;
import com.yousef.genaza.models.Dead;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Helper implements Constants {
    private final Context context;

    public Helper(Context context){
        this.context = context;
    }

    public void setIntent(Class<?> clc){
        Intent intent = new Intent(context, clc);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public String generateRandomID() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-={}[]|:;<>,.?/~";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    public String bitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    // Create and return a progress dialog
    public Dialog createProgress(){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    private String getDateByPattern() {
        return new SimpleDateFormat("EEEE", Locale.forLanguageTag("ar")).format(System.currentTimeMillis());
    }
    private String getDay(){
        return getDateByPattern();
    }

    public String getHijriDate() {
        IslamicCalendar calendar = new IslamicCalendar(ULocale.forLanguageTag("ar"));
        calendar.setTime(new Date());

        int day = calendar.get(IslamicCalendar.DAY_OF_MONTH);
        int month = calendar.get(IslamicCalendar.MONTH);
        int year = calendar.get(IslamicCalendar.YEAR);

        String[] months = {
                "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
                "جمادى الأولى", "جمادى الآخرة",
                "رجب", "شعبان", "رمضان",
                "شوال", "ذو القعدة", "ذو الحجة"
        };

        String hijri = getDay() + " - " + day + " " + months[month] + " " + year + " هـ";
        return convertEnglishToArabicNumbers(hijri);
    }

    private String convertEnglishToArabicNumbers(String input) {
        char[] english = {'0','1','2','3','4','5','6','7','8','9'};
        char[] arabic  = {'٠','١','٢','٣','٤','٥','٦','٧','٨','٩'};

        String result = input;
        for (int i = 0; i < english.length; i++) {
            result = result.replace(english[i], arabic[i]);
        }
        return result;
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private PendingIntent getPendingIntent(){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );
    }
    public Notification startNotification(){
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("تطبيق جنازة")
                .setContentText("إترك هذا الإشعار لمتابعة حالات الوفاة")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(getPendingIntent())
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setAutoCancel(false)
                .build();
    }


    public void sendNotification(Dead item) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_foreground)
                        .setContentTitle(item.getName())
                        .setContentText(item.getTimePlace())
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(item.getTimePlace()))
                        .setContentIntent(getPendingIntent())
                        .setAutoCancel(true);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
    }
}
