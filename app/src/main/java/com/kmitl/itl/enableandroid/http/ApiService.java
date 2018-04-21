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

//    http://enable.somee.com/api/Bus?firststoplat=13.730329&laststoplat=13.722049
    @GET("Bus")
    @Gson
    Observable<Bus> getBus(@Query("firststoplat") Double startLat,
                           @Query("laststoplat") Double endLat);

}
