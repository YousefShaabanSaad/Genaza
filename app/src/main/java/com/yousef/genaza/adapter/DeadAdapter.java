package com.yousef.genaza.adapter;

import android.content.Context;
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
import com.yousef.genaza.models.Dead;
import java.util.List;

public class DeadAdapter extends RecyclerView.Adapter<DeadAdapter.MyHolder> {

    private final Context context;
    private final List<Dead> list;

    public DeadAdapter(Context context, List<Dead> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_dead, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Dead model = list.get(position);

        holder.name.setText(model.getName());
        holder.timePlace.setText(model.getTimePlace());
        holder.note.setText(model.getNotes());

        Glide.with(context)
                .load(model.getPhotoBitmap())
                .error(R.drawable.bg_image)
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return list.size();
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
