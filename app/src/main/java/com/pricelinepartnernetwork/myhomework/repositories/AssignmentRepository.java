package com.pricelinepartnernetwork.myhomework.repositories;

import com.pricelinepartnernetwork.myhomework.models.Assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AssignmentRepository extends BaseRepository<Assignment> {
    private static AssignmentRepository instance;
    private List<Assignment> assignments;

    public static AssignmentRepository instance () {
        if (instance == null) {
            instance = new AssignmentRepository();
        }

        return instance;
    }

    private AssignmentRepository() {
        assignments = new ArrayList<>();
    }

    @Override
    public void getById(long id, FetchDataCallback<Assignment> callback) {
        for ( Assignment assignment : assignments ) {
            if ( assignment.getId() == id ) {
                callback.onDataRetrieved(Collections.singletonList(assignment));
                return;
            }
        }

        callback.onError("Sorry, couldn't find an Assignment with id " + id);
    }

    @Override
    public void getAll(FetchDataCallback<Assignment> callback) {
        // Let's order them by due-date or alphabetical first.
        Collections.sort(assignments, new Comparator<Assignment>() {
            @Override
            public int compare(Assignment assignment1, Assignment assignment2) {
                if ( assignment1.getDueDate() != null && assignment2.getDueDate() != null ) {
                    if ( !assignment1.getDueDate().equals(assignment2.getDueDate()) ) {
                        return assignment1.getDueDate().compareTo(assignment2.getDueDate());
                    }
                }

                return assignment1.getTitle().compareTo(assignment2.getTitle());
            }
        });

        callback.onDataRetrieved(assignments);
    }

    @Override
    public long getNextId() {
        long maxId = 0;

        for ( Assignment assignment : assignments ) {
            if ( assignment.getId() > maxId ) {
                maxId = assignment.getId();
            }
        }

        return maxId + 1;
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void removeAssigment(Assignment toRemove) {
        for ( Assignment assignment : assignments ) {
            if ( assignment.getId() == toRemove.getId() ) {
                assignments.remove(assignment);
                return;
            }
        }
    }
}
