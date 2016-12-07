package com.pricelinepartnernetwork.myhomework.repositories;

import java.util.List;

public abstract class BaseRepository<T> {
    public abstract void getById(long id, FetchDataCallback<T> callback);
    public abstract void getAll(FetchDataCallback<T> callback);
    public abstract long getNextId();

    public interface FetchDataCallback<T> {
        void onDataRetrieved(List<T> data);
        void onError(String error);
    }
}
