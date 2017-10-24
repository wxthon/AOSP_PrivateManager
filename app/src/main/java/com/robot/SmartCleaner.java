package com.robot;

/**
 * Created by wxthon on 10/23/17.
 */

public class SmartCleaner implements IProcessCleaner, IRobotComponent {

    private IRobotContext mRobotContext;

    SmartCleaner(IRobotContext context) {
        mRobotContext = context;
        if (context instanceof RobotContextImpl) {
            ((RobotContextImpl)mRobotContext).registerProcessCleaner(this);
        }
    }

    public void start() {

    }


    @Override
    public void doSmartClean() {

    }

    @Override
    public String getName() {
        return "SmartCleaner";
    }

    @Override
    public int getComponentLayout() {
        return R.layout.smart_cleaner_component;
    }

    @Override
    public int getUILayout() {
        return R.layout.smart_cleaner_ui;
    }
}
