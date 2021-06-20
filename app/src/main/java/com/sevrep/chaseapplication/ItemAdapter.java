package com.sevrep.chaseapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemModelViewHolder> {

    private final Context context;
    private final List<ItemModel> itemModelList;
    private static OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDelClick(int position);
    }

    public static void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public ItemAdapter(Context context, List<ItemModel> itemModelList) {
        this.context = context;
        this.itemModelList = itemModelList;
    }

    @NonNull
    @Override
    public ItemModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
        return new ItemModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemModelViewHolder holder, int position) {
        holder.txtName.setText(itemModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    static class ItemModelViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        ImageView imgDelete, imgEdit;

        public ItemModelViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgDelete.setOnClickListener(v -> {
                if (mlistener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mlistener.onDelClick(position);
                    }
                }
            });

            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgEdit.setOnClickListener(v -> {
                if (mlistener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mlistener.onEditClick(position);
                    }
                }
            });
        }

    }
}
