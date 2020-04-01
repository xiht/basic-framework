package com.hunterxi.lib.customView.menu;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewDebug.CapturedViewProperty;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hunterxi.lib.R;
import com.hunterxi.lib.utils.StringUtil;


/**
 * 菜单项
 */
public class MenuItem extends LinearLayout {
    private TextView text;

    private ImageView image;

    private Context context;

    private int id;

    // 点击效果中图片和文本颜色的定义均来自网络数据的时候使用这些变量
    private String defaultImage;
    private String selectedImage;
    private String defaultTextColor;
    private String selectedTextColor;

    public MenuItem(Context context, int id) {
        super(context);
        this.context = context;
        this.id = id;
        init();
    }

    public MenuItem(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @Override
    @CapturedViewProperty
    public int getId() {
        return id;
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.menu_item, this);
        text = findViewById(R.id.text);
        image = findViewById(R.id.image);

        text.setGravity(Gravity.CENTER);
        text.setTextColor(getResources().getColor(R.color.bottom_menu_text_color));
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
    }

    public MenuItem setIcon(int resId) {
        image.setBackgroundResource(resId);
        return this;
    }

    public MenuItem setLable(int resId) {
        text.setText(resId);
        return this;
    }

    public MenuItem setLable(String label) {
        text.setText(label);
        return this;
    }

    public MenuItem setTextColor(int resId) {
        text.setTextColor(getResources().getColor(resId));
        return this;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public void setSelectedImage(String seletedImage) {
        this.selectedImage = seletedImage;
    }

    public void setDefaultTextColor(String defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

    public void setSelectedTextColor(String selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        if (isSelected()) {
            if (StringUtil.isEmpty(selectedTextColor)) {
                text.setTextColor(getResources().getColor(R.color.bottom_menu_text_selected_color));
            }else {
                text.setTextColor(Color.parseColor(selectedTextColor));
            }

            if (!StringUtil.isEmpty(selectedImage)) {
                Glide.with(context).load(selectedImage).into(image);
            }
            return;
        }

        // 未选中状态下文本颜色和图片设置
        if (StringUtil.isEmpty(defaultTextColor)) {
            text.setTextColor(getResources().getColor(R.color.bottom_menu_text_color));
        }else {
            text.setTextColor(Color.parseColor(defaultTextColor));
        }
        if (!StringUtil.isEmpty(defaultImage)) {
            Glide.with(context).load(defaultImage).into(image);
        }
    }

}
