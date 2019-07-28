package ga.fliptech.imageeditor.imageeditor.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ga.fliptech.imageeditor.MainActivity;
import ga.fliptech.imageeditor.R;
import ga.fliptech.imageeditor.imageeditor.adapter.PaintColorListAdapter;

public class PaintFragment extends Fragment {
    private static final String TAG = "PaintFragment";

    private RecyclerView.LayoutManager colorListLayoutManager;
    public static RecyclerView rvColorList;
    public static PaintColorListAdapter paintColorListAdapter;
    SeekBar sbLineWidth;
    TextView tvUnit;
    ImageView ivEraser;

    boolean isEraser = false;

    private Context mContext;
    View mView;

    public PaintFragment() {
    }

    public static PaintFragment newInstance() {
        PaintFragment fragment = new PaintFragment();
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
        mView = inflater.inflate(R.layout.fragment_paint, container, false);

        // 顏色清單
        rvColorList = mView.findViewById(R.id.rvPaintColorList);
        rvColorList.setHasFixedSize(true);
        colorListLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvColorList.setLayoutManager(colorListLayoutManager);
        paintColorListAdapter = new PaintColorListAdapter();
        rvColorList.setAdapter(paintColorListAdapter);

        // 尺寸資訊
        tvUnit = mView.findViewById(R.id.tvUnit);

        // 橡皮擦功能
        ivEraser = mView.findViewById(R.id.ivEraser);
        ivEraser.setImageResource(R.drawable.eraser_normal);
        ivEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEraser) {
                    isEraser = !isEraser;
                    MainActivity.paintView.setEraser(isEraser);

                } else {
                    isEraser = !isEraser;
                    MainActivity.paintView.setEraser(isEraser);
                }
            }
        });

        // 線條調整
        sbLineWidth = mView.findViewById(R.id.sbLineWidth);
        sbLineWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    selectWidth(1 + progress);
                    tvUnit.setText("大小: " + (1 +progress) +" PX");
                    return;
                }
                selectWidth(progress);
                tvUnit.setText("大小: " + progress +" PX");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return mView;
    }

    public void selectColor(String colorHex) {
        MainActivity.paintView.setColor(colorHex);
        isEraser = false;
        MainActivity.paintView.setEraser(isEraser);
    }

    public void selectWidth(float lineWidth) {
        MainActivity.paintView.setWidth(lineWidth);
    }
}
