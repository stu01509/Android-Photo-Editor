package ga.fliptech.imageeditor;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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

    public static ImageViewTouch imageZoom;
    public static Bitmap sourceBitmap;

    Button btnPreview;

    public static RecyclerView rvModeList;
    ModeListAdapter modeListAdapter;
    RecyclerView.LayoutManager modeListLayoutManager;

    static CustomViewPager modeViewPager;

    public static StickerView stickerView;
    public static PaintView paintView;
    public static TextStickerView textStickerView;
    public static RotateImageView rotateImageView;

    private SaveStickersTask saveStickersTask;
    private SavePaintTask savePaintTask;
    private SaveTextStickerTask saveTextStickerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 鍵盤彈出時 直接覆蓋住當前 layout
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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

        // 文字貼圖畫布預覽
        textStickerView = new TextStickerView(this);
        textStickerView = findViewById(R.id.textStickerView);

        // ImageView 圖片選轉
        rotateImageView = new RotateImageView(this);
        rotateImageView = findViewById(R.id.rotateImageView);

        btnPreview = findViewById(R.id.btnPreview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (MODE_STATE) {
                    case MODE_STICKERS: {
                        processSticker();
                        break;
                    }
                    case MODE_PAINT: {
                        savePaintImage();
                        break;
                    }
                    case MODE_FILTER: {
                        break;
                    }
                    case MODE_TEXT: {
                        saveTextSticker();
                        break;
                    }
                    case MODE_ROTATE: {
                        saveRotate();
                        break;
                    }
                }
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
            case MODE_FILTER: {
//                paintView.setVisibility(View.VISIBLE);
                MODE_STATE = MODE_FILTER;
                break;
            }
            case MODE_TEXT: {
                textStickerView.setVisibility(View.VISIBLE);
                MODE_STATE = MODE_TEXT;
                break;
            }
            case MODE_BEAUTY: {
//                paintView.setVisibility(View.VISIBLE);
                MODE_STATE = MODE_BEAUTY;
                break;
            }
            case MODE_ROTATE: {
                rotateImageView.setVisibility(View.VISIBLE);
                // 圖片旋轉 會直接繪製一張新的 Bitmap
                // 所以需將最底部的 imageZoom 隱藏
                imageZoom.setVisibility(View.GONE);
                MODE_STATE = MODE_ROTATE;
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

    public void saveTextSticker() {
        if (saveTextStickerTask != null && !saveTextStickerTask.isCancelled()) {
            saveTextStickerTask.cancel(true);
        }

        saveTextStickerTask = new SaveTextStickerTask(this);
        saveTextStickerTask.execute(sourceBitmap);
    }

    public void saveRotate() {
        SaveRotateImageTask task = new SaveRotateImageTask();
        task.execute(sourceBitmap);
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
                case MODE_FILTER: {
//                    paintView.setVisibility(View.GONE);
                    break;
                }
                case MODE_TEXT: {
                    textStickerView.setVisibility(View.GONE);
                    break;
                }
                case MODE_BEAUTY: {
//                    paintView.setVisibility(View.GONE);
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

    private final class SaveTextStickerTask extends StickerTask {

        public SaveTextStickerTask(MainActivity activity) {
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
            previewSticker(result);
        }
    }

    private final class SaveRotateImageTask extends AsyncTask<Bitmap, Void, Bitmap> {

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
            previewSticker(result);
        }
    }
}
