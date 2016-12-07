package com.pricelinepartnernetwork.myhomework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pricelinepartnernetwork.myhomework.models.Assignment;
import com.pricelinepartnernetwork.myhomework.repositories.AssignmentRepository;
import com.pricelinepartnernetwork.myhomework.repositories.BaseRepository;
import com.tippingcanoe.eon.Eon;
import com.tippingcanoe.eon.TimeBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAssignmentActivity extends AppCompatActivity {
    public static final String EXTRA_ASSIGNMENT_ID = "assignment_id";

    @BindView(R.id.done)
    CheckBox done;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.activity_view_assignment)
    ScrollView activityViewAssignment;

    Assignment assignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);
        ButterKnife.bind(this);

        long assignmentId;
        if ( savedInstanceState != null ) {
            assignmentId = savedInstanceState.getLong(EXTRA_ASSIGNMENT_ID);
        } else {
            assignmentId = getIntent().getExtras().getLong(EXTRA_ASSIGNMENT_ID);
        }

        assignment = new Assignment(assignmentId, "Loading...");
        refresh();

        setResult(Activity.RESULT_CANCELED);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(EXTRA_ASSIGNMENT_ID, assignment.getId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getItemId() == android.R.id.home ) {
            finish();

            return true;
        } else if (item.getItemId() == R.id.action_edit) {
            Intent editAssignmentIntent = new Intent(this, EditAssignmentActivity.class);
            editAssignmentIntent.putExtra(EditAssignmentActivity.EXTRA_ASSIGNMENT_ID, assignment.getId());
            startActivityForResult(editAssignmentIntent, EditAssignmentActivity.EDIT_ASSIGNMENT_REQUEST_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == EditAssignmentActivity.EDIT_ASSIGNMENT_REQUEST_CODE && (resultCode == EditAssignmentActivity.DELETE_ASSIGNMENT_RESULT_CODE || resultCode == EditAssignmentActivity.EDIT_ASSIGNMENT_RESULT_CODE) ) {
            setResult(RESULT_OK);

            if ( resultCode == EditAssignmentActivity.DELETE_ASSIGNMENT_RESULT_CODE ) {
                finish();
            } else {
                refresh();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void refresh() {
        AssignmentRepository.instance().getById(assignment.getId(), new BaseRepository.FetchDataCallback<Assignment>() {
            @Override
            public void onDataRetrieved(List<Assignment> data) {
                assignment = data.get(0);

                title.setText(assignment.getTitle());
                done.setChecked(assignment.isDone());
                done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        assignment.setDone(b);
                        AssignmentRepository.instance().updateAssignment(assignment);
                        setResult(RESULT_OK);
                    }
                });

                StringBuilder infoStringBuilder = new StringBuilder();
                if ( assignment.getDueDate() != null ) {
                    infoStringBuilder.append("Due: ");
                    infoStringBuilder.append(Eon.getRelativeDate(assignment.getDueDate(), Eon.Length.MEDIUM, 2, false, true, TimeBuilder.TimeFrames.MONTH.getMilliseconds(), ", ", " and ", TimeBuilder.TimeFrames.MINUTE, TimeBuilder.TimeFrames.MONTH, ViewAssignmentActivity.this));
                    infoStringBuilder.append(" \u2022 ");
                }
                infoStringBuilder.append(assignment.getCourse().getName());
                info.setText(infoStringBuilder.toString());

                description.setText(assignment.getDescription());
            }

            @Override
            public void onError(String error) {
                Snackbar.make(title, "Could not find that assignment.", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
