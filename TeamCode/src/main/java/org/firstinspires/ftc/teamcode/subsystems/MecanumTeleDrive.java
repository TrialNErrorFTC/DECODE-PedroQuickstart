package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class MecanumTeleDrive extends SubsystemBase {
    private final DcMotor frontLeft;
    private final DcMotor frontRight;
    private final DcMotor backLeft;
    private final DcMotor backRight;

    public Follower follower;
    RobotHardware robot = RobotHardware.get();
    private Pose aimAtPose;
    private double turnDirection;

    public MecanumTeleDrive(HardwareMap map) {
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
    }


    public void setTeleOpDrive(double forward, double strafe, double rotation) {
        frontLeft.setPower(forward + strafe + rotation);
        frontRight.setPower(forward - strafe - rotation);
        backLeft.setPower(forward - strafe + rotation);
        backRight.setPower(forward + strafe - rotation);
    }

    public void leftDriveMotors(double power) {
        frontLeft.setPower(power);
        backLeft.setPower(power);

    }

    public void rightDriveMotors(double power) {
        frontRight.setPower(power);
        backRight.setPower(power);
    }
    /**
     * Get the current estimated robot pose
     */

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
}
