package com.kmitl.itl.enableandroid.http;

import com.kmitl.itl.enableandroid.model.PlaceSearchResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    //TODO:
    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/xml?type=bus_station")
    Observable<PlaceSearchResponse> searchBusStation(@Query("location") String latLng,
                                                     @Query("radius") long radius);
}
