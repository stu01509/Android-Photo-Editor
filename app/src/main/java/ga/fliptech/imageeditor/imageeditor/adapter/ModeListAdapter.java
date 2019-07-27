package ga.fliptech.imageeditor.imageeditor.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import ga.fliptech.imageeditor.MainActivity;
import ga.fliptech.imageeditor.R;

public class ModeListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ModeListAdapter";
    private Context context;
    private final String[] modeName = {"貼圖模式", "繪圖模式", "濾鏡模式", "文字模式", "美膚模式", "旋轉模式"};
    private final int[] modeIcon = {
            R.drawable.baseline_insert_photo_black_48, R.drawable.baseline_insert_photo_black_48,
            R.drawable.baseline_insert_photo_black_48, R.drawable.baseline_insert_photo_black_48,
            R.drawable.baseline_insert_photo_black_48, R.drawable.baseline_insert_photo_black_48};

    public ModeListAdapter(Context mContext) {
        this.context = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_mode_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(context).load(modeIcon[position]).into(viewHolder.ivModeIcon);
        viewHolder.tvModeName.setText(modeName[position]);
    }

    @Override
    public int getItemCount() {
        return modeName.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivModeIcon;
        public TextView tvModeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivModeIcon = itemView.findViewById(R.id.ivModeIcon);
            tvModeName = itemView.findViewById(R.id.tvModeName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.setMode(getAdapterPosition());
                }
            });
        }
    }
}
