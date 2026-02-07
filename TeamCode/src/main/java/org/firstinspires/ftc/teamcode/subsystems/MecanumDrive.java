package org.firstinspires.ftc.teamcode.subsystems;

import android.view.animation.GridLayoutAnimationController;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.math.Vector;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.pedroCommand.TurnCommand;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

import java.util.Arrays;
import java.util.List;

public class MecanumDrive extends SubsystemBase {
    public static final Pose blueGoalPose = new Pose(0, 144);
    public static final Pose redGoalPose = blueGoalPose.mirror();
    private static final double TURRET_OFFSET = 2.0599;
    public static double PREDICT_FACTOR = 0.35;
    public static Pose lastPose = new Pose(0, 0, 0);
    private final DcMotor frontLeft;
    private final DcMotor frontRight;
    private final DcMotor backLeft;
    private final DcMotor backRight;
    public AimAtTarget lastAimTarget = new AimAtTarget(0, 0);

    public Follower follower;
    RobotHardware robot = RobotHardware.get();
    public Pose aimAtPose;
    private double turnDirection;

    public MecanumDrive(HardwareMap map, Pose startingPose) {
        this.follower = Constants.createFollower(map);

        //this is for teleop

        frontLeft = map.get(DcMotor.class, "motorFL");
        frontRight = map.get(DcMotor.class, "motorFR");
        backLeft = map.get(DcMotor.class, "motorBL");
        backRight = map.get(DcMotor.class, "motorBR");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reverse the left side motors
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        follower.setStartingPose(startingPose == null ? new Pose(0, 0, 0) : startingPose);
        follower.update();
    }

    @Override
    public void periodic() {
        lastPose = follower.getPose();

        lastAimTarget = getShooterPositionPinpointRel2();

    }

    public void resetHeading(double newHeading) {
        follower.setPose(follower.getPose().setHeading(newHeading));
    }

    public void setTeleOpDrive(double forward, double strafe, double rotation) {
        frontLeft.setPower(forward + strafe + rotation);
        frontRight.setPower(forward - strafe - rotation);
        backLeft.setPower(forward - strafe + rotation);
        backRight.setPower(forward + strafe - rotation);
    }

    /**
     * Get the current estimated robot pose
     */
    public Pose getPose() {
        return follower.getPose();
    }

    public double getRadialVelocityToGoal() {
        return 0;
    }

    //    public double getTangentVelocityToGoal() {
//        Vector robotPosition = getPose().getAsVector();
//        Vector goalPosition = (RobotHardware.alliance == RobotHardware.Alliance.BLUE ? blueGoalPose : redGoalPose).getAsVector();
//
//        // Calculate vector from robot to goal
//        Vector toGoal = goalPosition.minus(robotPosition);
//
//        // Calculate angle to goal (field-relative)
//        double angleToGoalField = Math.atan2(toGoal.getYComponent(), toGoal.getXComponent());
//
//        // Get robot velocity scalar
//        double robotSpeed = getVelocity().getMagnitude();
//
//        if (robotSpeed < 3) {
//            return 0;
//        }
//
//        double robotVelocityAngle = Math.atan2(getVelocity().getYComponent(), getVelocity().getXComponent());
//
//        // Calculate tangential component
//        double deltaTheta = AngleUnit.normalizeRadians(robotVelocityAngle - angleToGoalField);
//        return robotSpeed * Math.sin(deltaTheta);
//    }
//
    public Vector getVelocity() {
        return follower.getVelocity();
    }

    public double getAngularVelocity() {
        return follower.getAngularVelocity();
    }

    public Vector getAcceleration() {
        return follower.getAcceleration();
    }

    public AimAtTarget getAimTarget() {
        return lastAimTarget;
    }

    private AimAtTarget getShooterPositionPinpointRel2() {
        Pose chosenPose = RobotHardware.alliance == RobotHardware.Alliance.BLUE ? blueGoalPose : redGoalPose;
        Pose currPose = getPose();

        // aim logic to help prevent undershoot on the edge of the top tiles
        if (currPose.getY() > 72.0 && RobotHardware.alliance == RobotHardware.Alliance.BLUE) {
            aimAtPose = new Pose(7, 144 - 7);
        } else if (currPose.getY() > 72.0 && RobotHardware.alliance == RobotHardware.Alliance.RED) {
            aimAtPose = new Pose(144 - 7, 144 - 7);
        } else {
            aimAtPose = chosenPose;
        }


        double distance = chosenPose.distanceFrom(chosenPose) / 12.0;

        double absAngleToTarget = Math.atan2(
                aimAtPose.getY() - (currPose.getY()),
                aimAtPose.getX() - (currPose.getX())
        );

        double angleToTarget = Math.toDegrees(absAngleToTarget);
        follower.getHeading();
        return new AimAtTarget(distance, angleToTarget);
    }

    public double getHeadingError() {
        turnDirection = MathFunctions.getTurnDirection(follower.getPose().getHeading(), Math.toRadians(getAimTarget().heading));
        return turnDirection * MathFunctions.getSmallestAngleDifference(follower.getPose().getHeading(), Math.toRadians(getAimTarget().heading));
    }


    public static class AimAtTarget {
        public double distance;
        public double heading;

        public AimAtTarget(double distance, double heading) {
            this.distance = distance;
            this.heading = heading;
        }
    }
}
