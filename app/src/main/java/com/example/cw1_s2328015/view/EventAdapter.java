package com.example.cw1_s2328015.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cw1_s2328015.R;
import com.example.cw1_s2328015.model.Event;
import com.example.cw1_s2328015.viewmodel.TrafficViewModel;

import java.util.ArrayList;


public class EventAdapter extends BaseAdapter {
    Context context;
    ArrayList<Event> eventList; //storage for the events to display
    TrafficViewModel viewModel;
    LayoutInflater inflater;

    public EventAdapter(Context context, ArrayList<Event> events, TrafficViewModel viewModel) {
        this.context = context;
        this.eventList = events;
        this.viewModel = viewModel;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int i) {
        return eventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        EventAdapter.WidgetsHolder holder = null;
        if(view == null) {
            view = inflater.inflate(R.layout.layout_listview, viewGroup, false);
            holder = new EventAdapter.WidgetsHolder();
            holder.category = (ImageView) view.findViewById(R.id.category);
            holder.road = (TextView) view.findViewById(R.id.road);
            holder.comment = (TextView) view.findViewById(R.id.comment);
            holder.details = view.findViewById(R.id.details);
            holder.rowMaintenance = view.findViewById(R.id.rowMaintenance);
            holder.maintenanceValue = (TextView) view.findViewById(R.id.maintenanceValue);
            holder.rowRoad = view.findViewById(R.id.rowRoad);
            holder.roadValue = (TextView) view.findViewById(R.id.roadValue);
            holder.rowImpact = view.findViewById(R.id.rowImpact);
            holder.impactValue = (TextView) view.findViewById(R.id.impactValue);
            holder.rowStart = view.findViewById(R.id.rowStart);
            holder.startValue = (TextView) view.findViewById(R.id.startValue);
            holder.rowEnd = view.findViewById(R.id.rowEnd);
            holder.endValue = (TextView) view.findViewById(R.id.endValue);
            holder.rowFrom = view.findViewById(R.id.rowFrom);
            holder.fromValue = (TextView) view.findViewById(R.id.fromValue);
            holder.rowTo = view.findViewById(R.id.rowTo);
            holder.toValue = (TextView) view.findViewById(R.id.toValue);
            holder.rowCoords = view.findViewById(R.id.rowCoords);
            holder.coordsValue = (TextView) view.findViewById(R.id.coordsValue);
            holder.rowUpdated = view.findViewById(R.id.rowUpdated);
            holder.updatedValue = (TextView) view.findViewById(R.id.updatedValue);
            view.setTag(holder); //store bound objects into the view
        }
        else{
            holder = (EventAdapter.WidgetsHolder)view.getTag();
        }
        Event event = eventList.get(i);
        // Icon for the category (unplanned and roadworks)
        if (event.getCategory() == Event.Category.UNPLANNED) {
            holder.category.setImageResource(R.drawable.incident);
        } else {
            holder.category.setImageResource(R.drawable.warning);
        }
        holder.road.setText(event.getRoadName());
        holder.comment.setText(event.getComment());

        // Details lines
        showRow(holder.rowMaintenance, holder.maintenanceValue, event.getMaintenanceTypeText());
        showRow(holder.rowRoad, holder.roadValue, event.getRoadAndDirectionText());
        showRow(holder.rowImpact, holder.impactValue, event.getImpactText());
        showRow(holder.rowStart, holder.startValue, event.getStartTime());
        showRow(holder.rowEnd, holder.endValue, event.getEndTime());
        showRow(holder.rowFrom, holder.fromValue, event.getFromName());
        showRow(holder.rowTo, holder.toValue, event.getToName());
        showRow(holder.rowCoords, holder.coordsValue, event.getCoordinatesText());
        showRow(holder.rowUpdated, holder.updatedValue, event.getVersionTime());


        if (viewModel.isExpanded(event.getId())) {
            holder.details.setVisibility(View.VISIBLE);
        } else {
            holder.details.setVisibility(View.GONE);
        }
        return view;
    }

    // shows a details line, or hides it if there's no text
    private void showRow(View row, TextView value, String text) {
        if (text == null || text.isEmpty()) {
            row.setVisibility(View.GONE);
        } else {
            value.setText(text);
            row.setVisibility(View.VISIBLE);
        }
    }

    static class WidgetsHolder {
        ImageView category;
        TextView road;
        TextView comment;
        View details;
        View rowMaintenance;
        TextView maintenanceValue;
        View rowRoad;
        TextView roadValue;
        View rowImpact;
        TextView impactValue;
        View rowStart;
        TextView startValue;
        View rowEnd;
        TextView endValue;
        View rowFrom;
        TextView fromValue;
        View rowTo;
        TextView toValue;
        View rowCoords;
        TextView coordsValue;
        View rowUpdated;
        TextView updatedValue;
    }
}
