package ga.fliptech.imageeditor.imageeditor.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ga.fliptech.imageeditor.MainActivity;
import ga.fliptech.imageeditor.R;
import ga.fliptech.imageeditor.imageeditor.adapter.StickerListAdapter;
import ga.fliptech.imageeditor.imageeditor.adapter.StickerTypeListAdapter;

public class StickerFragment extends Fragment {
    private static final String TAG = "StickerFragment";

    private RecyclerView.LayoutManager stickerTypeLayoutManager;
    public static RecyclerView rvStickerTypeList, rvStickerList;
    public static StickerTypeListAdapter stickerTypeListAdapter;
    public static StickerListAdapter stickerListAdapter;
    private Context mContext;
    View mView;

    public StickerFragment() {
    }

    public static StickerFragment newInstance() {
        StickerFragment fragment = new StickerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sticker, container, false);

        rvStickerTypeList = mView.findViewById(R.id.rvStickerTypeList);
        // 固定 RecyclerView 每個 item 的尺寸大小
        rvStickerTypeList.setHasFixedSize(true);

        stickerTypeLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvStickerTypeList.setLayoutManager(stickerTypeLayoutManager);
        stickerTypeListAdapter = new StickerTypeListAdapter(mContext);
        rvStickerTypeList.setAdapter(stickerTypeListAdapter);

        // 圖片選擇
        rvStickerList = mView.findViewById(R.id.rvStickerList);
        rvStickerList.setHasFixedSize(true);
        // RecyclerView Item 快取
        rvStickerList.setItemViewCacheSize(10);
        rvStickerList.setLayoutManager(new GridLayoutManager(mContext, 4));
        rvStickerList.setDrawingCacheEnabled(true);
        rvStickerList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        stickerListAdapter = new StickerListAdapter(mContext);
        rvStickerList.setAdapter(stickerListAdapter);
        initSticker("樣式A");
        return mView;
    }

    // 設定貼圖樣式
    public static void initSticker(String stickerType) {
        stickerListAdapter.initSticker(stickerType);
    }

    // 選擇貼圖
    public void selectedStickerItem(Context context, int resId) {
        Bitmap addStickerBitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        MainActivity.stickerView.addBitImage(addStickerBitmap);
    }

}
