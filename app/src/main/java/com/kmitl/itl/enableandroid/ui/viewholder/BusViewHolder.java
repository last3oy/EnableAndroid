package com.kmitl.itl.enableandroid.ui.viewholder;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kmitl.itl.enableandroid.databinding.ItemBusBinding;
import com.kmitl.itl.enableandroid.model.Bus;
import com.kmitl.itl.enableandroid.ui.adapter.BusAdapter;

import java.util.Random;

import static com.kmitl.itl.enableandroid.ui.adapter.BusAdapter.*;

public class BusViewHolder extends RecyclerView.ViewHolder {

    private ItemBusBinding mBinding;

    public BusViewHolder(View itemView) {
        super(itemView);
        mBinding = DataBindingUtil.bind(itemView);
    }

    public void bind(Bus bus, ItemClickListener listener) {
        float density = (float) bus.getSeat() / bus.getCount();
        mBinding.tvBusNumber.setText(bus.getBunNumber());
        mBinding.tvTimeBusArrive.setText(String.format("%d", new Random().nextInt(30) + 1));
        mBinding.tvVacantSeat.setText(String.format("%d", bus.getSeat()));
        if (density > 0 && density < 0.3) {
            mBinding.vDensityPopulation.setBackgroundColor(Color.GREEN);
        } else if (density >= 0.3 && density < 0.5) {
            mBinding.vDensityPopulation.setBackgroundColor(Color.YELLOW);
        } else if (density >= 0.5 && density < 0.7) {
            mBinding.vDensityPopulation.setBackgroundColor(Color.rgb(255, 157, 255));
        } else {
            mBinding.vDensityPopulation.setBackgroundColor(Color.RED);
        }
        mBinding.btSub.setOnClickListener(v -> listener.onItemClick(bus));
    }
}
