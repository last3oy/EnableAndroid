package com.kmitl.itl.enableandroid.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kmitl.itl.enableandroid.R;
import com.kmitl.itl.enableandroid.model.Bus;
import com.kmitl.itl.enableandroid.ui.viewholder.BusViewHolder;
import com.kmitl.itl.enableandroid.ui.viewholder.HeaderViewHolder;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ItemClickListener {
        void onItemClick(Bus bus);
    }

    private static final int HEADER_TYPE = 0;
    private static final int ITEM_TYPE = 1;
    private List<Bus> mBus;
    private ItemClickListener mListener;

    public BusAdapter(List<Bus> bus, ItemClickListener listener) {
        mBus = bus;
        mListener = listener;
    }

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
            BusViewHolder busViewHolder = (BusViewHolder) holder;
            Bus bus = mBus.get(position - 1);
            busViewHolder.bind(bus, mListener);
        }
    }

    @Override
    public int getItemCount() {
        if (mBus == null) {
            return 1;
        }
        return mBus.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER_TYPE : ITEM_TYPE;
    }

    public void setBus(List<Bus> mBus) {
        this.mBus = mBus;
        notifyDataSetChanged();
    }
}
