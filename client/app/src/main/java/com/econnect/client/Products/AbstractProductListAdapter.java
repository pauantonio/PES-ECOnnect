package com.econnect.client.Products;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.econnect.API.IAbstractProduct;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.client.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public abstract class AbstractProductListAdapter extends BaseAdapter implements Filterable {
    private final Fragment owner;
    private final int highlightColor;
    private final Drawable defaultImage;

    private static LayoutInflater inflater = null;
    private List<IAbstractProduct> data;

    private String lastQueryLower = null; // Store last query (in lowercase)

    public AbstractProductListAdapter(Fragment owner, int highlightColor, Drawable defaultImage) {
        this.owner = owner;
        this.highlightColor = highlightColor;
        this.defaultImage = defaultImage;
        data = new ArrayList<>();
        inflater = (LayoutInflater) owner.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    protected void setInitialValues(IAbstractProduct[] products) {
        data = Arrays.asList(products);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
            vi = inflater.inflate(R.layout.product_list_item, null);
        }
        final IAbstractProduct p = data.get(position);

        // Set item name
        TextView name = vi.findViewById(R.id.product_item_name);
        Spannable spannable = new SpannableString(p.getName());
        if (lastQueryLower != null) {
            // Highlight search substring (ignore case)
            String nameLower = p.getName().toLowerCase();
            int indexStart = nameLower.indexOf(lastQueryLower);
            int indexEnd = indexStart + lastQueryLower.length();
            if (indexStart != -1) {
                spannable.setSpan(new ForegroundColorSpan(highlightColor),
                        indexStart, indexEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        name.setText(spannable, TextView.BufferType.SPANNABLE);

        // Set item manufacturer
        TextView manufacturer = vi.findViewById(R.id.product_item_manufacturer);
        manufacturer.setText(p.getSecondaryText());

        // Set item rating
        TextView rating = vi.findViewById(R.id.product_item_rating);
        if(p.getAvgRating() == 0.0f) rating.setText("-");
        else rating.setText(String.format(Locale.getDefault(), "%.02f", p.getAvgRating()));

        // Set item image
        ImageView image = vi.findViewById(R.id.product_item_image);
        ExecutionThread.nonUI(()->{
            Bitmap bmp = p.getImage(64);
            ExecutionThread.UI(owner, ()-> {
                if (bmp == null) image.setImageDrawable(defaultImage);
                else image.setImageBitmap(bmp);
            });
        });

        return vi;
    }

    // Implement search

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Store query for highlighting the item names
                if (constraint.length() == 0) constraint = null;

                // If filter is empty, return all selectable products
                if (constraint == null) {
                    ArrayList<IAbstractProduct> res = new ArrayList<>();
                    for (IAbstractProduct p : getAllProducts()) {
                        if (isSelectable(p)) res.add(p);
                    }
                    results.values = res;
                    results.count = res.size();
                    lastQueryLower = null;
                    return results;
                }

                // Ignore case and trim
                String queryLower = constraint.toString().toLowerCase().trim();
                // Get only the products that contain (or begin with) the query
                ArrayList<IAbstractProduct> containQuery = new ArrayList<>();
                ArrayList<IAbstractProduct> beginWithQuery = new ArrayList<>();
                for (IAbstractProduct p : getAllProducts()) {
                    // Skip all non-selectable products
                    if (!isSelectable(p)) continue;
                    // Add type to the corresponding list
                    String name = p.getName().toLowerCase();
                    if (name.startsWith(queryLower)) {
                        beginWithQuery.add(p);
                    }
                    else if (name.contains(queryLower)) {
                        containQuery.add(p);
                    }
                }

                // Combine the two lists (beginWithQuery is preferred)
                beginWithQuery.addAll(containQuery);
                results.values = beginWithQuery;
                results.count = beginWithQuery.size();
                lastQueryLower = queryLower;

                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data = (List<IAbstractProduct>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    protected abstract IAbstractProduct[] getAllProducts();
    protected abstract boolean isSelectable(IAbstractProduct p);
}
