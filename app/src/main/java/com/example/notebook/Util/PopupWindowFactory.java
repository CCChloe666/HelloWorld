package com.example.notebook.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;

import com.example.notebook.Adapter.PopUpMenuAdapter;

public class PopupWindowFactory {

    public static ListPopupWindow createListPopUpWindow(Context context, View anchor, int[] icons,
                                                        String[] texts, final ListPopUpWindowItemClickListener listener) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
        PopUpMenuAdapter adapter = new PopUpMenuAdapter(context, icons, texts);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setAnchorView(anchor);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                listener.onItemClick(parent, view, position, id);
                listPopupWindow.dismiss();
            }
        });
        return listPopupWindow;
    }

    public static PopupMenu createAndShowPopupMenu(Context context, View anchor, int menuRes, PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu popup = new PopupMenu(context, anchor);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());
        popup.setOnMenuItemClickListener(listener);
        popup.show();
        return popup;
    }

    public interface ListPopUpWindowItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
}
