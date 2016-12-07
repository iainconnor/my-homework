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

public class AddAssignmentActivity extends AppCompatActivity {
    public static int CREATE_ASSIGNMENT_REQUEST_CODE = 1;

    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.dueDate)
    EditText dueDate;
    @BindView(R.id.course)
    Spinner course;
    @BindView(R.id.description)
    EditText description;

    CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        ButterKnife.bind(this);

        courseAdapter = new CourseAdapter();

        setResult(Activity.RESULT_CANCELED);

        setupCourseList();
    }

    private void setupCourseList() {
        course.setAdapter(new CourseAdapter());
        CourseRepository.instance().getAll(new BaseRepository.FetchDataCallback<Course>() {
            @Override
            public void onDataRetrieved(List<Course> data) {
                courseAdapter.setCourses(data);
                course.setAdapter(courseAdapter);
            }

            @Override
            public void onError(String error) {
                Snackbar.make(course, error, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getItemId() == R.id.action_save ) {
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

            Assignment assignment = new Assignment(AssignmentRepository.instance().getNextId(), title.getText().toString());
            assignment.setCourse((Course) course.getSelectedItem());
            assignment.setDescription(description.getText().toString());
            assignment.setDueDate(convertedDate);

            AssignmentRepository.instance().addAssignment(assignment);

            setResult(Activity.RESULT_OK);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
