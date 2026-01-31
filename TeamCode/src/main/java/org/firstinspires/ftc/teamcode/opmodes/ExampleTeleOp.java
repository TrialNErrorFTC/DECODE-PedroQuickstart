package org.firstinspires.ftc.teamcode.opmodes;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.Supplier;

@Configurable
@TeleOp
public class ExampleTeleOp extends OpMode {
    private Follower follower;
    public static Pose startingPose; //See ExampleAuto to understand how to use this
    private boolean automatedDrive;
    private Supplier<PathChain> pathChain;
    private TelemetryManager telemetryM;
    private boolean slowMode = false;
    private double slowModeMultiplier = 0.5;
    boolean headingLock;
    double targetHeading;
    PIDFController controller;
    private Pose targetPose;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(72, 72, Math.toRadians(90)));
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        targetPose = new Pose(144,144);
        controller = new PIDFController(follower.constants.coefficientsHeadingPIDF);
        headingLock = false;
        controller.setCoefficients(follower.constants.coefficientsHeadingPIDF);
    }

    @Override
    public void start() {
        //The parameter controls whether the Follower should use break mode on the motors (using it is recommended).
        //In order to use float mode, add .useBrakeModeInTeleOp(true); to your Drivetrain Constants in Constant.java (for Mecanum)
        //If you don't pass anything in, it uses the default (false)
        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        follower.update();

        telemetryM.debug("position", follower.getPose());
        telemetryM.debug("velocity", follower.getVelocity());
        telemetryM.debug("headingLock", headingLock);
        telemetryM.update();

        //Call this once per loop
        Pose robotPose = follower.getPose();
        targetHeading = Math.atan2(targetPose.getY() - robotPose.getY(), targetPose.getX() - robotPose.getX());

        controller.updateError(getHeadingError(targetHeading));

        if (headingLock) {
            //Make the last parameter false for field-centric
            //In case the drivers want to use a "slowMode" you can scale the vectors
            //This is the normal version to use in the TeleOp
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    controller.run()
            );
        } else {
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    true // Robot Centric
            );
        }

        if(gamepad1.aWasPressed()){
            headingLock = !headingLock;
        }

    }
    public double getHeadingError(double targetHeading) {
        double headingError = MathFunctions.getTurnDirection(follower.getPose().getHeading(), targetHeading) * MathFunctions.getSmallestAngleDifference(follower.getPose().getHeading(), targetHeading);
        return headingError;
    }
}