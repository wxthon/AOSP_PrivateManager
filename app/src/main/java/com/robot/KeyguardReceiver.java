package com.robot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by wxthon on 10/23/17.
 */

public class KeyguardReceiver extends BroadcastReceiver {

    private IRobotContext mRobotContext;

    KeyguardReceiver(IRobotContext context) {
        mRobotContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            IProcessCleaner cleaner = mRobotContext.getProcessCleaner();
            if (cleaner != null)
                cleaner.doSmartClean();
        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {

        }
    }
}
