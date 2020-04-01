package com.hunterxi.lib.customView.menu;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.hunterxi.lib.R;

import java.util.List;


public class MenuLine extends LinearLayout {

    private List<MenuItem> items;
    private int menuItemMargin;

    public MenuLine(Context context, List<MenuItem> items) {
        super(context);
        this.items = items;
        menuItemMargin = getResources().getDimensionPixelOffset(R.dimen.menu_item_margin_top_bottom);
        init();
    }

    private void init() {
        if (items == null || items.isEmpty()) {
            return;
        }
        setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams itemParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        itemParams.weight = 1;
        itemParams.topMargin = menuItemMargin;
        itemParams.bottomMargin = menuItemMargin;
        itemParams.gravity = Gravity.CENTER_VERTICAL;

        for (MenuItem item : items) {
            addView(item, itemParams);
        }
    }
}
