package com.pricelinepartnernetwork.myhomework;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pricelinepartnernetwork.myhomework.adapters.AssignmentAdapter;
import com.pricelinepartnernetwork.myhomework.models.Assignment;
import com.pricelinepartnernetwork.myhomework.repositories.AssignmentRepository;
import com.pricelinepartnernetwork.myhomework.repositories.BaseRepository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.content_main)
    RecyclerView contentMain;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AssignmentAdapter assignmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openAssignmentIntent = new Intent(MainActivity.this, AddAssignmentActivity.class);
                startActivityForResult(openAssignmentIntent, AddAssignmentActivity.CREATE_ASSIGNMENT_REQUEST_CODE);
            }
        });

        setupList();
    }

    private void setupList() {
        assignmentAdapter = new AssignmentAdapter();
        contentMain.setAdapter(assignmentAdapter);
        contentMain.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == AddAssignmentActivity.CREATE_ASSIGNMENT_REQUEST_CODE && resultCode == RESULT_OK ) {
            AssignmentRepository.instance().getAll(new BaseRepository.FetchDataCallback<Assignment>() {
                @Override
                public void onDataRetrieved(List<Assignment> data) {
                    assignmentAdapter.setAssignments(data);
                }

                @Override
                public void onError(String error) {
                    Snackbar.make(contentMain, error, Snackbar.LENGTH_SHORT);
                }
            });
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
