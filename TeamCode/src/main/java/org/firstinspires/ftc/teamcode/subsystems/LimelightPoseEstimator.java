package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

import java.util.List;

@Configurable
public class LimelightPoseEstimator extends SubsystemBase {
    private static final double LIMELIGHT_OFFSET = 6;
    private RobotHardware robot;
    public LLResult lastResult;
    public static double Kp;
    public static double min_command;

    public double getLimelightMountAngleDegrees() {
        return limelightMountAngleDegrees;
    }

    // how many degrees back is your limelight rotated from perfectly vertical?
    public double limelightMountAngleDegrees = 25.3;

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
        if (RobotHardware.alliance == RobotHardware.Alliance.RED) {
            robot.limelight.pipelineSwitch(0);
        } else {
            robot.limelight.pipelineSwitch(1);
        }
    }

    public boolean isValidTarget() {
        return robot.limelight.getLatestResult().isValid() && robot.limelight.getLatestResult() != null;
    }

    public double getTx() {
        return robot.limelight.getLatestResult().getTx();

    }


    public double Kp() {
        return Kp;
    }

    public double getTy() {
//        LLResult result = robot.limelight.getLatestResult();

        //check for result is null
        return robot.limelight.getLatestResult().getTy();
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
        double distanceToFrontOfRobotToGoalInches = distanceFromLimelightToGoalInches - LIMELIGHT_OFFSET;
        return distanceToFrontOfRobotToGoalInches;
    }

}
