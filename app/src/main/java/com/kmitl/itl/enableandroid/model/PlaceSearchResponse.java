package com.kmitl.itl.enableandroid.model;

import com.google.android.gms.maps.model.LatLng;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "PlaceSearchResponse", strict = false)
public class PlaceSearchResponse {

    @ElementList(name = "result", required = false, entry = "result", inline = true)
    List<PlaceResult> result;

    @Element(name = "status", required = false)
    String status;

    public List<PlaceResult> getPlaceResult() {
        return this.result;
    }

    public void setPlaceResult(List<PlaceResult> value) {
        this.result = value;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public static class PlaceResult {

        @Element(name = "rating", required = false)
        String rating;

        @Element(name = "icon", required = false)
        String icon;

        @Element(name = "photo", required = false)
        Photo photo;

        @ElementList(name = "type", required = false, entry = "type", inline = true)
        List<String> type;

        @Element(name = "reference", required = false)
        String reference;

        @Element(name = "price_level", required = false)
        String priceLevel;

        @Element(name = "scope", required = false)
        String scope;

        @Element(name = "name", required = false)
        String name;

        @Element(name = "opening_hours", required = false)
        OpeningHours openingHours;

        @Element(name = "vicinity", required = false)
        String vicinity;

        @Element(name = "geometry", required = false)
        Geometry geometry;

        @Element(name = "id", required = false)
        String id;

        @Element(name = "place_id", required = false)
        String placeId;

        public String getRating() {
            return this.rating;
        }

        public void setRating(String value) {
            this.rating = value;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String value) {
            this.icon = value;
        }

        public Photo getPhoto() {
            return this.photo;
        }

        public void setPhoto(Photo value) {
            this.photo = value;
        }

        public List<String> getType() {
            return this.type;
        }

        public void setType(List<String> value) {
            this.type = value;
        }

        public String getReference() {
            return this.reference;
        }

        public void setReference(String value) {
            this.reference = value;
        }

        public String getPriceLevel() {
            return this.priceLevel;
        }

        public void setPriceLevel(String value) {
            this.priceLevel = value;
        }

        public String getScope() {
            return this.scope;
        }

        public void setScope(String value) {
            this.scope = value;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String value) {
            this.name = value;
        }

        public OpeningHours getOpeningHours() {
            return this.openingHours;
        }

        public void setOpeningHours(OpeningHours value) {
            this.openingHours = value;
        }

        public String getVicinity() {
            return this.vicinity;
        }

        public void setVicinity(String value) {
            this.vicinity = value;
        }

        public Geometry getGeometry() {
            return this.geometry;
        }

        public void setGeometry(Geometry value) {
            this.geometry = value;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String value) {
            this.id = value;
        }

        public String getPlaceId() {
            return this.placeId;
        }

        public void setPlaceId(String value) {
            this.placeId = value;
        }

    }

    public static class Southwest {

        @Element(name = "lng", required = false)
        String lng;

        @Element(name = "lat", required = false)
        String lat;

        public String getLng() {
            return this.lng;
        }

        public void setLng(String value) {
            this.lng = value;
        }

        public String getLat() {
            return this.lat;
        }

        public void setLat(String value) {
            this.lat = value;
        }

    }

    public static class Viewport {

        @Element(name = "southwest", required = false)
        Southwest southwest;

        @Element(name = "northeast", required = false)
        Northeast northeast;

        public Southwest getSouthwest() {
            return this.southwest;
        }

        public void setSouthwest(Southwest value) {
            this.southwest = value;
        }

        public Northeast getNortheast() {
            return this.northeast;
        }

        public void setNortheast(Northeast value) {
            this.northeast = value;
        }

    }

    public static class OpeningHours {

        @Element(name = "open_now", required = false)
        String openNow;

        public String getOpenNow() {
            return this.openNow;
        }

        public void setOpenNow(String value) {
            this.openNow = value;
        }

    }

    public static class Northeast {

        @Element(name = "lng", required = false)
        String lng;

        @Element(name = "lat", required = false)
        String lat;

        public String getLng() {
            return this.lng;
        }

        public void setLng(String value) {
            this.lng = value;
        }

        public String getLat() {
            return this.lat;
        }

        public void setLat(String value) {
            this.lat = value;
        }

    }

    public static class Photo {

        @Element(name = "photo_reference", required = false)
        String photoReference;

        @Element(name = "width", required = false)
        String width;

        @Element(name = "html_attribution", required = false)
        String htmlAttribution;

        @Element(name = "height", required = false)
        String height;

        public String getPhotoReference() {
            return this.photoReference;
        }

        public void setPhotoReference(String value) {
            this.photoReference = value;
        }

        public String getWidth() {
            return this.width;
        }

        public void setWidth(String value) {
            this.width = value;
        }

        public String getHtmlAttribution() {
            return this.htmlAttribution;
        }

        public void setHtmlAttribution(String value) {
            this.htmlAttribution = value;
        }

        public String getHeight() {
            return this.height;
        }

        public void setHeight(String value) {
            this.height = value;
        }

    }

    public static class Geometry {

        @Element(name = "viewport", required = false)
        Viewport viewport;

        @Element(name = "location", required = false)
        Location location;

        public Viewport getViewport() {
            return this.viewport;
        }

        public void setViewport(Viewport value) {
            this.viewport = value;
        }

        public Location getLocation() {
            return this.location;
        }

        public void setLocation(Location value) {
            this.location = value;
        }

        public LatLng getLatLng() {
            return new LatLng(Double.parseDouble(location.getLat()),
                    Double.parseDouble(location.getLng()));
        }

    }

    public static class Location {

        @Element(name = "lng", required = false)
        String lng;

        @Element(name = "lat", required = false)
        String lat;

        public String getLng() {
            return this.lng;
        }

        public void setLng(String value) {
            this.lng = value;
        }

        public String getLat() {
            return this.lat;
        }

        public void setLat(String value) {
            this.lat = value;
        }

    }

}