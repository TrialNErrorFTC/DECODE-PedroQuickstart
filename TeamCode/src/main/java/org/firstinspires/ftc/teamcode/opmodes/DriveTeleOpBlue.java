package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.cmd.Commandlet.fork;
import static org.firstinspires.ftc.teamcode.cmd.Commandlet.run;
import static org.firstinspires.ftc.teamcode.cmd.Commandlet.waitFor;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.PIDFController;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ScheduleCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;


@Configurable
@TeleOp(name = "DriveTrain TeleOp (BLUE)")
public class DriveTeleOpBlue extends OpMode {
    public static double TARGET_VELOCITY = 0.0;
    public static double Kp = 0.02;
    private GamepadEx gamepad1Ex;
    private RobotHardware robot;
    private boolean automatedDrive;

    private PIDFController controller;

    private boolean headingLock = false;

    @Override
    public void init() {
        CommandScheduler.getInstance().reset();
        robot = RobotHardware.get();
        robot.init(hardwareMap, RobotHardware.Mode.TELEOP, telemetry, new Pose(72, 72, Math.toRadians(90)));
        RobotHardware.alliance = RobotHardware.Alliance.BLUE;
        robot.limelight.pipelineSwitch(0);

        gamepad1Ex = new GamepadEx(gamepad1);

//        controller = new PIDFController(robot.drive.follower.constants.coefficientsHeadingPIDF);
        InstantCommand m_initialize = new InstantCommand(robot.shooterAdjust::initializeServos);
        InstantCommand m_offCommand = new InstantCommand(robot.transfer::off_position);
        robot.shooter.setMode(Shooter.Mode.RAW);
        robot.shooter.setPower(0);
//        TurnCommand m_turnCommand = new TurnCommand(
//                robot.drive.follower,
//                robot.drive.getHeadingError(),
//                robot.drive.
//        )
        SequentialCommandGroup m_threeBallShoot2 = new SequentialCommandGroup(
                new InstantCommand(robot.transfer::shoot_position),
                waitFor(400),
                new InstantCommand(() -> {
                    robot.intake.setPower(0.375);
                    robot.intake.setMode(Intake.Mode.CUSTOM);
                }),

                waitFor(400),
                fork(new InstantCommand(robot.transfer::transfer),
                        new InstantCommand(() -> robot.intake.setMode(Intake.Mode.INGEST))),
                waitFor(3000),
                fork(new InstantCommand(() -> robot.transfer.stop()),
                        new InstantCommand(() -> robot.intake.setMode(Intake.Mode.OFF))),
                new InstantCommand(() -> robot.transfer.off_position())

        );
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        run(() -> robot.shooterAdjust.initializeServos()),
                        run(() -> robot.transfer.off_position()))
        );

