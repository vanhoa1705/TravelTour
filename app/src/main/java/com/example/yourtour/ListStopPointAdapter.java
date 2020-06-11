package com.example.yourtour;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListStopPointAdapter extends ArrayAdapter<StopPoint> implements Filterable {
    public Context _context;
    public int _resource;
    public ArrayList<StopPoint> _stopPoints, tempArray;
    CustomFilter cs;


    public ListStopPointAdapter(Context context, int resource, ArrayList<StopPoint> stopPoints) {
        super(context, resource, stopPoints);
        this.tempArray = stopPoints;
        this._context = context;
        this._resource = resource;
        this._stopPoints = stopPoints;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater layoutInflater =  LayoutInflater.from(this._context);
            convertView = layoutInflater.inflate(_resource, parent, false);
        }
        TextView txtName = convertView.findViewById(R.id.sp_Name);
        TextView txtAddress = convertView.findViewById(R.id.sp_address);
        TextView txtDate = convertView.findViewById(R.id.sp_date);
        TextView txtCost = convertView.findViewById(R.id.sp_cost);

        StopPoint item = _stopPoints.get(position);
        txtName.setText(item.name);
        txtAddress.setText("Address: " + item.address);
        txtDate.setText("Date: " + item.arriveAt +"-" + item.leaveAt);
        txtCost.setText("Cost: " + item.minCost + "-" + item.maxCost);

        return convertView;
    }

    @Override
    public int getCount() {
        return _stopPoints.size();
    }

    @Override
    public Filter getFilter() {
        if (cs == null){
            cs = new CustomFilter();
        }
        return cs;
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<StopPoint> filter = new ArrayList<>();

                for (int i = 0; i < tempArray.size(); i++) {
                    if (tempArray.get(i).getName().toUpperCase().contains(constraint)) {
                        StopPoint stopPoint = new StopPoint(tempArray.get(i).getId(),
                                tempArray.get(i).getServiceId(),tempArray.get(i).getName(),
                                tempArray.get(i).getAddress(), tempArray.get(i).getLat(),
                                tempArray.get(i).getLong(),tempArray.get(i).getArriveAt(),
                                tempArray.get(i).getLeaveAt(), tempArray.get(i).getMinCost(),
                                tempArray.get(i).getMaxCost(), tempArray.get(i).getServiceTypeId());
                        filter.add(stopPoint);
                    }
                }
                results.count = filter.size();
                results.values = filter;
            }
            else {
                results.count = tempArray.size();
                results.values = tempArray;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            _stopPoints = (ArrayList<StopPoint>)filterResults.values;
            notifyDataSetChanged();
        }
    }


}
