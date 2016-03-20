package org.calcwiki.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

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
        return null;
    }
}
