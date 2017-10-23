package com.robot;

/**
 * Created by wxthon on 10/23/17.
 */

public class HomeSystem implements IRobotComponent {

    private IRobotContext mRobotContext;

    HomeSystem(IRobotContext context) {
        mRobotContext = context;
    }

    public void start() {


    }

    @Override
    public String getName() {
        return "HomeSystem";
    }

    @Override
    public int getLayoutID() {
        return 0;
    }
}
