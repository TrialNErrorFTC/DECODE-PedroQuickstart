package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

@Configurable
public class ShooterAdjust extends SubsystemBase {
    private final RobotHardware robot;
    public static double slope = -0.117;

    public ShooterAdjust() {
        robot = RobotHardware.get();
    }

    public void initializeServos() {
        setServos(1);
    }

    public void setServos(double angle) {
        robot.servoLeft.set(angle);
        robot.servoRight.set(angle);
    }


    public double goalDistanceToAngle(double distance) {
        if (12 < distance && distance < 60) {
            double hoodPosition = Range.clip(getSlope() * distance + 0.82, 0, 1);
            return hoodPosition;
        }
        return 0;
    }

    public double getSlope() {
        return slope;
    }
}
