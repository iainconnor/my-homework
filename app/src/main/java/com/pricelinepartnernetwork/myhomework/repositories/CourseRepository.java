package com.pricelinepartnernetwork.myhomework.repositories;

import com.pricelinepartnernetwork.myhomework.models.Course;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CourseRepository extends BaseRepository<Course> {
    private List<Course> courses;
    private static CourseRepository instance;

    public static CourseRepository instance() {
        if ( instance == null ) {
            instance = new CourseRepository();
        }

        return instance;
    }

    private CourseRepository() {
        courses = Arrays.asList(
                new Course(1, "Math"),
                new Course(2, "Science"),
                new Course(3, "Programming")
        );
    }

    @Override
    public void getById(long id, FetchDataCallback<Course> callback) {
        for ( Course course : courses ) {
            if ( course.getId() == id ) {
                callback.onDataRetrieved(Collections.singletonList(course));
                return;
            }
        }

        callback.onError("Sorry, couldn't find a Course with id " + id);
    }

    @Override
    public void getAll(FetchDataCallback<Course> callback) {
        // Let's alphabetise them first.
        Collections.sort(courses, new Comparator<Course>() {
            @Override
            public int compare(Course course1, Course course2) {
                return course1.getName().compareTo(course2.getName());
            }
        });

        callback.onDataRetrieved(courses);
    }

    @Override
    public long getNextId() {
        long maxId = 0;

        for ( Course course : courses ) {
            if ( course.getId() > maxId ) {
                maxId = course.getId();
            }
        }

        return maxId + 1;
    }
}
