// IRobot.aidl
package com.robot;

// Declare any non-default types here with import statements

import java.util.List;
import com.robot.RobotComponent;

interface IRobot {

    boolean isReady();
    List<RobotComponent> getComponents();
    int getComponentUI(in RobotComponent rc);
}
