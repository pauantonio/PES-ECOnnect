package com.econnect.client.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.econnect.API.ForumService;
import com.econnect.API.HomeService;
import com.econnect.client.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StreetListAdapter extends BaseAdapter implements Filterable {


    private static LayoutInflater inflater = null;
    private final HomeService.Street[] _streets;
    private List<HomeService.Street> _displayedData;

    public StreetListAdapter(Fragment owner, HomeService.Street[] streets) {
        inflater = (LayoutInflater) owner.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this._streets = streets;
        _displayedData = Arrays.asList(streets);
    }

    @Override
    public int getCount() {
        return _displayedData.size();
    }

    @Override
    public Object getItem(int position) {
        return _displayedData.get(position).name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Initialize view and product
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.street_list_item, null);
        }

        TextView nameStreet = vi.findViewById(R.id.street_name);
        nameStreet.setText(_displayedData.get(position).name);

        return vi;
    }

    // Implement search
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                // If filter is empty, return all tags
                if (constraint == null || constraint.length() == 0) {
                    results.values = Arrays.asList(_streets);
                    results.count = _streets.length;
                    return results;
                }

                // Ignore case and trim
                String queryLower = constraint.toString().toLowerCase().trim();
                // Get only the tags that contain (or begin with) the query
                ArrayList<HomeService.Street> containQuery = new ArrayList<>();
                ArrayList<HomeService.Street> beginWithQuery = new ArrayList<>();
                for (HomeService.Street street : _streets) {
                    // Add type to the corresponding list
                    String name = street.name.toLowerCase();
                    if (name.startsWith(queryLower)) {
                        beginWithQuery.add(street);
                    }
                    else if (name.contains(queryLower)) {
                        containQuery.add(street);
                    }
                }

                // Combine the two lists (beginWithQuery is preferred)
                beginWithQuery.addAll(containQuery);
                results.values = beginWithQuery;
                results.count = beginWithQuery.size();

                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                _displayedData = (List<HomeService.Street>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public HomeService.Street getStreet(String streetName) {
        for (HomeService.Street s : _streets) {
            if (Objects.equals(s.name, streetName)) return s;
        }
        return null;
    }
}
