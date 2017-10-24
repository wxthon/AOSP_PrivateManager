package com.robot;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wxthon on 10/23/17.
 */

public class RobotService extends Service {

    private IRobotContext mRobotContext;
    private boolean mInited = false;
    private KeyguardReceiver mKeyguardReceiver;
    private BatteryStatsMonitor mBatteryStatsMonitor;
    private HomeSystem mHomeSystem;
    private SmartCleaner mSmartCleaner;

    private List<IRobotComponent> mComponents = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Robot();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInited = false;

        mRobotContext = new RobotContextImpl(this);

        // register all receivers firstly to kill all background processes immediately
        registerAllReceivers();

        //  the battery use stats
        startBatteryStatsMonitor();

        // start home stats, eg: light、temp...、...
        startHomeSystem();

        // start smart cleaner service
        startSmartCleaner();

        mInited = true;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void registerAllReceivers() {
        mKeyguardReceiver = new KeyguardReceiver(mRobotContext);
        registerReceiver(mKeyguardReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        registerReceiver(mKeyguardReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
    }

    private void startBatteryStatsMonitor() {
        mBatteryStatsMonitor = new BatteryStatsMonitor(mRobotContext);
        mBatteryStatsMonitor.start();
    }

    private void startHomeSystem() {
        mHomeSystem = new HomeSystem(mRobotContext);
        mHomeSystem.start();
        mComponents.add(mHomeSystem);
    }

    private void startSmartCleaner() {
        mSmartCleaner = new SmartCleaner(mRobotContext);
        mSmartCleaner.start();
        mComponents.add(mSmartCleaner);
    }

    private class Robot extends IRobot.Stub {

        @Override
        public boolean isReady() throws RemoteException {
            return mInited;
        }

        @Override
        public List<RobotComponent> getComponents() throws RemoteException {
            List<RobotComponent> components = new ArrayList<>();
            for (IRobotComponent c : mComponents) {
                components.add(new RobotComponent(c.getName(), c.getComponentLayout()));
            }
            return components;
        }

        @Override
        public int getComponentUI(RobotComponent rc) throws RemoteException {
            for (IRobotComponent c : mComponents) {
                if (c.getName().equals(rc.getName())) {
                    return c.getUILayout();
                }
            }
            return 0;
        }
    }

}
