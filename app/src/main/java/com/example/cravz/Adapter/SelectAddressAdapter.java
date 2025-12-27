package com.example.cravz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cravz.Domain.SelectedAddress;
import com.example.cravz.R;

import java.util.List;

public class SelectAddressAdapter extends RecyclerView.Adapter<SelectAddressAdapter.ViewHolder> {

    public interface OnAddressSelectedListener {
        void onAddressSelected(SelectedAddress address);
    }

    private final Context context;
    private final List<SelectedAddress> addressList;
    private final OnAddressSelectedListener listener;
    private int selectedPosition = -1;

    public SelectAddressAdapter(Context context, List<SelectedAddress> addressList, OnAddressSelectedListener listener) {
        this.context = context;
        this.addressList = addressList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_select_address_item_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelectedAddress address = addressList.get(position);

        // FIXED: Display proper address
        holder.tvAddress.setText(address.getFullAddress());

        holder.rbSelect.setChecked(position == selectedPosition || address.isSelected());

        holder.itemView.setOnClickListener(v -> selectPosition(position));
        holder.rbSelect.setOnClickListener(v -> selectPosition(position));
    }

    private void selectPosition(int position) {
        if (position == selectedPosition) return;
        if (selectedPosition != -1) {
            addressList.get(selectedPosition).setSelected(false);
            notifyItemChanged(selectedPosition);
        }
        selectedPosition = position;
        addressList.get(position).setSelected(true);
        notifyItemChanged(position);
        if (listener != null) listener.onAddressSelected(addressList.get(position));
    }

    public SelectedAddress getSelectedAddress() {
        if (selectedPosition >= 0 && selectedPosition < addressList.size()) {
            return addressList.get(selectedPosition);
        }
        // fallback to any item which has selected == true
        for (SelectedAddress a : addressList) if (a.isSelected()) return a;
        return null;
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress;
        RadioButton rbSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address);
            rbSelect = itemView.findViewById(R.id.rb_select);
        }
    }
}
