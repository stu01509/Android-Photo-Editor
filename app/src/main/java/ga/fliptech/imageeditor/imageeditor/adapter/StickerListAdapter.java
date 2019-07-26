package ga.fliptech.imageeditor.imageeditor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ga.fliptech.imageeditor.MainActivity;
import ga.fliptech.imageeditor.R;
import ga.fliptech.imageeditor.imageeditor.fragment.StickerFragment;

public class StickerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private static final String TAG = "StickerListAdapter";
    public ArrayList<Integer> stickerList = new ArrayList<Integer>();

    public StickerListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_sticker_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(context).load(stickerList.get(position)).into(viewHolder.ivSticker);
        viewHolder.ivSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerChoose(stickerList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivSticker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSticker = itemView.findViewById(R.id.ivSticker);
        }
    }

    public void stickerChoose(int sticker) {
        StickerFragment.newInstance().selectedStickerItem(context, sticker);
        Toast.makeText(context, "你選擇" + sticker + " 的圖片", Toast.LENGTH_SHORT).show();
    }

    public void initSticker(String stickerType) {
        stickerList.clear();
        switch (stickerType) {
            case "樣式A": {
                stickerList.add(R.drawable.a_1);
                stickerList.add(R.drawable.a_2);
                stickerList.add(R.drawable.a_3);
                stickerList.add(R.drawable.a_4);
                stickerList.add(R.drawable.a_5);
                stickerList.add(R.drawable.a_6);
                stickerList.add(R.drawable.a_7);
                stickerList.add(R.drawable.a_8);
                break;
            }

            case "樣式B": {
                stickerList.add(R.drawable.b_1);
                stickerList.add(R.drawable.b_2);
                stickerList.add(R.drawable.b_3);
                stickerList.add(R.drawable.b_4);
                stickerList.add(R.drawable.b_5);
                stickerList.add(R.drawable.b_6);
                stickerList.add(R.drawable.b_7);
                stickerList.add(R.drawable.b_8);
                break;
            }

            case "樣式C": {
                stickerList.add(R.drawable.c_1);
                stickerList.add(R.drawable.c_2);
                stickerList.add(R.drawable.c_3);
                stickerList.add(R.drawable.c_4);
                stickerList.add(R.drawable.c_5);
                stickerList.add(R.drawable.c_6);
                stickerList.add(R.drawable.c_7);
                stickerList.add(R.drawable.c_8);
                break;
            }
        }
        this.notifyDataSetChanged();
    }

}
