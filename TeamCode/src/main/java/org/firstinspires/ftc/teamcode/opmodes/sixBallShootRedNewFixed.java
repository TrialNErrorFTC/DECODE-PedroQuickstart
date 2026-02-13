//package org.firstinspires.ftc.teamcode.opmodes;
//
//import static org.firstinspires.ftc.teamcode.cmd.Commandlet.fork;
//import static org.firstinspires.ftc.teamcode.cmd.Commandlet.waitFor;
//import static org.firstinspires.ftc.teamcode.opmodes.DriveTeleOpBlue.Kp;
//
//import androidx.annotation.NonNull;
//
//import com.pedropathing.follower.Follower;
//import com.pedropathing.geometry.BezierLine;
//import com.pedropathing.geometry.Pose;
//import com.pedropathing.paths.PathChain;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.seattlesolvers.solverslib.command.Command;
//import com.seattlesolvers.solverslib.command.CommandOpMode;
//import com.seattlesolvers.solverslib.command.InstantCommand;
//import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
//import com.seattlesolvers.solverslib.command.RunCommand;
//import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
//import com.seattlesolvers.solverslib.command.WaitCommand;
//import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
//import com.seattlesolvers.solverslib.util.TelemetryData;
//
//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
//import org.firstinspires.ftc.teamcode.robot.RobotHardware;
//import org.firstinspires.ftc.teamcode.subsystems.Intake;
//
//import java.util.Arrays;
//
//
/// **
// * This TeleOp class manages driver control of the robot using the SolversLib command-based framework.
// * It's responsible for initializing subsystems and mapping gamepad inputs to robot actions.
// */
//@Autonomous
//public class sixBallShootRedNewFixed extends CommandOpMode {
//    private Follower follower;
//    TelemetryData telemetryData = new TelemetryData(telemetry);//Poses
//    public static Pose startPose = new Pose(122.87258687258688, 123.54831199068684, Math.toRadians(40));
//    public static Pose shootPose = new Pose(107.11969111969113, 106.05405405405405, Math.toRadians(40));
//    public static Pose pickup1StartPose = new Pose(106.05405405405405, 90.23748544819556, Math.toRadians(0));
//    public static Pose pickup1EndPose = new Pose(122.87258687258687, 90.23748544819556, Math.toRadians(0));
//
//    PathChain scorePreload, preloadToPickup1, startPickup1, endPickup1, shootPickup;
//    private RobotHardware robot;
//
//
//    public void buildPaths() {
//        scorePreload = follower.pathBuilder()
//                .addPath(new BezierLine(startPose, shootPose))
//                .setConstantHeadingInterpolation(Math.toRadians(40))
//                .build();
//        preloadToPickup1 = follower.pathBuilder()
//                .addPath(new BezierLine(shootPose, pickup1StartPose))
//                .setConstantHeadingInterpolation(Math.toRadians(35))
//                .build();
//        startPickup1 = follower.pathBuilder()
//                .addPath(new BezierLine(pickup1StartPose, pickup1EndPose))
//                .setConstantHeadingInterpolation(pickup1StartPose.getHeading())
//                .build();
//        endPickup1 = follower.pathBuilder()
//                .addPath(new BezierLine(pickup1EndPose, shootPose))
//                .setLinearHeadingInterpolation(pickup1EndPose.getHeading(), shootPose.getHeading())
//                .build();
//
//    }
//
//
//    @NonNull
//    private SequentialCommandGroup getM_threeBallShoot2() {
//        return new SequentialCommandGroup(
//                new InstantCommand(robot.transfer::shoot_position),
//                waitFor(400),
//                new InstantCommand(() -> {
//                    robot.intake.setPower(0.375);
//                    robot.intake.setMode(Intake.Mode.CUSTOM);
//                }),
//
//                waitFor(400),
//                fork(new InstantCommand(robot.transfer::transfer),
//                        new InstantCommand(() -> robot.intake.setMode(Intake.Mode.INGEST))),
//                waitFor(3500),
//                fork(new InstantCommand(() -> robot.transfer.stop()),
//                        new InstantCommand(() -> robot.intake.setMode(Intake.Mode.OFF))),
//                new InstantCommand(() -> robot.transfer.off_position())
//
//        );
//    }
//
//    public Command threeBallShoot() {
//        return new SequentialCommandGroup(
//                new InstantCommand(robot.transfer::shoot_position),
//                waitFor(400),
//                new InstantCommand(() -> {
//                    robot.transfer.transfer();
//                }),
//
//                waitFor(400),
//                fork(new InstantCommand(robot.transfer::transfer),
//                        new InstantCommand(() -> robot.intake.setMode(Intake.Mode.INGEST))),
//                waitFor(3000),
//                fork(new InstantCommand(() -> robot.transfer.stop()),
//                        new InstantCommand(() -> robot.intake.setMode(Intake.Mode.OFF))),
//                new InstantCommand(() -> robot.transfer.off_position())
//
//        );
//
//    }
//
//    public Command threeBallShootTeleOp() {
//        return new SequentialCommandGroup(
////                new InstantCommand(this::autoAimAdjust),
//                new InstantCommand(() -> {
//                    if (robot.limelightPoseEstimator.isValidTarget()) {
//                        robot.shooterAdjust.setServos(
//                                robot.shooterAdjust.goalDistanceToAngle(robot.limelightPoseEstimator.distanceToGoal())
//                        );
//                    }
//                }),
//                new WaitCommand(300),
//                new InstantCommand(
//                        () -> {
//                            if (robot.limelightPoseEstimator.isValidTarget()) {
//                                robot.shooter.setVelocity(
//                                        robot.shooter.goalDistanceToRPM(robot.limelightPoseEstimator.distanceToGoal())
//                                );
//                            } else {
//                                robot.shooter.setVelocity(2100);
//                            }
//                        }
//                ),
//                new WaitCommand(3000),
//                threeBallShoot(),
//                new InstantCommand(() -> {
//                    robot.shooter.setVelocity(1500);
//                })
//        );
//    }
//
//    public void autoAimAdjust() {
//                double drivePowers[] = {Kp * robot.limelight.getLatestResult().getTx(), Kp * robot.limelight.getLatestResult().getTx(), -Kp * robot.limelight.getLatestResult().getTx(), -Kp * robot.limelight.getLatestResult().getTx()};
//                robot.drive.follower.drivetrain.runDrive(drivePowers);
//    }
//    /**
//     * This method is executed once when the OpMode is initialized.
//     * It handles the setup of all necessary components for driver control,
//     * such as subsystems and button bindings for commands.
//     */
//    @Override
//    public void initialize() {
//        super.reset();
//
//        robot = RobotHardware.get();
//        robot.init(hardwareMap, RobotHardware.Mode.TELEOP, telemetry, new Pose(72, 72, Math.toRadians(135)));
//        RobotHardware.alliance = RobotHardware.Alliance.RED;
//
//        // Initialize follower
//        follower = Constants.createFollower(hardwareMap);
//        follower.setStartingPose(startPose);
//        buildPaths();
//
//
//        InstantCommand m_initialize = new InstantCommand(robot.shooterAdjust::initializeServos);
//        InstantCommand m_offCommand = new InstantCommand(robot.transfer::off_position);
//        m_initialize.schedule();
//        m_offCommand.schedule();
//
//        SequentialCommandGroup autoTest = new SequentialCommandGroup(
//                new RunCommand(
//                        () -> {autoAimAdjust();}
//                ).withTimeout(5000)
//        );
//        SequentialCommandGroup autoSequence = new SequentialCommandGroup(
//                new FollowPathCommand(follower, scorePreload, true, 0.7),
//                new WaitCommand(1000),
//                threeBallShootTeleOp(),
//        new FollowPathCommand(follower, preloadToPickup1, true, 0.7),
//                new WaitCommand(1000),
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> {
//                            robot.intake.setMode(Intake.Mode.INGEST);
//                        }),
//                        new InstantCommand((robot.transfer::transfer)
//                        )),
//                new InstantCommand(() -> follower.setMaxPower(0.5)),
//                new FollowPathCommand(follower, startPickup1, true),
//                new ParallelCommandGroup(
//                        new InstantCommand(() -> {
//                            robot.intake.setMode(Intake.Mode.OFF);
//                        }),
//                        new InstantCommand((robot.transfer::stop)
//                        )),
//                new FollowPathCommand(follower, endPickup1, true),
//                threeBallShootTeleOp()
//        );
//        schedule(autoSequence);
//    }
//
//
//    @Override
//    public void run() {
//        super.run();
//        follower.update();
//
//        telemetryData.addData("X", follower.getPose().getX());
//        telemetryData.addData("Y", follower.getPose().getY());
//        telemetryData.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
//        telemetryData.update();
//    }
//
//}
