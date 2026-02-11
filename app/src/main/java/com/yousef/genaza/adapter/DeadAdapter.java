package com.yousef.genaza.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yousef.genaza.R;
import com.yousef.genaza.listener.ItemsListener;
import com.yousef.genaza.models.Dead;
import java.util.List;

public class DeadAdapter extends RecyclerView.Adapter<DeadAdapter.MyHolder> {

    private final Context context;
    private final List<Dead> list;
    private final ItemsListener<Dead> listener;
    private String search;

    public DeadAdapter(Context context, List<Dead> list, String search, ItemsListener<Dead> listener) {
        this.context = context;
        this.list = list;
        this.search = search;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_dead, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Dead item = list.get(position);
        String name = item.getName();
        if(search.isEmpty()){
            holder.name.setText(name);
        }
        else {
            int start = name.indexOf(search);
            int end = start + search.length();
            holder.name.setText(colorPart(name, start, end));
        }
        holder.timePlace.setText(item.getTimePlace());
        holder.note.setText(item.getNotes());

        Glide.with(context)
                .load(item.getPhotoBitmap())
                .error(R.drawable.bg_image)
                .into(holder.photo);

        holder.photo.setOnClickListener(v -> listener.clickItem(item));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyDataSetChanged(String search){
        this.search = search;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public SpannableString colorPart(String full, int start, int end) {
        SpannableString span = new SpannableString(full);
        span.setSpan(
                new ForegroundColorSpan(Color.parseColor("#FF9800")),
                start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       return span;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView name, timePlace, note;
        ProgressBar progress;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            timePlace = itemView.findViewById(R.id.timePlace);
            note = itemView.findViewById(R.id.note);
            progress = itemView.findViewById(R.id.progress);
        }
    }
}
