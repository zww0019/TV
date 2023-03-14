package com.fongmi.android.tv.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.bean.Class;
import com.fongmi.android.tv.databinding.AdapterTypeBinding;
import com.fongmi.android.tv.utils.ResUtil;

import java.util.ArrayList;
import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {

    private final OnClickListener mListener;
    private final List<Class> mItems;

    public TypeAdapter(OnClickListener listener) {
        this.mListener = listener;
        this.mItems = new ArrayList<>();
    }

    public interface OnClickListener {

        void onItemClick(int position, Class item);
    }

    private Class home() {
        Class type = new Class();
        type.setTypeName(ResUtil.getString(R.string.home));
        type.setTypeId("home");
        type.setActivated(true);
        return type;
    }

    public void clear() {
        mItems.clear();
        mItems.add(home());
        notifyDataSetChanged();
    }

    public void addAll(List<Class> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void setActivated(int position) {
        for (Class item : mItems) item.setActivated(false);
        mItems.get(position).setActivated(true);
        notifyItemRangeChanged(0, mItems.size());
    }

    public List<Class> getTypes() {
        return mItems;
    }

    public Class get(int position) {
        return mItems.get(position);
    }

    public boolean hasType() {
        return getItemCount() > 1;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterTypeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class item = mItems.get(position);
        holder.binding.text.setText(item.getTypeName());
        holder.binding.text.setActivated(item.isActivated());
        holder.binding.text.setOnClickListener(v -> mListener.onItemClick(position, item));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final AdapterTypeBinding binding;

        ViewHolder(@NonNull AdapterTypeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}