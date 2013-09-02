package com.yoalert.cops;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
    private SharedPreferences sharedPref;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences(getString(R.string.user_information), Context.MODE_PRIVATE);
        String userToken = sharedPref.getString(getString(R.string.current_user_token), "");
        if(userToken.length() > 0){
            startActivity(new Intent(LoginActivity.this, TaskListView.class));
        }else {
            setContentView(R.layout.main);
        }
    }

    public void btn_login_click(View view) throws JSONException {
        findViewById(R.id.error_msg).setVisibility(View.INVISIBLE);
        Response response = RequestWrapper.post(getString(R.string.login_url), buildLoginInfo());
        if(response.getHttp_status_code() == 201){
            saveUserToken(response);
            startActivity(new Intent(LoginActivity.this, TaskListView.class));
        }else {
            findViewById(R.id.error_msg).setVisibility(View.VISIBLE);
        }
    }

    private void saveUserToken(Response response) {
        try {
            JSONObject jObject = new JSONObject(response.getContent());
            String userToken = jObject.getString(getString(R.string.token));
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.current_user_token), userToken);
            editor.commit();

        } catch (JSONException e) {
            Log.e("save user token error", e.toString());
        }
    }

    private JSONObject buildLoginInfo() throws JSONException {
        JSONObject loginInfo = new JSONObject();
        loginInfo.put("email", getValueById(R.id.email));
        loginInfo.put("password", getValueById(R.id.password));
        return loginInfo;
    }

    private String getValueById(int id){
        return ((EditText)findViewById(id)).getText().toString();
    }
}
