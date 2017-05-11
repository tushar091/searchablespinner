package com.example.searchablespinner;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by tushar on 5/5/17.
 */

public interface IAdapterOnClickListener {

    public String onItemClicked(AdapterView<?> parent, View view, int selectedItem,
            int spinnerType);
}