//        bind(GamepadKeys.Button.A,
//                new TurnCommand(robot.drive.follower, Math.toRadians(robot.drive.lastAimTarget.heading),
//                        MathFunctions.getTurnDirection(robot.drive.lastPose.getHeading(),
//                                Math.toRadians(robot.drive.lastAimTarget.heading)) == 1),
//                new InstantCommand());
//        bind(GamepadKeys.Button.B, new InstantCommand(() -> {
//            headingLock = !headingLock;
//        }), new InstantCommand());


        Command intakeTransferOn = new ScheduleCommand(new InstantCommand(() -> {
            robot.intake.setMode(Intake.Mode.INGEST);
        }), new InstantCommand((robot.transfer::transfer)));

        Command intakeTransferOff = new ScheduleCommand(new InstantCommand(() -> {
            robot.intake.setMode(Intake.Mode.OFF);
        }), new InstantCommand((robot.transfer::stop)));

        Command intakeTransferReverse = new InstantCommand(() -> {
            robot.intake.setMode(Intake.Mode.DISCARD);
        });

        bind(GamepadKeys.Button.RIGHT_BUMPER, intakeTransferOn, intakeTransferOff);

        bind(GamepadKeys.Button.LEFT_BUMPER, new InstantCommand(robot.transfer::shoot_position), new InstantCommand(robot.transfer::off_position));

        bind(GamepadKeys.Button.DPAD_UP, new InstantCommand(() -> {
            robot.shooter.setVelocity(1500);
        }), new InstantCommand());

        bind(GamepadKeys.Button.DPAD_DOWN, new InstantCommand(() -> {
            robot.shooter.setVelocity(0);
        }), new InstantCommand());

        bindToggle(GamepadKeys.Button.X,
                new SequentialCommandGroup(
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
                                    } else {
                                        robot.shooter.setVelocity(2100);
                                    }
                                }
                        ),
                        new WaitCommand(2000),
                        m_threeBallShoot2,
                        new InstantCommand(() -> {
                            robot.shooter.setVelocity(1500);
                        })
                )
        );
        bindToggle(GamepadKeys.Button.Y,
                new InstantCommand(() -> {
                    if (robot.limelightPoseEstimator.isValidTarget()) {
                        robot.shooterAdjust.setServos(
                                robot.shooterAdjust.goalDistanceToAngle(robot.limelightPoseEstimator.distanceToGoal())
                        );
                    }
                }
                )
        );

        bindToggle(GamepadKeys.Button.A, new InstantCommand(
                () -> {
                    if (robot.limelightPoseEstimator.isValidTarget()) {
                        headingLock = true;
                    }
                }
        ));

        bindToggle(GamepadKeys.Button.B, new InstantCommand(
                () -> {
                    headingLock = false;
                }
        ));
//        bind(GamepadKeys.Button.DPAD_LEFT, new InstantCommand(() -> {
//
//        }), new InstantCommand());
//        bind(GamepadKeys.Button.DPAD_RIGHT, new InstantCommand(() -> {
//
//        }), new InstantCommand());
    }

    public double getKp() {
        return Kp;
    }

    @Override
    public void loop() {
//        controller.setCoefficients(robot.drive.follower.constants.coefficientsHeadingPIDF);
//        controller.updateError(robot.drive.getHeadingError());
//        robot.shooter.setVelocity(getTargetVelocity());
        robot.endLoop();
        if (!headingLock) {
            robot.teleDrive.setTeleOpDrive(gamepad1Ex.getLeftY() * 0.7, gamepad1Ex.getLeftX() * 0.7, gamepad1Ex.getRightX() * 0.7);
        } else {
            robot.teleDrive.setTeleOpDrive(gamepad1Ex.getLeftY() * 0.7, gamepad1Ex.getLeftX() * 0.7, Kp * robot.limelight.getLatestResult().getTx());
        }
    }
//    private double getTargetVelocity() {
//        return TARGET_VELOCITY;
//    }

    /**
     * Binds a gamepad button to a command.
     *
     * @param button      The button to bind.
     * @param pressedCmd  The command to run when the button is pressed.
     * @param releasedCmd The command to run when the button is released.
     *
     */
    public void bind(GamepadKeys.Button button, Command pressedCmd, Command releasedCmd) {
        gamepad1Ex.getGamepadButton(button).whenPressed(pressedCmd).whenReleased(releasedCmd);
    }

    public void bindToggle(GamepadKeys.Button button, Command toggleOnCommand) {
        gamepad1Ex.getGamepadButton(button).toggleWhenPressed(toggleOnCommand);
    }

    /**
     * Binds a gamepad button to toggle between two commands.
     *
     * @param button           The button to bind.
     * @param toggleOnCommand  The command to run when the button is pressed the first time (toggled on).
     * @param toggleOffCommand The command to run when the button is pressed the second time (toggled off).
     */
    public void bindToggle(GamepadKeys.Button button, Command toggleOnCommand, Command toggleOffCommand) {
        gamepad1Ex.getGamepadButton(button).toggleWhenPressed(toggleOnCommand, toggleOffCommand);
    }
}

