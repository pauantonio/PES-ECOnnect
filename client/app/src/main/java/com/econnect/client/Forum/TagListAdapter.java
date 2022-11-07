package com.econnect.client.Forum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.econnect.API.ForumService.Tag;
import com.econnect.client.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagListAdapter extends BaseAdapter implements Filterable {

    private final Tag[] _tags;
    private List<Tag> _displayedData;
    private static LayoutInflater inflater = null;

    public TagListAdapter(Context owner, Tag[] tags) {
        _tags = tags;
        _displayedData = Arrays.asList(tags);
        inflater = (LayoutInflater) owner.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _displayedData.size();
    }

    @Override
    public String getItem(int i) {
        return _displayedData.get(i).tag;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Initialize view and product
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.tag_list_item, null);
        }
        final Tag t = _displayedData.get(position);

        // Set tag text
        TextView tagText = vi.findViewById(R.id.tagText);
        tagText.setText(t.tag);

        // Set number of posts with tag
        TextView numPosts = vi.findViewById(R.id.numPostsWithTag);
        String text;
        if (t.count != 1) text = vi.getResources().getString(R.string.tag_posts_count, t.count);
        else text = vi.getResources().getString(R.string.tag_posts_count_one, t.count);
        numPosts.setText(text);

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
                    results.values = Arrays.asList(_tags);
                    results.count = _tags.length;
                    return results;
                }

                // Ignore case and trim
                String queryLower = constraint.toString().toLowerCase().trim();
                // Get only the tags that contain (or begin with) the query
                ArrayList<Tag> containQuery = new ArrayList<>();
                ArrayList<Tag> beginWithQuery = new ArrayList<>();
                for (Tag t : _tags) {
                    // Add type to the corresponding list
                    String name = t.tag.toLowerCase();
                    if (name.startsWith(queryLower)) {
                        beginWithQuery.add(t);
                    }
                    else if (name.contains(queryLower)) {
                        containQuery.add(t);
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
                _displayedData = (List<Tag>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
