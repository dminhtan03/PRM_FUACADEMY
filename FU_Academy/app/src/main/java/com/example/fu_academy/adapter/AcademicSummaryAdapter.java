package com.example.fu_academy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fu_academy.R;
import com.example.fu_academy.viewmodel.AcademicSummaryViewModel;

import java.util.List;

public class AcademicSummaryAdapter extends RecyclerView.Adapter<AcademicSummaryAdapter.SummaryViewHolder> {
    private List<AcademicSummaryViewModel.SemesterSummary> summaries;

    public AcademicSummaryAdapter(List<AcademicSummaryViewModel.SemesterSummary> summaries) {
        this.summaries = summaries;
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_academic_summary, parent, false);
        return new SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        AcademicSummaryViewModel.SemesterSummary summary = summaries.get(position);
        holder.tvSemester.setText(summary.semester);
        holder.tvYear.setText(summary.year);
        holder.tvGPA.setText(String.format("%.2f", summary.gpa));
        holder.tvCredits.setText(String.valueOf(summary.credits));
        holder.tvPassed.setText(String.valueOf(summary.passed));
        holder.tvFailed.setText(String.valueOf(summary.failed));
        holder.tvRank.setText("#" + summary.rank);
        holder.tvRemark.setText(summary.remark);
    }

    @Override
    public int getItemCount() {
        return summaries != null ? summaries.size() : 0;
    }

    public void updateData(List<AcademicSummaryViewModel.SemesterSummary> newItems) {
        this.summaries = newItems;
        notifyDataSetChanged();
    }

    static class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView tvSemester, tvYear, tvGPA, tvCredits, tvPassed, tvFailed, tvRank, tvRemark;

        SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSemester = itemView.findViewById(R.id.tvSemester);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvGPA = itemView.findViewById(R.id.tvGPA);
            tvCredits = itemView.findViewById(R.id.tvCredits);
            tvPassed = itemView.findViewById(R.id.tvPassed);
            tvFailed = itemView.findViewById(R.id.tvFailed);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvRemark = itemView.findViewById(R.id.tvRemark);
        }
    }
}




