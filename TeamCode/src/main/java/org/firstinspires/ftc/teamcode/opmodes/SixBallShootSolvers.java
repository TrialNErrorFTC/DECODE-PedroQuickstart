package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.LogCatCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.RepeatCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import com.seattlesolvers.solverslib.util.TelemetryData;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.commands.DefaultDrive;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;

import dev.nextftc.core.commands.utility.LambdaCommand;
import kotlin.time.Instant;

/**
 * This TeleOp class manages driver control of the robot using the SolversLib command-based framework.
 * It's responsible for initializing subsystems and mapping gamepad inputs to robot actions.
 */
@Autonomous
public class SixBallShootSolvers extends CommandOpMode {
    private Follower follower;
    TelemetryData telemetryData = new TelemetryData(telemetry);
    //Poses
    public static Pose startPose = new Pose(12.763678696158333, 130.6298020954598, Math.toRadians(135));
    public static Pose preloadPose = new Pose(23.310826542491252, 120.32130384167635, Math.toRadians(135));
    public static Pose pickup1StartPose = new Pose(40.618, 83.299, Math.toRadians(180));
    public static Pose pickup1EndPose = new Pose(17.973806752037255, 82.82479627473806, Math.toRadians(180));
    public static Pose shoot1Pose = new Pose(57.69033760186264, 84.52968568102445, Math.toRadians(135));

    PathChain scorePreload, preloadToPickup1, startPickup1, endPickup1, shootPickup;

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

    /**
     * This method is executed once when the OpMode is initialized.
     * It handles the setup of all necessary components for driver control,
     * such as subsystems and button bindings for commands.
     */
    @Override
    public void initialize() {
        super.reset();

        // Initialize follower
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();

        ShooterSubsystem m_shooter = new ShooterSubsystem(hardwareMap);
        IntakeSubsystem m_intake = new IntakeSubsystem(hardwareMap);
        TransferSubsystem m_transfer = new TransferSubsystem(hardwareMap);
        LimelightSubsystem m_limelight = new LimelightSubsystem(hardwareMap);

        InstantCommand m_initialize = new InstantCommand(m_shooter::initializeServos);
        InstantCommand m_offCommand = new InstantCommand(m_transfer::off_position);
        m_initialize.schedule();
        m_offCommand.schedule();
        SequentialCommandGroup m_threeBallShoot = new SequentialCommandGroup(
                new InstantCommand(() -> {
                    m_shooter.setServos(m_shooter.servoLeft.getPosition() - 0.2);
                }),
                //shoot first one
                new InstantCommand(m_transfer::shoot_position),
                new WaitCommand(400),
                //servo down
                new InstantCommand(m_transfer::off_position),
                new WaitCommand(500),
                //intake
                new InstantCommand(() -> {
                    m_intake.motor_intake.setPower(0.375);
                }),
                new WaitCommand(400),
                new InstantCommand(m_intake::stop),
                //shoot
                new InstantCommand(m_transfer::shoot_position),
                new WaitCommand(400),
                //servo down
                new InstantCommand(m_transfer::off_position),
                new WaitCommand(400),
                //outtake
                new InstantCommand(() -> {
                    m_intake.motor_intake.setPower(-0.3);
                }),
                new WaitCommand(200),
                new InstantCommand(m_intake::stop),
                new WaitCommand(100),
                //intake
                new InstantCommand(() -> {
                    m_intake.motor_intake.setPower(0.7);
                }),
                new WaitCommand(1000),
                //stop
                new InstantCommand(m_intake::stop),
                //shoot
                new InstantCommand(m_transfer::shoot_position),
                new WaitCommand(500),
                new InstantCommand(m_transfer::off_position)
        );


        SequentialCommandGroup autoSequence = new SequentialCommandGroup(
                new RunCommand(() -> {
                    m_shooter.highSpeed();
                }).withTimeout(7000),
                new WaitCommand(7000),
                new FollowPathCommand(follower, scorePreload, true, 0.7),
                m_threeBallShoot,
                new RunCommand(m_shooter::stop)
//                new FollowPathCommand(follower, startPickup1, true, 0.7),
//                new FollowPa/thCommand(follower, endPickup1, true, 0.7),
//                m_threeBallSh/oot
//                new LogCatCommand("Done With scorePreload"),
//                m_threeBallShoot
        );
        schedule(autoSequence);
    }


    @Override
    public void run() {
        super.run();
        follower.update();

        telemetryData.addData("X", follower.getPose().getX());
        telemetryData.addData("Y", follower.getPose().getY());
        telemetryData.addData("Heading", follower.getPose().getHeading());
        telemetryData.update();
    }

}
