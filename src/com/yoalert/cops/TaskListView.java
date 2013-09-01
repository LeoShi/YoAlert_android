package com.yoalert.cops;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.markupartist.android.widget.PullToRefreshListView;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: lei
 * Date: 7/29/13
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskListView extends ListActivity {
    private LinkedList<Incident> mListItems;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist);
        // Set a listener to be invoked when the list should be refreshed.
        ((PullToRefreshListView) getListView()).setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });

        mListItems = new LinkedList<Incident>();
        mListItems.addAll(Arrays.asList(mStrings));

        IncidentAdapter incidentAdapter = new IncidentAdapter(this, mListItems);

        setListAdapter(incidentAdapter);
    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);    //To change body of overridden methods use File | Settings | File Templates.
        Incident item = (Incident)this.getListAdapter().getItem(position - 1);
//        Toast.makeText(this, "You have chosen the incident: "+ item.getCategory(), Toast.LENGTH_LONG).show();
        Intent taskDetailIntent = new Intent(TaskListView.this, TaskDetail.class);
        taskDetailIntent.putExtra("incident", item);
        startActivity(taskDetailIntent);

    }

    private class GetDataTask extends AsyncTask<Void, Void, Incident[]> {

        @Override
        protected Incident[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(Incident[] result) {
            mListItems.addFirst(new Incident(2, "Hijack", new Date(), Incident.UNPROCESS));

            // Call onRefreshComplete when the list has been refreshed.
            ((PullToRefreshListView) getListView()).onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private Incident[] mStrings = {
            new Incident(1, "Shooting", new Date(), Incident.PROCESSED)};

}
