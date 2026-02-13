package org.firstinspires.ftc.teamcode.opmodes.newAuto;

import static org.firstinspires.ftc.teamcode.cmd.Commandlet.fork;
import static org.firstinspires.ftc.teamcode.cmd.Commandlet.waitFor;

import com.pedropathing.follower.Follower;
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
import com.seattlesolvers.solverslib.util.TelemetryData;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.Intake;


/**
 * This TeleOp class manages driver control of the robot using the SolversLib command-based framework.
 * It's responsible for initializing subsystems and mapping gamepad inputs to robot actions.
 */
@Autonomous
public class twelveBallAutoBlue extends CommandOpMode {
    public static Pose startPose = new Pose(122.87258687258688, 123.54831199068684, Math.toRadians(40));
    public static Pose shootPose = new Pose(107.11969111969113, 106.05405405405405, Math.toRadians(40));
    public static Pose pickup1StartPose = new Pose(106.05405405405405, 90.23748544819556, Math.toRadians(0));
    public static Pose pickup1EndPose = new Pose(122.87258687258687, 90.23748544819556, Math.toRadians(0));
    public static Pose targetPose = new Pose(7, 144 - 7);
    TelemetryData telemetryData = new TelemetryData(telemetry);//Poses
    PathChain scorePreload, preloadToPickup1, startPickup1, endPickup1, shootPickup;
    ;
    private Follower follower;
    private RobotHardware robot;
    private PathsBlue paths;


    public void buildPaths() {

        paths = new PathsBlue(follower);

        Paths.isPathRed = false;

        paths.goal21Build();
    }


    //set the shoot position and transfer
    private SequentialCommandGroup getM_threeBallShoot2() {
        return new SequentialCommandGroup(
                new InstantCommand(robot.transfer::shoot_position),
                waitFor(400),
                new InstantCommand(() -> {
                    robot.intake.setPower(0.375);
                    robot.intake.setMode(Intake.Mode.CUSTOM);
                }),

                waitFor(400),
                new InstantCommand(() -> robot.intake.setMode(Intake.Mode.INGEST)),
                waitFor(3500),
                fork(new InstantCommand(() -> robot.transfer.stop()),
                        new InstantCommand(() -> robot.intake.setMode(Intake.Mode.OFF))),
                new InstantCommand(() -> robot.transfer.off_position())

        );
    }

    private SequentialCommandGroup adjustSpeedAndAngle(Pose shootPose) {
        double distance = shootPose.distanceFrom(targetPose);
        return new SequentialCommandGroup(
                new InstantCommand(() -> {
                    robot.shooterAdjust.goalDistanceToAngle(distance);
                }),
                new WaitCommand(300),
                new InstantCommand(
                        () -> {
                            robot.shooter.goalDistanceToRPM(distance);
                        }
                ),
                new WaitCommand(3000)
        );
    }

    //set the actual three ball shoot
    public Command threeBallShoot() {
        return new SequentialCommandGroup(
                //set the intake
                new InstantCommand(() -> robot.intake.setMode(Intake.Mode.INGEST)),
                waitFor(400),
                //set the shoot position
                new InstantCommand(robot.transfer::shoot_position),
                waitFor(2000),
                //turn off intake
                new InstantCommand(() -> robot.intake.setMode(Intake.Mode.OFF)),
                //push to off position
                new InstantCommand(() -> robot.transfer.off_position())

        );


    }


