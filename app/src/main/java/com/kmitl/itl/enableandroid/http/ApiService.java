package com.kmitl.itl.enableandroid.http;

import com.kmitl.itl.enableandroid.model.Bus;
import com.kmitl.itl.enableandroid.model.PlaceSearchResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/xml?type=bus_station")
    @SimpleXml
    Observable<PlaceSearchResponse> searchBusStation(@Query("location") String latLng,
                                                     @Query("radius") long radius);

    @GET("api/Bus/{busId}")
    @Gson
    Observable<Bus> getBus(@Path("busId") String busId);

}
