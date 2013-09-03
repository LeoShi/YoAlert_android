package com.yoalert.cops;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.markupartist.android.widget.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private String userToken;
    private String latestUpdateTime;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SharedPreferences sharedPref;
    private int backButtonCount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUserToken();
        initLatestUpdateTime();
        setContentView(R.layout.tasklist);
        initListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_logout){
            String url = String.format(getString(R.string.log_out_url), userToken);
            Response response = RequestWrapper.delete(url);
            if(response.getHttp_status_code() == 201){
                sharedPref.edit().clear().commit();
                startActivity(new Intent(TaskListView.this, LoginActivity.class));
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    private void initLatestUpdateTime() {
        latestUpdateTime = sharedPref.getString(getString(R.string.latest_update_time), "2013-01-01 00:00:00");
    }

    private void initListView() {
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

    private void initUserToken() {
        sharedPref = getSharedPreferences(getString(R.string.user_information), Context.MODE_PRIVATE);
        userToken = sharedPref.getString(getString(R.string.current_user_token), "");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);    //To change body of overridden methods use File | Settings | File Templates.
        Incident item = (Incident)this.getListAdapter().getItem(position - 1);
        Intent taskDetailIntent = new Intent(TaskListView.this, TaskDetail.class);
        taskDetailIntent.putExtra("incident", item);
        startActivity(taskDetailIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
//        saveLatestUpdateTime();
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        ((PullToRefreshListView) getListView()).onRefresh();
    }

    private void saveLatestUpdateTime() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.latest_update_time), latestUpdateTime);
        editor.commit();
    }

    private class GetDataTask extends AsyncTask<Void, Void, LinkedList<Incident>> {

        @Override
        protected LinkedList<Incident> doInBackground(Void... params) {
            LinkedList<Incident> incidents = new LinkedList<Incident>();
            String incidentUrl = String.format(getString(R.string.get_incidents_url), userToken,
                    latestUpdateTime.replace(" ", "%20"));
            Response response = RequestWrapper.get(incidentUrl);
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
            updateLatestUpdateTime(updatedTime);

            return new Incident(id,category,updatedTime,status, location, mobile_user_name,
                    mobile_user_contact, reference, createdTime);
        }

        private void updateLatestUpdateTime(String updatedTime) {
            try {
                Date updatedDate = dateFormat.parse(updatedTime);
                Date latestUpdateDate = dateFormat.parse(latestUpdateTime);
                if(latestUpdateDate.before(updatedDate)){
                    latestUpdateTime = dateFormat.format(updatedDate);
                }

            } catch (ParseException ignored) {}
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
