package com.example.cw1_s2328015.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.cw1_s2328015.model.Event;
import com.example.cw1_s2328015.viewmodel.TrafficViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

// Events as markers on a Google Map
public class EventMapFragment extends SupportMapFragment implements OnMapReadyCallback {
    private GoogleMap gMap;
    private TrafficViewModel viewModel;

    public EventMapFragment() {
        //DO NOT DELETE
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // same ViewModel as the list
        viewModel = new ViewModelProvider(requireActivity()).get(TrafficViewModel.class);
        // onMapReady runs when the map has loaded
        getMapAsync(this);
    }

    // centres on Scotland, then shows the markers
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        // zoom 6 = whole country (lab 7 used newLatLng, no zoom)
        LatLng scotland = new LatLng(56.8, -4.2);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(scotland, 6));
        gMap.getUiSettings().setZoomControlsEnabled(true);

        // observed here so markers are only added once the map exists
        viewModel.getEvents().observe(getViewLifecycleOwner(), events -> showMarkers(events));
    }

    // clears old markers, adds one per event (from point, else to point, else skipped)
    private void showMarkers(ArrayList<Event> events) {
        gMap.clear(); //removes marks so no duplicates
        for (Event event : events) {
            LatLng position;
            if (event.hasFromPoint()) {
                position = new LatLng(event.getFromLatitude(), event.getFromLongitude());
            } else if (event.hasToPoint()) {
                position = new LatLng(event.getToLatitude(), event.getToLongitude());
            } else {
                continue;
            }
            gMap.addMarker(new MarkerOptions().position(position).title(event.getRoadName()));
        }
    }
}