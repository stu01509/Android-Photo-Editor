package ga.fliptech.imageeditor.imageeditor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ga.fliptech.imageeditor.R;
import ga.fliptech.imageeditor.imageeditor.fragment.StickerFragment;

public class StickerTypeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String[] stickerTypeName = {"樣式A", "樣式B", "樣式C"};
    private Context context;
    public StickerTypeListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_sticker_type_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String stickerType = stickerTypeName[position];
        viewHolder.tvStickerType.setText(stickerType);
        viewHolder.tvStickerType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeChoose(stickerTypeName[position]);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stickerTypeName.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvStickerType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStickerType = itemView.findViewById(R.id.tvStickerType);
        }
    }

    public void typeChoose(String stickerType) {
            StickerFragment.newInstance().initSticker(stickerType);
    }
}
