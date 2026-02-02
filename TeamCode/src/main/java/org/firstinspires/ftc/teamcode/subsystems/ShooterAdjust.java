package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class ShooterAdjust extends SubsystemBase {
    private final RobotHardware robot;

    public ShooterAdjust() {
        robot = RobotHardware.get();
    }

    public void initializeServos() {
        setServos(1);
    }

    private void setServos(int angle) {
        robot.servoLeft.set(angle);
        robot.servoRight.set(angle);
    }
}
