package ga.fliptech.imageeditor.imageeditor.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ga.fliptech.imageeditor.R;
import ga.fliptech.imageeditor.imageeditor.fragment.PaintFragment;
import ga.fliptech.imageeditor.imageeditor.fragment.TextFragment;

public class ColorListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ColorListAdapter";
    public static String[] colorList = {"#F44336", "#FF5722", "#FFEB3B", "#8BC34A", "#4CAF50", "#03A9F4", "#2196F3",
    "#3F51B5", "#673AB7", "#9C27B0", "#795548", "#9E9E9E", "#607D8B", "#000000"};

    public ColorListAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_color_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.ivColorIcon.setBackgroundColor(Color.parseColor(colorList[position]));
        viewHolder.ivColorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor(colorList[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivColorIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivColorIcon = itemView.findViewById(R.id.ivColorIcon);
        }
    }

    private void selectColor(String colorHex) {
        PaintFragment.newInstance().selectColor(colorHex);
        TextFragment.newInstance().selectColor(colorHex);
    }
}
