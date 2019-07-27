package ga.fliptech.imageeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
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

    public static RecyclerView rvModeList;
    ModeListAdapter modeListAdapter;
    RecyclerView.LayoutManager modeListLayoutManager;

    static CustomViewPager modeViewPager;

    public static StickerView stickerView;
    
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
}
