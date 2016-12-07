package com.pricelinepartnernetwork.myhomework.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.pricelinepartnernetwork.myhomework.models.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends BaseAdapter implements SpinnerAdapter {
    private List<Course> courses;

    public CourseAdapter() {
        courses = new ArrayList<>();
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int i) {
        return courses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return courses.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        TextView textView;
        if ( convertView == null || !(convertView instanceof TextView) ) {
            textView = (TextView) View.inflate(viewGroup.getContext(), android.R.layout.simple_spinner_item, null);
        } else {
            textView = (TextView) convertView;
        }

        textView.setText(courses.get(i).getName());

        return textView;
    }
}
