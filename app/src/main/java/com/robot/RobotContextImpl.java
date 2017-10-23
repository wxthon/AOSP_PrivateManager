package com.robot;

import android.content.Context;
import android.util.Log;

/**
 * Created by wxthon on 10/23/17.
 */

public class RobotContextImpl implements IRobotContext {

    private static final String TAG = RobotContextImpl.class.getSimpleName();

    private Context mContext;
    private IProcessCleaner mProcessCleaner = null;

    RobotContextImpl(Context context) {
        mContext = context;
    }


    @Override
    public IProcessCleaner getProcessCleaner() {
        return mProcessCleaner;
    }

    public void registerProcessCleaner(IProcessCleaner cleaner) {
        if (mProcessCleaner != null) {
            Log.e(TAG, "robot context already has a process cleaner");
            return;
        }
        mProcessCleaner = cleaner;
    }
}
