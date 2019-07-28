package ga.fliptech.imageeditor.imageeditor.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ga.fliptech.imageeditor.MainActivity;
import ga.fliptech.imageeditor.R;
import ga.fliptech.imageeditor.imageeditor.adapter.ColorListAdapter;

public class TextFragment extends Fragment {
    private static final String TAG = "TextFragment";

    private RecyclerView.LayoutManager colorListLayoutManager;
    public static RecyclerView rvColorList;
    public static ColorListAdapter textColorListAdapter;
    EditText etInputText;

    private Context mContext;
    View mView;

    public TextFragment() {
    }

    public static TextFragment newInstance() {
        TextFragment fragment = new TextFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_text , container, false);

        // 顏色清單
        rvColorList = mView.findViewById(R.id.rvTextColorList);
        rvColorList.setHasFixedSize(true);
        colorListLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        rvColorList.setLayoutManager(colorListLayoutManager);
        textColorListAdapter = new ColorListAdapter();
        rvColorList.setAdapter(textColorListAdapter);

        // 文字輸入
        etInputText = mView.findViewById(R.id.etInputText);
        etInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.textStickerView.setText(s.toString());
            }
        });
        return mView;
    }

    public void selectColor(String colorHex) {
        MainActivity.textStickerView.setColor(colorHex);
    }

}
