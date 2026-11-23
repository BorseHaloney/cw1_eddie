package com.example.cw1_s2328015.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cw1_s2328015.R;
import com.example.cw1_s2328015.model.Event;
import com.example.cw1_s2328015.viewmodel.TrafficViewModel;

// Shows the event list
public class EventListFragment extends Fragment {
    private ListView eventListView;
    private EditText searchEditText;
    private Button searchButton;
    private Button clearButton;
    private TrafficViewModel viewModel;

    public EventListFragment() {
        //DONT REMOVE
        // break if this isnt here for some reason
    }

    // inflates the layout and binds the widgets (lecture6)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_list, container, false);
        eventListView = v.findViewById(R.id.simpleListView);
        searchEditText = v.findViewById(R.id.searchEditText);
        searchButton = v.findViewById(R.id.searchButton);
        clearButton = v.findViewById(R.id.clearButton);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TrafficViewModel.class);

        //check something was typed then filter list
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchEditText.getText().toString().trim(); 
                if (text.length() == 0) {
                    Toast.makeText(requireContext(), "Please enter a road name, e.g. M8",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                viewModel.searchRoad(text);
                if (viewModel.getEvents().getValue().isEmpty()) {
                    Toast.makeText(requireContext(), "No incidents or roadworks found for " + text,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // shows full list again
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");
                viewModel.clearSearch();
            }
        });

        // tap shows the row details
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View row, int position, long id) {
                Event event = (Event) parent.getItemAtPosition(position);
                viewModel.toggleExpanded(event.getId());
                View details = row.findViewById(R.id.details);
                if (viewModel.isExpanded(event.getId())) {
                    details.setVisibility(View.VISIBLE);
                } else {
                    details.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getEvents().observe(getViewLifecycleOwner(), events ->
                eventListView.setAdapter(new EventAdapter(requireContext(), events, viewModel)));
    }
}
