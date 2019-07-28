package ga.fliptech.imageeditor;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import java.util.LinkedHashMap;
import ga.fliptech.imageeditor.imageeditor.Paint.PaintView;
import ga.fliptech.imageeditor.imageeditor.Sticker.StickerItem;
import ga.fliptech.imageeditor.imageeditor.Sticker.StickerTask;
import ga.fliptech.imageeditor.imageeditor.Sticker.StickerView;
import ga.fliptech.imageeditor.imageeditor.adapter.ModeListAdapter;
import ga.fliptech.imageeditor.imageeditor.adapter.ModeSelectPagerAdapter;
import ga.fliptech.imageeditor.imageeditor.fragment.BeautyFragment;
import ga.fliptech.imageeditor.imageeditor.fragment.FilterFragment;
import ga.fliptech.imageeditor.imageeditor.fragment.PaintFragment;
import ga.fliptech.imageeditor.imageeditor.fragment.RotateFragment;
import ga.fliptech.imageeditor.imageeditor.fragment.StickerFragment;
import ga.fliptech.imageeditor.imageeditor.fragment.TextFragment;
import ga.fliptech.imageeditor.imageeditor.imagezoom.ImageViewTouch;
import ga.fliptech.imageeditor.imageeditor.imagezoom.ImageViewTouchBase;
import ga.fliptech.imageeditor.imageeditor.view.CustomViewPager;

public class MainActivity extends AppCompatActivity {

    public static final int MODE_NONE = -1;
    public static final int MODE_STICKERS = 0; // 貼圖模式
    public static final int MODE_PAINT = 1; // 繪圖模式
    public static final int MODE_FILTER = 2; // 濾鏡模式
    public static final int MODE_TEXT = 3; // 文字貼圖模式
    public static final int MODE_BEAUTY = 4; // 美肌模式
    public static final int MODE_ROTATE = 5; // 旋轉模式
    public static int MODE_STATE; // 紀錄目前的模式
    public static boolean isEdit = false;

    public StickerFragment mStickerFragment;
    public PaintFragment mPaintFragment;
    public FilterFragment mFilterFragment;
    public TextFragment mTextFragment;
    public BeautyFragment mBeautyFragment;
    public RotateFragment mRotateFragment;

    public ImageViewTouch imageZoom;
    Bitmap sourceBitmap;

    Button btnPreview;

    public static RecyclerView rvModeList;
    ModeListAdapter modeListAdapter;
    RecyclerView.LayoutManager modeListLayoutManager;

    static CustomViewPager modeViewPager;

    public static StickerView stickerView;
    public static PaintView paintView;

