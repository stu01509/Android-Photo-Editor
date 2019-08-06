package ga.fliptech.imageeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static Bitmap bitmapImage;
    public static ImageView ivImage;
    Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImage = findViewById(R.id.ivImage);
        btnEdit = findViewById(R.id.btnEdit);

        ImagePicker.create(this)
                .start();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            ArrayList<Image> pathImages = (ArrayList) ImagePicker.getImages(data);

            for (Image image : pathImages) {
                Uri path = Uri.parse("file://" + image.getPath());
                Log.e("path:", path.toString());
                bitmapImage = getBitmap(path);
                Glide.with(this).load(bitmapImage).into(ivImage);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap getBitmap(Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = this.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void setEditImage(Bitmap bitmap) {
        bitmapImage = bitmap;
        ivImage.setImageBitmap(bitmapImage);
    }
}
