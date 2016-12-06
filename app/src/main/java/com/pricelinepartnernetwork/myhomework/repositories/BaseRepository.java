package com.pricelinepartnernetwork.myhomework.repositories;

import java.util.ArrayList;

public abstract class BaseRepository<T> {
    public abstract void getById(long id, FetchDataCallback<T> callback);
    public abstract void getAll(FetchDataCallback<T> callback);

    public interface FetchDataCallback<T> {
        void onDataRetrieved(ArrayList<T> data);
        void onError(String error);
    }
}
