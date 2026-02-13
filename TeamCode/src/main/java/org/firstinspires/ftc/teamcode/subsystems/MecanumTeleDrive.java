package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.math.Vector;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class MecanumTeleDrive extends SubsystemBase {
    private final DcMotor frontLeft;
    private final DcMotor frontRight;
    private final DcMotor backLeft;
    private final DcMotor backRight;
    private final IMU imu;

    public Follower follower;
    RobotHardware robot = RobotHardware.get();
    private Pose aimAtPose;
    private double turnDirection;

    public MecanumTeleDrive(HardwareMap map) {
        frontLeft = map.get(DcMotor.class, "frontLeftMotor");
        frontRight = map.get(DcMotor.class, "frontRightMotor");
        backLeft = map.get(DcMotor.class, "backLeftMotor");
        backRight = map.get(DcMotor.class, "backRightMotor");

        imu = map.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

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

    public void setTeleOpDriveField(double forward, double strafe, double rotation) {
        frontLeft.setPower(forward + strafe + rotation);
        frontRight.setPower(forward - strafe - rotation);
        backLeft.setPower(forward - strafe + rotation);
        backRight.setPower(forward + strafe - rotation);
        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Rotate the movement direction counter to the bot's rotation
        double rotX = strafe * Math.cos(-botHeading) - forward * Math.sin(-botHeading);
        double rotY = strafe * Math.sin(-botHeading) + forward * Math.cos(-botHeading);

        rotX = rotX * 1.1;  // Counteract imperfect strafing

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rotation), 1);
        double frontLeftPower = (rotY + rotX + rotation) / denominator;
        double backLeftPower = (rotY - rotX + rotation) / denominator;
        double frontRightPower = (rotY - rotX - rotation) / denominator;
        double backRightPower = (rotY + rotX - rotation) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
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
