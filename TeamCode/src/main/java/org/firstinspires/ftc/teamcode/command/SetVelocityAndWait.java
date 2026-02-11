package org.firstinspires.ftc.teamcode.command;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.LimelightPoseEstimator;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

/**
 * An example command that uses an example subsystem.
 */
public class SetVelocityAndWait extends CommandBase {
    private final RobotHardware robot;
    private LimelightPoseEstimator limelightPoseEstimator;
    private Shooter shooter;

    /**
     * @param limelightPoseEstimator limelight pose estimator
     * @param shooter                shooter
     */
    public SetVelocityAndWait(LimelightPoseEstimator limelightPoseEstimator, Shooter shooter, RobotHardware robot) {
        this.limelightPoseEstimator = limelightPoseEstimator;
        this.shooter = shooter;
        this.robot = robot;
    }

    @Override
    public void execute() {
        if (limelightPoseEstimator.isValidTarget()) {
            shooter.setVelocity(
                    shooter.goalDistanceToRPM(limelightPoseEstimator.distanceToGoal())
            );
        } else {
            shooter.setVelocity(2100);
        }
    }

    @Override
    public boolean isFinished() {
        return shooter.getTargetVelocity() - 50 < robot.shooterMotor.getVelocity() && robot.shooterMotor.getVelocity() < shooter.getTargetVelocity() + 50;
    }
}
