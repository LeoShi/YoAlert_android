package com.yoalert.cops;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.markupartist.android.widget.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String userToken;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.user_information), Context.MODE_PRIVATE);
        userToken = sharedPref.getString(getString(R.string.current_user_token), "");
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

        IncidentAdapter incidentAdapter = new IncidentAdapter(this, mListItems);

        setListAdapter(incidentAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);    //To change body of overridden methods use File | Settings | File Templates.
        Incident item = (Incident)this.getListAdapter().getItem(position - 1);
        Intent taskDetailIntent = new Intent(TaskListView.this, TaskDetail.class);
        taskDetailIntent.putExtra("incident", item);
        startActivity(taskDetailIntent);

    }

    private class GetDataTask extends AsyncTask<Void, Void, LinkedList<Incident>> {

        @Override
        protected LinkedList<Incident> doInBackground(Void... params) {
            LinkedList<Incident> incidents = new LinkedList<Incident>();
            Response response = RequestWrapper.get(String.format(getString(R.string.get_incidents_url), userToken));
            try{
                JSONArray jsonArray = new JSONArray(response.getContent());
                for(int i = 0; i < jsonArray.length(); i++){
                    Incident incident = parseIncident(jsonArray.getJSONObject(i));
                    incidents.addLast(incident);
                }
            } catch (JSONException e) {
            Log.e("save user token error", e.toString());
            }

            return incidents;
        }

        private Incident parseIncident(JSONObject jsonObject) throws JSONException {
            int id = jsonObject.getInt("id");
            String category = jsonObject.getString("category");
            String updatedTime = jsonObject.getString("updated_at");
            String createdTime = jsonObject.getString("created_at");
            String status = jsonObject.getString("status");
            String location = jsonObject.getString("location");
            String reference = jsonObject.getString("reference");
            String mobile_user_contact = jsonObject.getString("mobile_user_contact");
            String mobile_user_name = jsonObject.getString("mobile_user_name");

            return new Incident(id,category,updatedTime,status, location, mobile_user_name,
                    mobile_user_contact, reference, createdTime);
        }

        @Override
        protected void onPostExecute(LinkedList<Incident> result) {
            mListItems.addAll(result);
            // Call onRefreshComplete when the list has been refreshed.
            ((PullToRefreshListView) getListView()).onRefreshComplete();
            super.onPostExecute(result);
        }
    }
}
