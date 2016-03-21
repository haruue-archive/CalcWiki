package org.calcwiki.ui.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.calcwiki.R;
import org.calcwiki.ui.item.MainDrawerMenuItem;

import java.util.List;

public class MainDrawerMenuAdapter extends RecyclerArrayAdapter<MainDrawerMenuItem> {

    public MainDrawerMenuAdapter(Context context) {
        super(context);
    }

    public MainDrawerMenuAdapter(Context context, List<MainDrawerMenuItem> objects) {
        super(context, objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainDrawerMenuViewHolder(parent, R.layout.item_menu_with_icon);
    }

    class MainDrawerMenuViewHolder extends BaseViewHolder<MainDrawerMenuItem> {

        ImageView iconView;
        TextView textView;

        public MainDrawerMenuViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            iconView = $(R.id.icon_in_menu_item);
            textView = $(R.id.title_in_menu_item);
        }


        @Override
        public void setData(MainDrawerMenuItem data) {
            super.setData(data);
            if (data.getIconId() == -1) {
                iconView.setVisibility(View.INVISIBLE);
            } else {
                iconView.setImageDrawable(getContext().getResources().getDrawable(data.getIconId()));
            }
            textView.setText(data.getTitle());
        }
    }
}
