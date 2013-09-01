package com.yoalert.cops;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void btn_login_click(View view){
        findViewById(R.id.error_msg).setVisibility(View.INVISIBLE);
        Response response = RequestWrapper.post(getString(R.string.login_url), buildLoginInfo());
        if(response.getHttp_status_code() == 201){
            startActivity(new Intent(LoginActivity.this, TaskListView.class));
        }else {
            findViewById(R.id.error_msg).setVisibility(View.VISIBLE);
        }
    }

    private List<NameValuePair> buildLoginInfo() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("[user][email]", getValueById(R.id.email)));
        nameValuePairs.add(new BasicNameValuePair("[user][password]", getValueById(R.id.password)));
        return nameValuePairs;
    }

    private String getValueById(int id){
        return ((EditText)findViewById(id)).getText().toString();
    }
}
