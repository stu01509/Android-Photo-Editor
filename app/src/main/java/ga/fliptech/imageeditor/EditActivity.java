package ga.fliptech.imageeditor;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.LinkedHashMap;

import ga.fliptech.imageeditor.imageeditor.Paint.PaintView;
import ga.fliptech.imageeditor.imageeditor.Rotate.RotateImageView;
import ga.fliptech.imageeditor.imageeditor.Sticker.StickerItem;
import ga.fliptech.imageeditor.imageeditor.Sticker.StickerTask;
import ga.fliptech.imageeditor.imageeditor.Sticker.StickerView;
import ga.fliptech.imageeditor.imageeditor.Text.TextStickerView;
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

public class EditActivity extends AppCompatActivity {

    public static final int MODE_NONE = -1;
    public static final int MODE_STICKERS = 0; // 貼圖模式
    public static final int MODE_PAINT = 1; // 繪圖模式
    public static final int MODE_FILTER = 2; // 濾鏡模式
    public static final int MODE_TEXT = 3; // 文字貼圖模式
    public static final int MODE_BEAUTY = 4; // 美肌模式
    public static final int MODE_ROTATE = 5; // 旋轉模式
    public static int MODE_STATE; // 紀錄目前的模式
    public static boolean isEdit = false; // 是否在編輯模式中
    public static int editCount = 0; // 紀錄修改次數

    public static ImageViewTouch imageZoom; // 底層縮放 ImageView
    public Bitmap sourceBitmap; // 原始Bitmap
    public Bitmap editBitmap; // 編輯用Bitmap

    public StickerFragment mStickerFragment;
    public PaintFragment mPaintFragment;
    public FilterFragment mFilterFragment;
    public TextFragment mTextFragment;
    public BeautyFragment mBeautyFragment;
    public RotateFragment mRotateFragment;

    private Toolbar toolbar; //頂部 Toolbar
    public RecyclerView rvModeList; // 功能模式選擇
    public ModeListAdapter modeListAdapter; // 功能模式選擇
    public RecyclerView.LayoutManager modeListLayoutManager;
    public CustomViewPager modeViewPager; // 顯示功能按鈕 ViewPager

    public StickerView stickerView;
    public static PaintView paintView;
    public static TextStickerView textStickerView;
    public RotateImageView rotateImageView;

