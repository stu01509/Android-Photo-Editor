package ga.fliptech.imageeditor.imageeditor.fragment;


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

public class PaintFragment extends Fragment {

    public PaintFragment() {
    }

    public static PaintFragment newInstance() {
        PaintFragment fragment = new PaintFragment();
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
        View view  = inflater.inflate(R.layout.fragment_paint, container, false);
        return view;
    }
}
