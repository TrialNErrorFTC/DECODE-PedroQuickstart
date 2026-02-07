package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.cmd.Commandlet.go;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

/**
 * This TeleOp class manages driver control of the robot using the SolversLib command-based framework.
 * It's responsible for initializing subsystems and mapping gamepad inputs to robot actions.
 */
@Autonomous
public class sixBallShoot extends OpMode {
    //Poses
    public static Pose startPose = new Pose(12.763678696158333, 130.6298020954598, Math.toRadians(135));
    public static Pose preloadPose = new Pose(23.310826542491252, 120.32130384167635, Math.toRadians(135));
    public static Pose pickup1StartPose = new Pose(40.618, 83.299, Math.toRadians(180));
    public static Pose pickup1EndPose = new Pose(17.973806752037255, 82.82479627473806, Math.toRadians(180));
    public static Pose shoot1Pose = new Pose(57.69033760186264, 84.52968568102445, Math.toRadians(135));

    PathChain scorePreload, preloadToPickup1, startPickup1, endPickup1, shootPickup;
    private RobotHardware robot;
    private Follower follower;


    public void buildPaths() {

        scorePreload = follower.pathBuilder()
                .addPath(new BezierLine(startPose, preloadPose))
                .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(135))
                .build();
        preloadToPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(preloadPose, pickup1StartPose))
                .setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180))
                .build();
        startPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(pickup1StartPose, pickup1EndPose))
                .setLinearHeadingInterpolation(pickup1StartPose.getHeading(), pickup1EndPose.getHeading())
                .build();
        endPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(pickup1EndPose, shoot1Pose))
                .setLinearHeadingInterpolation(pickup1EndPose.getHeading(), shoot1Pose.getHeading())
                .build();

    }

    @Override
    public void init() {
        robot = RobotHardware.get().init(hardwareMap, RobotHardware.Mode.TELEOP, telemetry, startPose);
        robot.alliance = RobotHardware.Alliance.RED;

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        buildPaths();

        Command goToPreload = new FollowPathCommand(follower, scorePreload, true, 0.7);
        CommandScheduler.getInstance().schedule(
                goToPreload
        );
    }

    @Override
    public void loop() {
        robot.endLoop();
    }

    /**
     * This method is executed once when the OpMode is initialized.
     * It handles the setup of all necessary components for driver control,
     * such as subsystems and button bindings for commands.
     */
}