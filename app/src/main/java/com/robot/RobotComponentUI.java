package com.robot;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by haitengwang on 24/10/2017.
 */

public class RobotComponentUI extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra("component_layout", 0);
        if (id == 0) {
            id = R.layout.component_ui;
        }
        setContentView(id);
    }
}
