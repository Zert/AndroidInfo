package com.meow.ops.androidinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created on 6/10/16.
 */
public class ParamAdapter extends ArrayAdapter<Item> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Item> items = null;
    private LayoutInflater vi;

    public ParamAdapter(Context context, int layoutResourceId, ArrayList<Item> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
        this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Item i = items.get(position);
        if (i != null) {
            if (i.isSection()) {
                SectionItem si = (SectionItem) i;
                v = vi.inflate(R.layout.listview_item_section, parent, false);
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);

                final TextView sectionTextView = (TextView) v.findViewById(R.id.list_item_section_text);
                sectionTextView.setText(si.getTitle());
            } else {

                ParamItem pi = (ParamItem) i;
                v = vi.inflate(R.layout.listview_item_row, parent, false);
                final TextView key = (TextView) v.findViewById(R.id.key);
                final  TextView value = (TextView) v.findViewById(R.id.value);

                key.setText(pi.key);
                value.setText(pi.value);
            }
        }
        return v;
    }

}
