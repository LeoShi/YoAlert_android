package com.yoalert.cops;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: lei
 * Date: 8/1/13
 * Time: 12:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskDetail extends Activity {
    private Incident incident;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);
        buildView();
    }

    private String getUserToken() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.user_information), Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.current_user_token), "");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TaskDetail.this, TaskListView.class));
    }

    private void buildView() {
        Bundle extras = getIntent().getExtras();
        incident = (Incident) extras.getSerializable("incident");
        setValueById(R.id.createDate, incident.getCreateTime());
        setValueById(R.id.category, incident.getCategory());
        setValueById(R.id.location, incident.getLocation());
        setValueById(R.id.victim, incident.getVictim());
        setValueById(R.id.contact, incident.getContact());
        setValueById(R.id.refNumber, incident.getReference());
        setValueById(R.id.status, incident.getStatus());
        setValueById(R.id.updatedTime, incident.getUpdateTime());
    }

    public void btn_back_click(View view){
        startActivity(new Intent(TaskDetail.this, TaskListView.class));
    }

    public void btn_process_click(View view){
       new AlertDialog.Builder(this)
                .setTitle("Confirm Submit")
                .setMessage("Are you sure you have done this task?")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String url = String.format(getString(R.string.put_incident_url), incident.getId(), getUserToken());
                        Response response = RequestWrapper.put(url, buildProcessedInfo());
                        if (response.getHttp_status_code() == 201) {
                            startActivity(new Intent(TaskDetail.this, TaskListView.class));
                        }
                    }
                })
               .setNegativeButton("No", null).show();
    }

    private JSONObject buildProcessedInfo(){
        JSONObject loginInfo = new JSONObject();
        try {
            loginInfo.put("incident", new JSONObject().put("status", Incident.PROCESSED));
        } catch (JSONException ignored) {}
        return loginInfo;
    }

    private void setValueById(int id, String value){
        ((TextView)findViewById(id)).setText(value);
    }
}