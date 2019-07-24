package ga.fliptech.imageeditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import ga.fliptech.imageeditor.imageeditor.adapter.ModeListAdapter;
import ga.fliptech.imageeditor.imageeditor.adapter.ModeSelectPagerAdapter;
import ga.fliptech.imageeditor.imageeditor.fragment.BeautyFragment;
import ga.fliptech.imageeditor.imageeditor.fragment.FilterFragment;
import ga.fliptech.imageeditor.imageeditor.fragment.PaintFragment;
import ga.fliptech.imageeditor.imageeditor.fragment.RotateFragment;
import ga.fliptech.imageeditor.imageeditor.fragment.StickerFragment;
import ga.fliptech.imageeditor.imageeditor.fragment.TextFragment;
import ga.fliptech.imageeditor.imageeditor.view.CustomViewPager;

public class MainActivity extends AppCompatActivity {

    public static final int MODE_STICKERS = 1; // 貼圖模式
    public static final int MODE_PAINT = 2; // 繪圖模式
    public static final int MODE_FILTER = 3; // 濾鏡模式
    public static final int MODE_TEXT = 4; // 文字貼圖模式
    public static final int MODE_BEAUTY = 5; // 美肌模式
    public static final int MODE_ROTATE = 6; // 旋轉模式

    Bitmap sourceBitmap;

    RecyclerView rvModeList;
    ModeListAdapter modeListAdapter;
    RecyclerView.LayoutManager modeListLayoutManager;

    static CustomViewPager modeViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mode List
        rvModeList = findViewById(R.id.rvModeList);
        rvModeList.setHasFixedSize(true);
        modeListLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        rvModeList.setLayoutManager(modeListLayoutManager);
        modeListAdapter = new ModeListAdapter(this);
        rvModeList.setAdapter(modeListAdapter);

        // Mode Select
        modeViewPager = findViewById(R.id.vpContainer);
        initModeViewPager(modeViewPager);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initModeViewPager(ViewPager viewPager) {
        ModeSelectPagerAdapter modeSelectPagerAdapter = new ModeSelectPagerAdapter(getSupportFragmentManager());
        modeSelectPagerAdapter.addFragment(new StickerFragment());
        modeSelectPagerAdapter.addFragment(new PaintFragment());
        modeSelectPagerAdapter.addFragment(new FilterFragment());
        modeSelectPagerAdapter.addFragment(new TextFragment());
        modeSelectPagerAdapter.addFragment(new BeautyFragment());
        modeSelectPagerAdapter.addFragment(new RotateFragment());
        viewPager.setAdapter(modeSelectPagerAdapter);
    }

    public static void setMode(int modeNumber) {
        modeViewPager.setCurrentItem(modeNumber);
    }




}
