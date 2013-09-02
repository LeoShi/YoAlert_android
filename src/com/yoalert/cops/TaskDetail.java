package com.yoalert.cops;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
                .setPositiveButton("Done",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(TaskDetail.this, TaskListView.class));
                    }
                })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       // do nothing
                   }
               }).show();
    }

    private void setValueById(int id, String value){
        ((TextView)findViewById(id)).setText(value);
    }
}