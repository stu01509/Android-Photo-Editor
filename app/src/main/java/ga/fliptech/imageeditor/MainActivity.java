package ga.fliptech.imageeditor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.LinkedHashMap;

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

    public static final int MODE_STICKERS = 1; // 貼圖模式
    public static final int MODE_PAINT = 2; // 繪圖模式
    public static final int MODE_FILTER = 3; // 濾鏡模式
    public static final int MODE_TEXT = 4; // 文字貼圖模式
    public static final int MODE_BEAUTY = 5; // 美肌模式
    public static final int MODE_ROTATE = 6; // 旋轉模式
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
    private SaveStickersTask saveStickersTask;

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


        btnPreview = findViewById(R.id.btnPreview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSticker();
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
}
