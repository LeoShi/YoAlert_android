package com.yoalert.cops;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.markupartist.android.widget.PullToRefreshListView;

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
        startActivity(new Intent(LoginActivity.this, TaskListView.class));
    }
}
