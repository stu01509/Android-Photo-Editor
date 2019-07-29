package ga.fliptech.imageeditor.imageeditor.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.fragment.app.Fragment;
import ga.fliptech.imageeditor.MainActivity;
import ga.fliptech.imageeditor.R;

public class RotateFragment extends Fragment {
    private static final String TAG = "RotateFragment";

    SeekBar sbRotateDegree;
    View mView;

    public RotateFragment() {
    }

    public static RotateFragment newInstance() {
        RotateFragment fragment = new RotateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_rotate, container, false);

        MainActivity.rotateImageView.addBit(
                MainActivity.sourceBitmap,
                MainActivity.imageZoom.getBitmapRect()
        );
        MainActivity.rotateImageView.reset();

        sbRotateDegree = mView.findViewById(R.id.sbRotateDegree);
        sbRotateDegree.setProgress(0);
        sbRotateDegree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.rotateImageView.rotateImage(progress);
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

}
