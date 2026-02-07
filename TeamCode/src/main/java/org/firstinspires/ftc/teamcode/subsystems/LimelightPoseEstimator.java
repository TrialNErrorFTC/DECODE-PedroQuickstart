package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

import java.util.List;

@Configurable
public class LimelightPoseEstimator extends SubsystemBase {
    private RobotHardware robot;
    public LLResult lastResult;
    public static double Kp;
    public static double min_command;

    public double getLimelightMountAngleDegrees() {
        return limelightMountAngleDegrees;
    }

    // how many degrees back is your limelight rotated from perfectly vertical?
    public double limelightMountAngleDegrees = 36.8;

    // distance from the center of the Limelight lens to the floor
    public double limelightLensHeightInches = 10.0;

    // distance from the target to the floor
    double goalHeightInches = 30.0;
    public LLResult result;

    //
    public LimelightPoseEstimator() {
        robot = RobotHardware.get();
        robot.limelight.setPollRateHz(100);
        robot.limelight.start();
        if (robot.alliance == RobotHardware.Alliance.BLUE) {
            robot.limelight.pipelineSwitch(1);
        } else {
            robot.limelight.pipelineSwitch(0);
        }
    }

    public void periodic() {
        result = robot.limelight.getLatestResult();
    }

    public double getTx() {
        LLResult result = robot.limelight.getLatestResult();

        //check for result is null
        if (result != null && result.isValid()) {
            return result.getTx();
        }
        return -99999;
    }


    public double drivePower() {
        double heading_error = -getTx();
        double steering_adjust = 0;
        if (Math.abs(heading_error) > 1.0) {
            if (heading_error < 0) {
                steering_adjust = Kp() * heading_error + min_command;
            } else {
                steering_adjust = Kp() * heading_error - min_command;
            }
        }
        return steering_adjust;
    }

    public double Kp() {
        return Kp;
    }

    public double getTy() {
        LLResult result = robot.limelight.getLatestResult();

        //check for result is null
        if (result != null && result.isValid()) {
            return result.getTy();
        }
        return -99999;
    }

    /**
     * Calculate the distance of the goal in inches
     *
     * @return
     */
    public double distanceToGoal() {

        double angleToGoalDegrees = limelightMountAngleDegrees + this.getTy();
        double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

        //calculate distance
        double distanceFromLimelightToGoalInches = (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);
        return distanceFromLimelightToGoalInches;
    }

}
