package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.database.EducationDatabase;
import com.example.fu_academy.entity.Material;
import com.example.fu_academy.entity.User;

import java.util.List;

public class MaterialListAdapter extends RecyclerView.Adapter<MaterialListAdapter.MaterialViewHolder> {
    private List<Material> materialList;
    private OnMaterialClickListener listener;
    private EducationDatabase db;

    public interface OnMaterialClickListener {
        void onMaterialClick(Material material);
    }

    public MaterialListAdapter(List<Material> materialList, OnMaterialClickListener listener, EducationDatabase db) {
        this.materialList = materialList;
        this.listener = listener;
        this.db = db;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_material_list, parent, false);
        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        Material material = materialList.get(position);
        holder.txtTitle.setText(material.getTitle());
        
        // Set type badge
        String type = material.getType() != null ? material.getType() : "N/A";
        holder.txtType.setText(type);
        
        holder.txtFileSize.setText("Kích thước: " + (material.getFile_size() != null ? material.getFile_size() : "N/A"));
        holder.txtUploadDate.setText("Ngày: " + (material.getUpload_date() != null ? material.getUpload_date() : "N/A"));
        
        String ownerName = "N/A";
        if (db != null && material.getOwner_id() > 0) {
            User owner = db.userDao().getUserById(material.getOwner_id());
            if (owner != null) {
                ownerName = owner.getName();
            }
        }
        holder.txtOwner.setText("Người tải: " + ownerName);
        
        holder.txtDescription.setText(material.getDescription() != null ? material.getDescription() : "N/A");
        holder.txtLink.setText(material.getFile_url() != null ? material.getFile_url() : "N/A");
        
        holder.btnDownload.setOnClickListener(v -> {
            // Handle download
        });
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMaterialClick(material);
            }
        });
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    static class MaterialViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtType, txtFileSize, txtUploadDate, txtOwner, txtDescription, txtLink;
        Button btnDownload;

        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtType = itemView.findViewById(R.id.txtType);
            txtFileSize = itemView.findViewById(R.id.txtFileSize);
            txtUploadDate = itemView.findViewById(R.id.txtUploadDate);
            txtOwner = itemView.findViewById(R.id.txtOwner);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtLink = itemView.findViewById(R.id.txtLink);
            btnDownload = itemView.findViewById(R.id.btnDownload);
        }
    }
}

