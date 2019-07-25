package ga.fliptech.imageeditor.imageeditor.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ga.fliptech.imageeditor.MainActivity;
import ga.fliptech.imageeditor.R;


public class StickerFragment extends Fragment {

    public StickerFragment() {
    }

    public static StickerFragment newInstance() {
        StickerFragment fragment = new StickerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_sticker, container, false);
        View view  = inflater.inflate(R.layout.fragment_sticker, container, false);
        return view;
    }

//    public void finishEdit() {
//        MainActivity.isEdit = false;
//        MainActivity.rvModeList.setVisibility(View.INVISIBLE);
//        MainActivity.modeViewPager.setVisibility(View.VISIBLE);
//    }

}
