package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.entity.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private OnUserActionListener listener;

    public interface OnUserActionListener {
        void onUserAction(User user, String action);
    }

    public UserAdapter(List<User> userList, OnUserActionListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtEmail.setText(user.email);
        holder.txtRole.setText("Vai trò: " + user.role);
        holder.txtStatus.setText("Trạng thái: " + (user.status != null ? user.status : "N/A"));

        holder.btnChangeRole.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserAction(user, "change_role");
            }
        });

        holder.btnChangeStatus.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserAction(user, "change_status");
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtEmail, txtRole, txtStatus;
        Button btnChangeRole, btnChangeStatus;

        UserViewHolder(View itemView) {
            super(itemView);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtRole = itemView.findViewById(R.id.txtRole);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnChangeRole = itemView.findViewById(R.id.btnChangeRole);
            btnChangeStatus = itemView.findViewById(R.id.btnChangeStatus);
        }
    }
}