    private SaveStickersTask saveStickersTask;
    private SavePaintTask savePaintTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wp2284576);

        // Mode List
        rvModeList = findViewById(R.id.rvModeList);
        rvModeList.setHasFixedSize(true);
        modeListLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        rvModeList.setLayoutManager(modeListLayoutManager);
        modeListAdapter = new ModeListAdapter(this);
        rvModeList.setAdapter(modeListAdapter);

        // Main ImageZoomView
        imageZoom = findViewById(R.id.imageZoom);
        imageZoom.setImageBitmap(sourceBitmap);
        imageZoom.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        initView();

        // Mode Select
        modeViewPager = findViewById(R.id.vpContainer);
        initModeViewPager(modeViewPager);

        // 貼圖畫布預覽
        stickerView = new StickerView(this);
        stickerView = findViewById(R.id.stickerView);

        // 繪圖畫布預覽
        paintView = new PaintView(this);
        paintView = findViewById(R.id.paintView);


        btnPreview = findViewById(R.id.btnPreview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSticker();
                savePaintImage();
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        mStickerFragment = StickerFragment.newInstance();
        mPaintFragment = PaintFragment.newInstance();
        mFilterFragment = FilterFragment.newInstance();
        mTextFragment = TextFragment.newInstance();
        mBeautyFragment = BeautyFragment.newInstance();
        mRotateFragment = RotateFragment.newInstance();
    }

    private void initModeViewPager(ViewPager viewPager) {
        ModeSelectPagerAdapter modeSelectPagerAdapter = new ModeSelectPagerAdapter(getSupportFragmentManager());
        modeSelectPagerAdapter.addFragment(mStickerFragment);
        modeSelectPagerAdapter.addFragment(mPaintFragment);
        modeSelectPagerAdapter.addFragment(mFilterFragment);
        modeSelectPagerAdapter.addFragment(mTextFragment);
        modeSelectPagerAdapter.addFragment(mBeautyFragment);
        modeSelectPagerAdapter.addFragment(mRotateFragment);
        viewPager.setAdapter(modeSelectPagerAdapter);
    }

    public static void setMode(int modeNumber) {
        isEdit = true;
        switch (modeNumber) {
            case MODE_STICKERS: {
                stickerView.setVisibility(View.VISIBLE);
                MODE_STATE = MODE_STICKERS;
                break;
            }
            case MODE_PAINT: {
                paintView.setVisibility(View.VISIBLE);
                MODE_STATE = MODE_PAINT;
                break;
            }
        }
        rvModeList.setVisibility(View.INVISIBLE);
        modeViewPager.setVisibility(View.VISIBLE);
        modeViewPager.setCurrentItem(modeNumber);
    }

    public void processSticker() {
        if (saveStickersTask != null) {
            saveStickersTask.cancel(true);
        }
        saveStickersTask = new SaveStickersTask(this);
        saveStickersTask.execute(sourceBitmap);
    }

    public void savePaintImage() {
        if (savePaintTask != null && !savePaintTask.isCancelled()) {
            savePaintTask.cancel(true);
        }

        savePaintTask = new SavePaintTask(this);
        savePaintTask.execute(sourceBitmap);
    }

    public void previewSticker(Bitmap previewSticker) {
        AlertDialog.Builder stickerDialog = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.dialog_preview, null);

        ImageView ivPreview =  view.findViewById(R.id.ivPreview);
        Glide.with(this).load(previewSticker).into(ivPreview);

        stickerDialog.setView(view);
        stickerDialog.setNeutralButton("OK!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        stickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (isEdit) {
            isEdit = false;
            rvModeList.setVisibility(View.VISIBLE);
            modeViewPager.setVisibility(View.INVISIBLE);
            switch (MODE_STATE) {
                case MODE_STICKERS: {
                    stickerView.setVisibility(View.GONE);
                    break;
                }
                case MODE_PAINT: {
                    paintView.setVisibility(View.GONE);
                    break;
                }
            }

            MODE_STATE = MODE_NONE;
            Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

    private final class SaveStickersTask extends StickerTask {
        public SaveStickersTask(MainActivity activity) {
            super(activity);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            LinkedHashMap<Integer, StickerItem> addItems = stickerView.getBank();
            for (Integer id : addItems.keySet()) {
                StickerItem item = addItems.get(id);
                item.matrix.postConcat(m);
                canvas.drawBitmap(item.bitmap, item.matrix, null);
            }
        }

        @Override
        public void onPostResult(Bitmap result) {
            previewSticker(result);
            stickerView.clear();
        }
    }

    private final class SavePaintTask extends StickerTask {

        public SavePaintTask(MainActivity activity) {
            super(activity);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            float[] f = new float[9];
            m.getValues(f);
            int dx = (int) f[Matrix.MTRANS_X];
            int dy = (int) f[Matrix.MTRANS_Y];
            float scale_x = f[Matrix.MSCALE_X];
            float scale_y = f[Matrix.MSCALE_Y];
            canvas.save();
            canvas.translate(dx, dy);
            canvas.scale(scale_x, scale_y);

            if (paintView.getPaintBit() != null) {
                canvas.drawBitmap(paintView.getPaintBit(), 0, 0, null);
            }
            canvas.restore();
        }

        @Override
        public void onPostResult(Bitmap result) {
            previewSticker(result);
            paintView.reset();
        }
    }
}
