package com.pricelinepartnernetwork.myhomework.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.pricelinepartnernetwork.myhomework.R;
import com.pricelinepartnernetwork.myhomework.models.Assignment;
import com.pricelinepartnernetwork.myhomework.repositories.AssignmentRepository;
import com.tippingcanoe.eon.Eon;
import com.tippingcanoe.eon.TimeBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {
    private List<Assignment> assignments;
    private OnEditAssignmentListener onEditAssignmentListener;

    public AssignmentAdapter(OnEditAssignmentListener onEditAssignmentListener) {
        assignments = new ArrayList<>();
        this.onEditAssignmentListener = onEditAssignmentListener;

        setHasStableIds(true);
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_cell, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Assignment assignment = assignments.get(position);

        holder.done.setChecked(assignment.isDone());
        holder.done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                assignment.setDone(b);
                AssignmentRepository.instance().updateAssignment(assignment);
            }
        });

        holder.title.setText(assignment.getTitle());

        StringBuilder infoStringBuilder = new StringBuilder();
        if ( assignment.getDueDate() != null ) {
            infoStringBuilder.append("Due: ");
            infoStringBuilder.append(Eon.getRelativeDate(assignment.getDueDate(), Eon.Length.MEDIUM, 2, false, true, TimeBuilder.TimeFrames.MONTH.getMilliseconds(), ", ", " and ", TimeBuilder.TimeFrames.MINUTE, TimeBuilder.TimeFrames.MONTH, holder.info.getContext()));
            infoStringBuilder.append(" \u2022 ");
        }
        infoStringBuilder.append(assignment.getCourse().getName());

        holder.info.setText(infoStringBuilder.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditAssignmentListener.onEditAssignment(assignment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    @Override
    public long getItemId(int position) {
        return assignments.get(position).getId();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.done)
        CheckBox done;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.info)
        TextView info;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnEditAssignmentListener {
        void onEditAssignment(Assignment assignment );
    }
}
