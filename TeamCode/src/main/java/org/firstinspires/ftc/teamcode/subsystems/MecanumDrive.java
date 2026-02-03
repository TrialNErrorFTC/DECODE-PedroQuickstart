package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class MecanumDrive extends SubsystemBase {
    private static final double TURRET_OFFSET = 2.0599;
    public static double PREDICT_FACTOR = 0.35;
    RobotHardware robot = RobotHardware.get();
    private Pose aimAtPose;

    public static class AimAtTarget {
        public double distance;
        public double heading;

        public AimAtTarget(double distance, double heading) {
            this.distance = distance;
            this.heading = heading;
        }
    }

    public AimAtTarget lastAimTarget = new AimAtTarget(0, 0);

    public Follower follower;
    public static Pose lastPose = new Pose(0, 0, 0);
    public static final Pose blueGoalPose = new Pose(0, 144);
    public static final Pose redGoalPose = blueGoalPose.mirror();

    public MecanumDrive(HardwareMap map, Pose startingPose) {
        this.follower = Constants.createFollower(map);
        follower.setStartingPose(startingPose == null ? new Pose(0, 0, 0) : startingPose);
        follower.update();
    }

    @Override
    public void periodic() {
        lastPose = follower.getPose();

        lastAimTarget = getShooterPositionPinpointRel2();
        robot.flightRecorder.addLine("======DRIVETRAIN:=======");
        robot.flightRecorder.addData("goal heading", lastAimTarget.heading);
        robot.flightRecorder.addData("goal distance", lastAimTarget.distance);
        robot.flightRecorder.addData("goal x", aimAtPose.getX());
        robot.flightRecorder.addData("goal y", aimAtPose.getY());

        robot.flightRecorder.addData("X:", lastPose.getX());
        robot.flightRecorder.addData("Y:", lastPose.getY());
        robot.flightRecorder.addData("Heading", Math.toDegrees(lastPose.getHeading()));

        robot.flightRecorder.addData("Heading Error", getHeadingError());
        follower.update();
    }

    public void resetHeading(double newHeading) {
        follower.setPose(follower.getPose().setHeading(newHeading));
    }

    public void setTeleOpDrive(double forward, double strafe, double rotation) {
        follower.setTeleOpDrive(forward, strafe, rotation, false);
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

    public double getTangentVelocityToGoal() {
        Vector robotPosition = getPose().getAsVector();
        Vector goalPosition = (RobotHardware.alliance == RobotHardware.Alliance.BLUE ? blueGoalPose : redGoalPose).getAsVector();

        // Calculate vector from robot to goal
        Vector toGoal = goalPosition.minus(robotPosition);

        // Calculate angle to goal (field-relative)
        double angleToGoalField = Math.atan2(toGoal.getYComponent(), toGoal.getXComponent());

        // Get robot velocity scalar
        double robotSpeed = getVelocity().getMagnitude();

        if (robotSpeed < 3) {
            return 0;
        }

        double robotVelocityAngle = Math.atan2(getVelocity().getYComponent(), getVelocity().getXComponent());

        // Calculate tangential component
        double deltaTheta = AngleUnit.normalizeRadians(robotVelocityAngle - angleToGoalField);
        return robotSpeed * Math.sin(deltaTheta);
    }

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
        return new AimAtTarget(distance, angleToTarget);
    }
    private double getHeadingError(){
        if (follower.getCurrentPath() == null){
            return 0;
        }

        double headingError = MathFunctions.getTurnDirection(follower.getPose().getHeading(),Math.toRadians(lastAimTarget.heading))
                * MathFunctions.getSmallestAngleDifference(follower.getPose().getHeading(), Math.toRadians(lastAimTarget.heading));
        return headingError;
    }
}