    private SaveStickersTask saveStickersTask;
    private SavePaintTask savePaintTask;
    private SaveTextStickerTask saveTextStickerTask;
    private SaveRotateTask saveRotateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 鍵盤彈出時 直接覆蓋住當前 layout
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_edit);
        sourceBitmap = MainActivity.bitmapImage;
        editBitmap = sourceBitmap;

        initToolbar();
        initFragment();
        initView();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 隱藏 Toolbar 標題名稱
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem toolbarSave = menu.findItem(R.id.toolbarSave);
        if (!isEdit) {
            toolbarSave.setVisible(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else if (isEdit && editCount != 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbarSave.setVisible(true);
        } else if (isEdit && editCount == 0){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbarSave.setVisible(false);
        }
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toolbarSave) {
            switch (MODE_STATE) {
                case MODE_STICKERS: {
                    startSaveStickerTask();
                    break;
                }
                case MODE_PAINT: {
                    startSavePaintImageTask();
                    break;
                }
                case MODE_FILTER: {
                    sourceBitmap = editBitmap;
                    editCount = 0;
                    backToMain();
                    break;
                }
                case MODE_TEXT: {
                    startSaveTextStickerTask();
                    break;
                }
                case MODE_BEAUTY: {
                    sourceBitmap = editBitmap;
                    editCount = 0;
                    backToMain();
                    break;
                }
                case MODE_ROTATE: {
                    startSaveRotateTask();
                    break;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragment() {
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

    private void initView() {
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
        Glide.with(this).load(editBitmap).into(imageZoom);
        imageZoom.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        // Mode Select
        modeViewPager = findViewById(R.id.vpContainer);
        initModeViewPager(modeViewPager);

        // 貼圖畫布預覽
        stickerView = new StickerView(this);
        stickerView = findViewById(R.id.stickerView);

        // 繪圖畫布預覽
        paintView = new PaintView(this);
        paintView = findViewById(R.id.paintView);

        // 文字貼圖畫布預覽
        textStickerView = new TextStickerView(this);
        textStickerView = findViewById(R.id.textStickerView);

        // ImageView 圖片選轉
        rotateImageView = new RotateImageView(this);
        rotateImageView = findViewById(R.id.rotateImageView);

    }

    public void setMode(int modeNumber) {
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
            case MODE_FILTER: {
                MODE_STATE = MODE_FILTER;
                break;
            }
            case MODE_TEXT: {
                textStickerView.setVisibility(View.VISIBLE);
                MODE_STATE = MODE_TEXT;
                break;
            }
            case MODE_BEAUTY: {
                MODE_STATE = MODE_BEAUTY;
                break;
            }
            case MODE_ROTATE: {
                rotateImageView.setVisibility(View.VISIBLE);
                // 圖片旋轉 會直接繪製一張新的 Bitmap
                // 所以需將最底部的 imageZoom 隱藏
                imageZoom.setVisibility(View.GONE);
                rotateImageView.addBit(
                        editBitmap,
                        imageZoom.getBitmapRect()
                );

                MODE_STATE = MODE_ROTATE;
                break;
            }
        }
        rvModeList.setVisibility(View.INVISIBLE);
        modeViewPager.setVisibility(View.VISIBLE);
        modeViewPager.setCurrentItem(modeNumber);
    }

    private void backToMain() {
        if (editCount != 0) {
            saveDialog();
        }

        if (isEdit && editCount == 0) {
            isEdit = false;
            rvModeList.setVisibility(View.VISIBLE);
            modeViewPager.setVisibility(View.INVISIBLE);
            switch (MODE_STATE) {
                case MODE_STICKERS: {
                    stickerView.clear();
                    stickerView.setVisibility(View.GONE);
                    break;
                }
                case MODE_PAINT: {
                    paintView.reset();
                    paintView.setVisibility(View.GONE);
                    break;
                }
                case MODE_FILTER: {
                    break;
                }
                case MODE_TEXT: {
                    textStickerView.setVisibility(View.GONE);
                    break;
                }
                case MODE_BEAUTY: {
                    break;
                }
                case MODE_ROTATE: {
                    rotateImageView.setVisibility(View.GONE);
                    // 圖片旋轉 會直接繪製一張新的 Bitmap
                    // 結束後 需將最底部的 imageZoom 顯示
                    imageZoom.setVisibility(View.VISIBLE);
                    break;
                }
            }
            MODE_STATE = MODE_NONE;
            return;
        }
    }

    public void saveDialog() {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("要捨棄編輯嗎?")
            .setMessage("您的編輯尚未儲存")
            .setCancelable(false)
            .setNegativeButton("放棄", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editCount = 0;
                    switch (MODE_STATE) {
                        case MODE_FILTER: {
                            imageZoom.setImageBitmap(sourceBitmap);
                            break;
                        }
                        case MODE_TEXT: {
                            TextFragment.etInputText.setText("");
                            break;
                        }
                        case MODE_BEAUTY: {
                            BeautyFragment.resetValue();
                            break;
                        }
                        case MODE_ROTATE: {
                            RotateFragment.resetValue();
                        }
                    }
                    editBitmap = sourceBitmap;
                    imageZoom.setImageBitmap(editBitmap);
                    backToMain();
                }
            })
            .setPositiveButton("繼續", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            })
        .show();
    }

    public void saveImage(Bitmap saveBitmap) {
        editCount = 0;
        sourceBitmap = saveBitmap;
        editBitmap = sourceBitmap;
        imageZoom.setImageBitmap(editBitmap);
    }

    public static void editCount() {
        editCount++;
    }

    @Override
    public void onBackPressed() {
        if (isEdit == false && editCount == 0) {
            MainActivity.setEditImage(sourceBitmap);
            super.onBackPressed();
        } else {
            backToMain();
        }

    }

    private void startSaveStickerTask() {
        if (saveStickersTask != null) {
            saveStickersTask.cancel(true);
        }
        saveStickersTask = new SaveStickersTask(this);
        saveStickersTask.execute(editBitmap);
    }

    private void startSavePaintImageTask() {
        if (savePaintTask != null && !savePaintTask.isCancelled()) {
            savePaintTask.cancel(true);
        }

        savePaintTask = new SavePaintTask(this);
        savePaintTask.execute(editBitmap);
    }

    private void startSaveTextStickerTask() {
        if (saveTextStickerTask != null && !saveTextStickerTask.isCancelled()) {
            saveTextStickerTask.cancel(true);
        }

        saveTextStickerTask = new SaveTextStickerTask(this);
        saveTextStickerTask.execute(editBitmap);
    }

    private void startSaveRotateTask() {
        if (saveRotateTask != null && !saveRotateTask.isCancelled()) {
            saveRotateTask.cancel(true);
        }
        saveRotateTask = new SaveRotateTask();
        saveRotateTask.execute(editBitmap);
    }

    private final class SaveStickersTask extends StickerTask {

        public SaveStickersTask(EditActivity activity) {
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
            saveImage(result);
            stickerView.clear();
            backToMain();
        }
    }

    private final class SavePaintTask extends StickerTask {

        public SavePaintTask(EditActivity activity) {
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
            saveImage(result);
            paintView.reset();
            backToMain();
        }
    }

    private final class SaveTextStickerTask extends StickerTask {

        public SaveTextStickerTask(EditActivity activity) {
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
            //System.out.println("scale = " + scale_x + "       " + scale_y + "     " + dx + "    " + dy);
            textStickerView.drawText(canvas, textStickerView.layout_x,
                    textStickerView.layout_y, textStickerView.mScale, textStickerView.mRotateAngle);
            canvas.restore();
        }

        @Override
        public void onPostResult(Bitmap result) {
            textStickerView.clearTextContent();
            textStickerView.resetView();
            TextFragment.etInputText.setText("");
            saveImage(result);
            backToMain();
        }
    }

    private final class SaveRotateTask extends AsyncTask<Bitmap, Void, Bitmap> {

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            RectF imageRect = rotateImageView.getImageNewRect();
            Bitmap originBit = params[0];
            Bitmap result = Bitmap.createBitmap((int) imageRect.width(),
                    (int) imageRect.height(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            int w = originBit.getWidth() >> 1;
            int h = originBit.getHeight() >> 1;
            float centerX = imageRect.width() / 2;
            float centerY = imageRect.height() / 2;

            float left = centerX - w;
            float top = centerY - h;

            RectF dst = new RectF(left, top, left + originBit.getWidth(), top
                    + originBit.getHeight());
            canvas.save();

            canvas.rotate(rotateImageView.getRotateAngle(), imageRect.width() / 2,
                    imageRect.height() / 2);

            canvas.drawBitmap(originBit, new Rect(0, 0, originBit.getWidth(),
                    originBit.getHeight()), dst, null);
            canvas.restore();
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result == null)
                return;
            saveImage(result);
            RotateFragment.resetValue();
            backToMain();
        }
    }
}
