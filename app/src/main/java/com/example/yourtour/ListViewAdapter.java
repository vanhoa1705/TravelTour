package com.example.yourtour;


import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<TourDetail> implements Filterable {
    public Context _context;
    public int _resource;
    public ArrayList<TourDetail> _tours, tempArray;
    CustomFilter cs;

    public ListViewAdapter(Context context, int resource, ArrayList<TourDetail> tours) {
        super(context,resource,tours);
        this.tempArray = tours;
        this._context = context;
        this._resource = resource;
        this._tours = tours;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater layoutInflater =  LayoutInflater.from(this._context);
            convertView = layoutInflater.inflate(_resource, parent, false);
        }
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        TextView txtCost = convertView.findViewById(R.id.txtCost);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);

        TourDetail item = _tours.get(position);
        txtName.setText(item.getName());
        txtDate.setText("Date: " + item.getStartDate() + " - "+ item.getEndDate());
        txtCost.setText("Cost: " + item.getMinCost() + " - " + item.getMaxCost());
        //if(item.getRating() == 0) item.setRating(1);
        ratingBar.setRating(item.getRating());
        return convertView;
    }

    @Override
    public int getCount() {
        return _tours.size();
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
                ArrayList<TourDetail> filter = new ArrayList<>();

                for (int i = 0; i < tempArray.size(); i++) {
                    if (tempArray.get(i).getName().toUpperCase().contains(constraint)) {
                        TourDetail tourDetail = new TourDetail(tempArray.get(i).getId(), tempArray.get(i).getStatus(), tempArray.get(i).getName(),
                                tempArray.get(i).getMinCost(), tempArray.get(i).getMaxCost(), tempArray.get(i).getStartDate(), tempArray.get(i).getEndDate(),
                                tempArray.get(i).getAdults(), tempArray.get(i).getChilds(), tempArray.get(i).getPrivate(), tempArray.get(i).getAvatar(), tempArray.get(i).getRating());
                        filter.add(tourDetail);
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
            _tours = (ArrayList<TourDetail>)filterResults.values;
            notifyDataSetChanged();
        }
    }

}