package com.econnect.client.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.econnect.API.HomeService.Home;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

public class HomeListAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    private final Home[] _homes;

    public HomeListAdapter(Fragment owner, Home[] homes) {
        inflater = (LayoutInflater) owner.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this._homes = homes;
    }

    @Override
    public int getCount() {
        return _homes.length;
    }

    @Override
    public Object getItem(int position) {
        return _homes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Initialize view and product
        Home h = _homes[position];
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.home_list_item, null);
        }

        TextView staircase = vi.findViewById(R.id.staircase_num);
        staircase.setText(h.escala == null ? Translate.id(R.string.none_staircase) : h.escala);

        TextView floor = vi.findViewById(R.id.floor_num);
        floor.setText(h.pis == null ? Translate.id(R.string.none_floor) : h.pis);

        TextView door = vi.findViewById(R.id.door_num);
        door.setText(h.porta == null ? Translate.id(R.string.none_door) : h.porta);

        return vi;
    }
}