    //take code from the three ball shoot teleop
    public Command threeBallShootTeleOp() {
        return new SequentialCommandGroup(
//                new InstantCommand(this::autoAimAdjust),
                new InstantCommand(() -> {
                    if (robot.limelightPoseEstimator.isValidTarget()) {
                        robot.shooterAdjust.setServos(
                                robot.shooterAdjust.goalDistanceToAngle(robot.limelightPoseEstimator.distanceToGoal())
                        );
                    }
                }),
                new WaitCommand(300),
                new InstantCommand(
                        () -> {
                            if (robot.limelightPoseEstimator.isValidTarget()) {
                                robot.shooter.setVelocity(
                                        robot.shooter.goalDistanceToRPM(robot.limelightPoseEstimator.distanceToGoal())
                                );
                            }
                        }
                ),
                new WaitCommand(500),
                threeBallShoot(),
                new InstantCommand(() -> {
                    robot.shooter.setVelocity(0);
                })
        );
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
        RobotHardware.alliance = RobotHardware.Alliance.RED;

        // Initialize follower
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(PathsBlue.P_START);
        buildPaths();


        InstantCommand m_initialize = new InstantCommand(robot.shooterAdjust::initializeServos);
        InstantCommand m_offCommand = new InstantCommand(robot.transfer::off_position);
        m_initialize.schedule();
        m_offCommand.schedule();


        SequentialCommandGroup autoSequence = new SequentialCommandGroup(
                //shoot the preload
                new InstantCommand(() -> follower.setMaxPower(0.85)),
                new FollowPathCommand(follower, paths.shootPreload, true, 0.7),
                new WaitCommand(1000),
                threeBallShootTeleOp(),

                new InstantCommand(() -> follower.setMaxPower(0.8)),


                //intake 2nd row
                new ParallelDeadlineGroup(
                        new FollowPathCommand(follower, paths.intake6, true, 0.7),

                        new InstantCommand(() -> {
                            robot.intake.setMode(Intake.Mode.INGEST);
                        })
                ),
                new InstantCommand(() -> {
                    robot.intake.setMode(Intake.Mode.OFF);
                }),


                //shoot the 2nd row
                new WaitCommand(1000), //maybe remove later
                new InstantCommand(() -> follower.setMaxPower(0.85)),
                new FollowPathCommand(follower, paths.shoot6, true),
                threeBallShootTeleOp(),

                new InstantCommand(() -> follower.setMaxPower(0.7)),


                //intake 1st row
                new ParallelDeadlineGroup(
                        new FollowPathCommand(follower, paths.intake3, true, 0.7),

                        new InstantCommand(() -> {
                            robot.intake.setMode(Intake.Mode.INGEST);
                        })
                ),
                new InstantCommand(() -> {
                    robot.intake.setMode(Intake.Mode.OFF);
                }),

                //shoot the 1st row
                new WaitCommand(1000), //maybe remove later
                new InstantCommand(() -> follower.setMaxPower(0.85)),
                new FollowPathCommand(follower, paths.shoot3, true),
                threeBallShootTeleOp()

//                //unload ramp
//                new WaitCommand(1000),
//                new InstantCommand(() -> follower.setMaxPower(0.85)),
//                new FollowPathCommand(follower, paths.unloadRamp, true),
//
//
//                //intake 1st row
//                new InstantCommand(() -> follower.setMaxPower(0.7)),
//
//                new ParallelDeadlineGroup(
//                        new FollowPathCommand(follower, paths.intake9, true, 0.7),
//                        new InstantCommand(() -> {
//                            robot.intake.setMode(Intake.Mode.INGEST);
//                        })
//
//                ),
//                new InstantCommand(() -> {
//                    robot.intake.setMode(Intake.Mode.OFF);
//                }),
//
//                //shoot the 3rd row
//                new WaitCommand(1000), //maybe remove later
//                new InstantCommand(() -> follower.setMaxPower(0.7)),
//                new ParallelDeadlineGroup(
//                        new FollowPathCommand(follower, paths.shoot9, true),
//                        adjustSpeedAndAngle(Paths.P_SHOOT)
//
//                ),
//                threeBallShootTeleOp()

                //intake human player
//                new InstantCommand(() -> follower.setMaxPower(0.85)),
//
//                new ParallelDeadlineGroup(
//                        new FollowPathCommand(follower, paths.intakeHuman, true, 0.7),
//                        new InstantCommand(() -> {
//                            robot.intake.setMode(Intake.Mode.INGEST);
//                        })
//                ),
//                new InstantCommand(() -> {
//                    robot.intake.setMode(Intake.Mode.OFF);
//                }),
//
//                //shoot the 1st row
//                new WaitCommand(1000), //maybe remove later
//                new InstantCommand(() -> follower.setMaxPower(0.85)),
//                new ParallelDeadlineGroup(
//                        new FollowPathCommand(follower, paths.shootHuman, true),
//                        adjustSpeedAndAngle(Paths.P_SHOOT)
//
//                ),
//                threeBallShootTeleOp()
        );
        schedule(autoSequence);
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
