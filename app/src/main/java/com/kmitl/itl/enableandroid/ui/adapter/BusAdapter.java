package com.kmitl.itl.enableandroid.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kmitl.itl.enableandroid.R;
import com.kmitl.itl.enableandroid.ui.viewholder.BusViewHolder;
import com.kmitl.itl.enableandroid.ui.viewholder.HeaderViewHolder;

public class BusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_TYPE = 0;
    private static final int ITEM_TYPE = 1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        switch (viewType) {
            case HEADER_TYPE:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_bus_header, parent, false);
                return new HeaderViewHolder(rootView);
            case ITEM_TYPE:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_bus, parent, false);
                return new BusViewHolder(rootView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE) {

        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER_TYPE : ITEM_TYPE;
    }
}
