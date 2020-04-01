package com.hunterxi.lib.customView.menu;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.hunterxi.lib.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Menu implements OnClickListener {

    public static final int MENU_MORE_ID = 9999128;
    private static final int MAX_MENU_COUNT = 5;
    private Map<Integer, Boolean> bottomIds = new HashMap<Integer, Boolean>();
    private List<MenuItem> items = new ArrayList<MenuItem>();
    private List<MenuLine> menuLines;
    private LinearLayout menuBottomGroup;
    private PopupWindow popupWindow;

    private MenuItemEvent menuEvent;
    private int menuLineHeight;

    public Menu(ViewGroup menuParent, MenuItemEvent menuEvent) {
        this.menuBottomGroup = (LinearLayout) menuParent;
        this.menuEvent = menuEvent;
        menuLineHeight = menuBottomGroup.getResources().getDimensionPixelOffset(R.dimen.menu_line_height);
    }

    public void init(List<MenuItem> items) {
        this.items.clear();
        if (items != null && !items.isEmpty()) {
            this.items.addAll(items);
        }
        updateMenu();
        mesureMenus();
        loadBottomMenus();
        registerListener();
    }

    public void init() {
        updateMenu();
        mesureMenus();
        loadBottomMenus();
        registerListener();
    }

    private void updateMenu() {
        menuBottomGroup.removeAllViews();
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    private void mesureMenus() {
        if (items == null || items.isEmpty()) {
            menuBottomGroup.setVisibility(View.GONE);
            return;
        }

        if (menuLines == null) {
            menuLines = new ArrayList<MenuLine>();
        } else {
            menuLines.clear();
        }

        if (items.size() <= MAX_MENU_COUNT) {
            menuLines.add(new MenuLine(menuBottomGroup.getContext(), items));
            return;
        }

        List<MenuItem> bottomMenus = items.subList(0, MAX_MENU_COUNT - 1);
        updateBottomCacheIds(bottomMenus);
        menuLines.add(new MenuLine(menuBottomGroup.getContext(), bottomMenus));

        List<MenuItem> allPopMenus = items.subList(MAX_MENU_COUNT, items.size());
        int linesCount = (allPopMenus.size() + MAX_MENU_COUNT - 1) / MAX_MENU_COUNT;
        for (int i = 0; i < linesCount; i++) {
            int startIndex = i * MAX_MENU_COUNT;
            int endIndex = (i == linesCount - 1) ? allPopMenus.size() : (i + 1) * MAX_MENU_COUNT;
            List<MenuItem> lineItems = allPopMenus.subList(startIndex, endIndex);
            MenuLine menuLine = new MenuLine(menuBottomGroup.getContext(), lineItems);
            menuLines.add(menuLine);
        }
    }

    private void updateBottomCacheIds(List<MenuItem> bottomItems) {
        for (MenuItem bottomItem : bottomItems) {
            bottomIds.put(bottomItem.getId(), true);
        }
    }


    private void loadBottomMenus() {
        if (menuLines == null || menuLines.isEmpty()) {
            return;
        }
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        MenuLine bottomMenu = menuLines.get(0);
        params.gravity = Gravity.CENTER_VERTICAL;
        menuBottomGroup.addView(bottomMenu, params);
    }

    private void registerListener() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        MenuItem item = (MenuItem) v;
        int id = item.getId();
//		//menu拦截事件后先处理
//		preHandleEvent(id);
        //将事件抛给UI,UI可能需要处理
        if (menuEvent != null && id != MENU_MORE_ID) {
            menuEvent.onMenuEvent(id);
        }

    }

    private void preHandleEvent(int id) {
        if (id == MENU_MORE_ID) {
            showMoreMenu();
        } else {

            Boolean moreObj = bottomIds.get(id);
            boolean isMore = moreObj == null;
            if (!isMore) {
                for (MenuItem it : items) {
                    it.setSelected(!it.isSelected());
                }
            } else {
                for (MenuItem it : items) {
                    if (it.getId() == id) {
                        it.setSelected(true);
                    } else {
                        it.setSelected(false);
                    }
                }

            }
        }
    }

    public void showMoreMenu() {
        if (popupWindow == null) {
            LinearLayout popGroup = (LinearLayout) LayoutInflater.from(menuBottomGroup.getContext()).inflate(R.layout.menu_pop, null);
            for (int i = 1; i < menuLines.size(); i++) {
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, menuLineHeight);
                LinearLayout menuLine = menuLines.get(i);
                menuLine.setWeightSum(5);
                popGroup.addView(menuLine, params);
            }
            popupWindow = new PopupWindow(menuBottomGroup.getContext());
            popupWindow.setContentView(popGroup);
            popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
            popupWindow.setWidth(-1);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setHeight((menuLines.size() - 1) * menuLineHeight);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
        popupWindow.showAsDropDown(menuBottomGroup);
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
        updateMenu();
    }

    public void setCurrentItem(int id) {
        preHandleEvent(id);
    }

//	public void showTips(int itemId)
//	{
//		if(items == null)
//			return;
//		for(MenuItem item:items)
//		{
//			if(item.getId() == itemId)
//			{
//				item.showTips();
//				break;
//			}
//		}
//	}
//
//	public void hideTips(int itemId)
//	{
//		if(items == null)
//			return;
//		for(MenuItem item:items)
//		{
//			if(item.getId() == itemId)
//			{
//				item.hideTips();
//				break;
//			}
//		}
//	}

    public void switchTabs(int itemId) {
        if (items == null) {
            return;
        }

        MenuItem willMenu = null;
        for (MenuItem item : items) {
            if (item.getId() == itemId) {
                willMenu = item;
                break;
            }
        }

        if (willMenu != null) {
            onClick(willMenu);
        }
    }

    public List<MenuItem> getMenuItemList() {
        return items;
    }

    public interface MenuItemEvent {
        void onMenuEvent(int id);
    }
}
