package org.firstinspires.ftc.teamcode.opmodes.newAuto.paths;

import static org.firstinspires.ftc.teamcode.cmd.Commandlet.fork;
import static org.firstinspires.ftc.teamcode.cmd.Commandlet.waitFor;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelDeadlineGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import com.seattlesolvers.solverslib.pedroCommand.TurnCommand;
import com.seattlesolvers.solverslib.pedroCommand.TurnToCommand;
import com.seattlesolvers.solverslib.util.TelemetryData;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.Intake;


/**
 * This TeleOp class manages driver control of the robot using the SolversLib command-based framework.
 * It's responsible for initializing subsystems and mapping gamepad inputs to robot actions.
 */
@Autonomous
@Configurable
public class threeBallFar extends CommandOpMode {
    public static double turnInDegrees = 0;
    TelemetryData telemetryData = new TelemetryData(telemetry);//Poses
    private Follower follower;
    private RobotHardware robot;


    //set the actual three ball shoot
    public Command threeBallShoot() {
        return new SequentialCommandGroup(
                //set the intake
//                new InstantCommand(() -> robot.intake.setMode(Intake.Mode.INGEST)),
                waitFor(400),
                //set the shoot position
                new InstantCommand(robot.transfer::shoot_position), waitFor(2000),
                //intake on

                //turn off intake
//                new InstantCommand(() -> robot.intake.setMode(Intake.Mode.OFF)),
                //push to off position
                new InstantCommand(() -> robot.transfer.off_position())

        );


    }


    //take code from the three ball shoot teleop
    public Command threeBallShootTeleOp() {
        return new SequentialCommandGroup(
//                new InstantCommand(this::autoAimAdjust),
                new InstantCommand(() -> {
                    robot.shooterAdjust.setServos(0);
                }), new WaitCommand(300), new InstantCommand(() -> {
            robot.shooter.setVelocity(6000);
        }), new WaitCommand(3000), threeBallShoot(), new InstantCommand(() -> {
            robot.shooter.setVelocity(0);
        }));
    }

//    public void autoAimAdjust() {
//        double drivePowers[] = {Kp * robot.limelight.getLatestResult().getTx(), Kp * robot.limelight.getLatestResult().getTx(), -Kp * robot.limelight.getLatestResult().getTx(), -Kp * robot.limelight.getLatestResult().getTx()};
//        robot.drive.follower.drivetrain.runDrive(drivePowers);
//    }

    /**
     * This method is executed once when the OpMode is initialized.
     * It handles the setup of all necessary components for driver control,
     * such as subsystems and button bindings for commands.
     */
    @Override
    public void initialize() {
        super.reset();

        robot = RobotHardware.get();
        robot.init(hardwareMap, RobotHardware.Mode.TELEOP, telemetry, new Pose(72, 72, Math.toRadians(135)));
        RobotHardware.alliance = RobotHardware.Alliance.BLUE;

        // Initialize follower
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(56, 8, Math.toRadians(90)));

        robot.limelight.pipelineSwitch(0);

        InstantCommand m_initialize = new InstantCommand(robot.shooterAdjust::initializeServos);
        InstantCommand m_offCommand = new InstantCommand(robot.transfer::off_position);

        PathChain moveWhileTurn = follower.pathBuilder().addPath(new BezierLine(new Pose(56, 8), new Pose(56, 15)))
//                .setConstantHeadingInterpolation(Math.toDegrees(100))
                .build();
        PathChain turn = follower.pathBuilder().addPath(new BezierLine(new Pose(56, 15), new Pose(56, 15))).setConstantHeadingInterpolation(Math.toDegrees(100)).build();
        m_initialize.schedule();
        m_offCommand.schedule();


        SequentialCommandGroup autoSequence = new SequentialCommandGroup(
                //shoot the preload
                new InstantCommand(() -> follower.setMaxPower(0.85)), new FollowPathCommand(follower, moveWhileTurn, true), new ParallelDeadlineGroup(new WaitCommand(1000), new TurnToCommand(follower, Math.toRadians(120))), new WaitCommand(1000), threeBallShootTeleOp()

        );
        schedule(autoSequence);
    }

    public double setTurnPosition() {
        return turnInDegrees;
    }

    @Override
    public void run() {
        super.run();
        follower.update();

        telemetryData.addData("X", follower.getPose().getX());
        telemetryData.addData("Y", follower.getPose().getY());
        telemetryData.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetryData.update();
    }

}
