package com.example.searchablespinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;

/**
 * Created by tushar on 5/5/17.
 */

public class SearchableSpinner extends LinearLayout {
    private ListView mListView;
    private int mSpinnerType;
    private EditText mEditText;
    private TextInputLayout mTextInputLayout;
    private ArrayList<String> mValues;
    private ArrayList<String> mCache;
    private Context mContext;
    private ArrayAdapter<String> mSpinnerAdapter;
    private IAdapterOnClickListener mIAdapterOnClickListener;
    private String mHint;
    private PopupWindow mPopupWindow;
    View.OnClickListener mSearchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPopupWindow.isShowing()) {
                dismissPopup();
            } else {
                mEditText.setText("");
                mPopupWindow.showAsDropDown(mEditText);
            }

        }
    };
    View.OnFocusChangeListener mEditTextListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                showPopup();
            } else {
                dismissPopup();
            }
        }
    };
    AdapterView.OnItemClickListener mListListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = (String) parent.getItemAtPosition(position);
            int index = mValues.indexOf(selectedItem);
            String name = mIAdapterOnClickListener.onItemClicked(parent, view, index, mSpinnerType);
            setEditTextName(name);
            dismissPopup();
        }
    };

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        a.recycle();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.searchable_spinner, this, true);
        mContext = context;
        mCache = new ArrayList<>();
        mEditText = (TextInputEditText) findViewById(R.id.search_beat_edittext);
        mTextInputLayout = (TextInputLayout) findViewById(R.id.input_layout_invoice);
        View dropDown = inflater.inflate(R.layout.popup_searchable_spinner, null);
        mListView = (ListView) dropDown.findViewById(R.id.beat_list);
        mEditText.setVisibility(View.VISIBLE);
        mPopupWindow = new PopupWindow(dropDown, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        setAdapter();
        setTexWatcher();
    }

    private void showPopup() {
        mEditText.setText("");
        mPopupWindow.showAsDropDown(mEditText);
    }

    public void setIAdapterOnClickListener(
            IAdapterOnClickListener IAdapterOnClickListener) {
        mIAdapterOnClickListener = IAdapterOnClickListener;
    }

    public void setSpinnerType(int type) {
        mSpinnerType = type;
    }

    public void setTexWatcher() {
        mEditText.setOnClickListener(mSearchListener);
        mEditText.addTextChangedListener(new SearchableSpinnerEditTextWatcher());
        mEditText.setOnFocusChangeListener(mEditTextListener);
    }

    public void setTextInputLayoutParams() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0,
                0, 0);
        mTextInputLayout.setLayoutParams(layoutParams);
        mTextInputLayout.setPadding(0, 0, 0, 0);
    }

    public void setAdapter() {
        mSpinnerAdapter = new ArrayAdapter<String>(mContext,
                R.layout.searchable_spinner_item, mCache);
        mListView.setAdapter(mSpinnerAdapter);
        mListView.setOnItemClickListener(mListListener);
    }

    public void setArrayList(ArrayList<String> arrayList) {
        mValues = arrayList;
        mCache.addAll(mValues);
    }

    public void setEditTextName(String name) {
        if (name == null) {
            return;
        }
        if (name.equals("")) {
            mEditText.setText(null);
            mEditText.setHint(getHint());
        } else {
            mEditText.setText(name);
        }
    }

    public void setSearchTextProperties(int color) {
        mEditText.setTextColor(color);
        mEditText.setTextSize(16);
        mEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

    }

    public void setSpinnerTriangleColor(int rightDrawable) {
        mEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightDrawable, 0);
    }

    private void dismissPopup() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }


    public String getHint() {
        return mHint;
    }

    public void setHint(String hint) {
        mHint = hint;
    }

    private class SearchableSpinnerEditTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mValues != null && mValues.size() > 0) {
                mListView.setVisibility(VISIBLE);
                String str = editable.toString();
                if (str.length() > 0) {
                    mCache.clear();
                    for (int i = 0; i < mValues.size(); i++) {
                        if (mValues.get(i).toLowerCase().contains(str.toLowerCase())) {
                            mCache.add(mValues.get(i));
                        }
                    }
                } else {
                    mCache.clear();
                    mCache.addAll(mValues);
                }
                mSpinnerAdapter.notifyDataSetChanged();
            }
        }
    }
}
