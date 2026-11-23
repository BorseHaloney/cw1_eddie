package com.example.cw1_s2328015.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cw1_s2328015.model.Event;
import com.example.cw1_s2328015.model.TrafficRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Holds the traffic data for the screens
public class TrafficViewModel extends ViewModel {
    public static final int UPDATE_INTERVAL_MINUTES = 60; // timer on auto refresh

    private final MutableLiveData<ArrayList<Event>> events;
    private ArrayList<Event> allEvents = new ArrayList<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private TrafficRepository repository;
    // ids of opened rows
    private final ArrayList<String> expandedIds = new ArrayList<>();
    private int currentScreen = 0;
    private final MutableLiveData<String> message;
    // results of the current update
    private boolean downloadFailed;
    private String savedTime;

    public TrafficViewModel() {
        events = new MutableLiveData<>();
        events.setValue(new ArrayList<>()); // empty list until data is loaded
        message = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Event>> getEvents() { return events; }
    public LiveData<String> getMessage() { return message; }
    public void clearMessage() { message.setValue(null); }
    public int getCurrentScreen() { return currentScreen; }
    public void setCurrentScreen(int currentScreen) { this.currentScreen = currentScreen; }

    // search feature
    public void searchRoad(String text) {
        ArrayList<Event> results = new ArrayList<>();
        for (Event event : allEvents) {
            if (event.getRoadName() != null
                    && event.getRoadName().toLowerCase().contains(text.toLowerCase())) {
                results.add(event);
            }
        }
        events.setValue(results);
    }

    // shows the full list again, no download
    public void clearSearch() {
        events.setValue(allEvents);
    }

    // opens or closes an event's details
    public void toggleExpanded(String eventId) {
        if (expandedIds.contains(eventId)) {
            expandedIds.remove(eventId);
        } else {
            expandedIds.add(eventId);
        }
    }

    // true if the event's details are open
    public boolean isExpanded(String eventId) { return expandedIds.contains(eventId); }

    // sets up the repository and starts auto-update (first call only, so rotation doesn't add a timer)
    public void start(String clientId, String clientKey, File filesDir) {
        if (repository == null) {
            repository = new TrafficRepository(clientId, clientKey, filesDir);
            // load now, change intervan
            executor.scheduleWithFixedDelay(this::downloadBothFeeds, 0, UPDATE_INTERVAL_MINUTES, TimeUnit.MINUTES);
        }
    }

    // manual refresh
    public void loadEvents() {
        executor.execute(this::downloadBothFeeds);
    }

    private void downloadBothFeeds() {
        try {
            downloadFailed = false;
            savedTime = null;
            ArrayList<Event> downloaded = new ArrayList<>();
            addFeed(downloaded, TrafficRepository.UNPLANNED_URL, Event.Category.UNPLANNED, TrafficRepository.UNPLANNED_FILE);
            addFeed(downloaded, TrafficRepository.ROADWORKS_URL, Event.Category.ROADWORKS, TrafficRepository.ROADWORKS_FILE);
            allEvents = downloaded;
            events.postValue(downloaded);

            if (savedTime != null) {
                message.postValue("Failed to fetch new data – showing saved data from " + savedTime);
            } else if (downloadFailed) {
                message.postValue("Failed to fetch new data – check your internet connection and press Refresh");
            }
        } catch (Exception e) {
            Log.e("TrafficViewModel", "Download/parse failed", e);
        }
    }

    // adds one feed's events and notes if it failed
    private void addFeed(ArrayList<Event> list, String url, Event.Category category, String fileName) {
        list.addAll(repository.fetchEvents(url, category, fileName));
        if (repository.downloadFailed()) {
            downloadFailed = true;
        }
        if (repository.usedSavedData()) {
            savedTime = repository.getSavedTimeText(fileName);
        }
    }
}
