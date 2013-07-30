package com.yoalert.cops;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.markupartist.android.widget.PullToRefreshListView;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: lei
 * Date: 7/29/13
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskListView extends ListActivity {
    private LinkedList<String> mListItems;

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

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mListItems);

        setListAdapter(adapter);
    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);    //To change body of overridden methods use File | Settings | File Templates.
        Object item = this.getListAdapter().getItem(position);
        Toast.makeText(this, "You have chosen the pen: "+ item.toString(), Toast.LENGTH_LONG).show();
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
        mListItems.addFirst("Added after refresh...");

        // Call onRefreshComplete when the list has been refreshed.
        ((PullToRefreshListView) getListView()).onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private String[] mStrings = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
            "Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis",
            "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler"};

}
