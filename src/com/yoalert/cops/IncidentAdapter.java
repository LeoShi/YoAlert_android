package com.yoalert.cops;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lei
 * Date: 7/30/13
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class IncidentAdapter extends ArrayAdapter<Incident> {
    private final Context context;
    private final List<Incident> data;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public IncidentAdapter(Context context, List<Incident> data) {
        super(context, R.layout.tasklist_row, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.tasklist_row, parent, false);
        TextView categoryView = (TextView)rowView.findViewById(R.id.category);
        TextView datetimeView = (TextView)rowView.findViewById(R.id.datetime);
        Incident currentIncident = data.get(position);

        System.out.print(currentIncident.getCategory());

        categoryView.setText(currentIncident.getCategory());
        datetimeView.setText(dateFormat.format(currentIncident.getDate()));
        if (currentIncident.status.equals(Incident.UNPROCESS)){
            rowView.setBackgroundColor(Color.RED);
        }
        return rowView;
    }
}
