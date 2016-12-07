package com.pricelinepartnernetwork.myhomework;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import com.pricelinepartnernetwork.myhomework.adapters.CourseAdapter;
import com.pricelinepartnernetwork.myhomework.models.Assignment;
import com.pricelinepartnernetwork.myhomework.models.Course;
import com.pricelinepartnernetwork.myhomework.repositories.AssignmentRepository;
import com.pricelinepartnernetwork.myhomework.repositories.BaseRepository;
import com.pricelinepartnernetwork.myhomework.repositories.CourseRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAssignmentActivity extends AppCompatActivity {
    public static final int EDIT_ASSIGNMENT_REQUEST_CODE = 3;
    public static final int EDIT_ASSIGNMENT_RESULT_CODE = 1;
    public static final int DELETE_ASSIGNMENT_RESULT_CODE = 2;
    public static final String EXTRA_ASSIGNMENT_ID = "assignment_id";

    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.dueDate)
    EditText dueDate;
    @BindView(R.id.course)
    Spinner course;
    @BindView(R.id.description)
    EditText description;

    CourseAdapter courseAdapter;
    Assignment assignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assigment);
        ButterKnife.bind(this);

        courseAdapter = new CourseAdapter();

        long assignmentId;
        if ( savedInstanceState != null ) {
            assignmentId = savedInstanceState.getLong(EXTRA_ASSIGNMENT_ID);
        } else {
            assignmentId = getIntent().getExtras().getLong(EXTRA_ASSIGNMENT_ID);
        }

       AssignmentRepository.instance().getById(assignmentId, new BaseRepository.FetchDataCallback<Assignment>() {
            @Override
            public void onDataRetrieved(List<Assignment> data) {
                assignment = data.get(0);
            }

            @Override
            public void onError(String error) {
                Snackbar.make(title, "Could not find that assignment.", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        });

        setResult(Activity.RESULT_CANCELED);

        setupCourseList();
        title.setText(assignment.getTitle());
        if ( assignment.getDueDate() != null ) {
            DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
            dueDate.setText(dateFormat.format(assignment.getDueDate()));
        }
        description.setText(assignment.getDescription());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(EXTRA_ASSIGNMENT_ID, assignment.getId());
        super.onSaveInstanceState(outState);
    }

    private void setupCourseList() {
        course.setAdapter(new CourseAdapter());
        CourseRepository.instance().getAll(new BaseRepository.FetchDataCallback<Course>() {
            @Override
            public void onDataRetrieved(List<Course> data) {
                courseAdapter.setCourses(data);
                course.setAdapter(courseAdapter);

                for ( int i = 0; i < data.size(); i ++ ) {
                    if ( data.get(i).equals(assignment.getCourse()) ) {
                        course.setSelection(i, false);
                    }
                }
            }

            @Override
            public void onError(String error) {
                Snackbar.make(course, error, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_delete_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getItemId() == android.R.id.home ) {
            finish();

            return true;
        } else if ( item.getItemId() == R.id.action_save ) {
            if (TextUtils.isEmpty(title.getText())) {
                Snackbar.make(dueDate, "Error, you need a title.", Snackbar.LENGTH_SHORT).show();
                return false;
            }

            Date convertedDate = null;
            if (!TextUtils.isEmpty(dueDate.getText())) {
                try {
                    DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
                    convertedDate = dateFormat.parse(dueDate.getText().toString());
                } catch (ParseException e) {
                    Snackbar.make(dueDate, "Error, couldn't understand that date.", Snackbar.LENGTH_SHORT).show();
                    return false;
                }
            }

            assignment.setTitle(title.getText().toString());
            assignment.setCourse((Course) course.getSelectedItem());
            assignment.setDescription(description.getText().toString());
            assignment.setDueDate(convertedDate);

            AssignmentRepository.instance().updateAssignment(assignment);

            setResult(EDIT_ASSIGNMENT_RESULT_CODE);
            finish();

            return true;
        } else if ( item.getItemId() == R.id.action_delete ) {
            AssignmentRepository.instance().removeAssignment(assignment);

            setResult(DELETE_ASSIGNMENT_RESULT_CODE);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
