package ga.fliptech.imageeditor.imageeditor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import ga.fliptech.imageeditor.EditActivity;
import ga.fliptech.imageeditor.R;

public class ModeListAdapter extends RecyclerView.Adapter {
    private Context context;
    private final String[] modeName = {"貼圖模式", "繪圖模式", "濾鏡模式", "文字模式", "美膚模式", "旋轉模式"};
    private final int[] modeIcon = {
            R.drawable.ic_insert_emoticon_black_24dp, R.drawable.ic_create_black_24dp,
            R.drawable.ic_filter_black_24dp, R.drawable.ic_format_color_text_black_24dp,
            R.drawable.ic_face_black_24dp, R.drawable.ic_crop_rotate_black_24dp};

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
                    ((EditActivity)context).setMode(getAdapterPosition());
                }
            });
        }
    }
}
