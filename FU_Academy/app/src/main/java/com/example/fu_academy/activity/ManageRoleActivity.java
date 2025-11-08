package com.example.fu_academy.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.adapter.UserAdapter;
import com.example.fu_academy.entity.User;
import com.example.fu_academy.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class ManageRoleActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private EditText edtSearch;
    private Spinner spinnerRoleFilter, spinnerStatusFilter;
    private Button btnFilter;
    private TextView txtPagination;
    private List<User> userList = new ArrayList<>();
    private int currentPage = 1;
    private int itemsPerPage = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_role);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Quản Lý Vai Trò");
        }

        recyclerView = findViewById(R.id.recyclerView);
        edtSearch = findViewById(R.id.edtSearch);
        spinnerRoleFilter = findViewById(R.id.spinnerRoleFilter);
        spinnerStatusFilter = findViewById(R.id.spinnerStatusFilter);
        btnFilter = findViewById(R.id.btnFilter);
        txtPagination = findViewById(R.id.txtPagination);

        // Setup role filter spinner
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoleFilter.setAdapter(roleAdapter);

        // Setup status filter spinner
        String[] statusArray = {"", "active", "inactive", "pending"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusArray);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatusFilter.setAdapter(statusAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(userList, this::onUserAction);
        recyclerView.setAdapter(adapter);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.userList.observe(this, users -> {
            if (users != null) {
                userList.clear();
                userList.addAll(users);
                updatePagination();
                adapter.notifyDataSetChanged();
            }
        });

        btnFilter.setOnClickListener(v -> {
            String roleFilter = spinnerRoleFilter.getSelectedItem().toString();
            String statusFilter = spinnerStatusFilter.getSelectedItem().toString();
            String emailFilter = edtSearch.getText().toString().trim();
            userViewModel.getUsersWithFilter(roleFilter, emailFilter, statusFilter);
        });

        // Load all users on start
        userViewModel.getAllUsers();
    }

    private void onUserAction(User user, String action) {
        if ("change_role".equals(action)) {
            // Show dialog to change role
            String[] roles = {"student", "lecturer"};
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Thay đổi vai trò");
            builder.setItems(roles, (dialog, which) -> {
                userViewModel.updateUserRole(user.user_id, roles[which]);
            });
            builder.show();
        } else if ("change_status".equals(action)) {
            // Show dialog to change status
            String[] statuses = {"active", "inactive", "pending"};
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Thay đổi trạng thái");
            builder.setItems(statuses, (dialog, which) -> {
                userViewModel.updateUserStatus(user.user_id, statuses[which]);
            });
            builder.show();
        }
    }

    private void updatePagination() {
        int total = userList.size();
        int start = (currentPage - 1) * itemsPerPage + 1;
        int end = Math.min(currentPage * itemsPerPage, total);
        txtPagination.setText(String.format("Hiển thị %d-%d của %d", start, end, total));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

