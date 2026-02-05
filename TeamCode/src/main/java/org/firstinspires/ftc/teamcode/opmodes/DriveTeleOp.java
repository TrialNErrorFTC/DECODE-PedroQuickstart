package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.cmd.Commandlet.fork;
import static org.firstinspires.ftc.teamcode.cmd.Commandlet.run;
import static org.firstinspires.ftc.teamcode.cmd.Commandlet.waitFor;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.PIDFController;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.pedroCommand.TurnCommand;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;

@Configurable
@TeleOp(name = "DriveTrain TeleOp")
public class DriveTeleOp extends OpMode {
    public static double TARGET_VELOCITY = 0.0;
    private GamepadEx gamepad1Ex;
    private RobotHardware robot;

    private PIDFController controller;

    private boolean headingLock = false;

    @Override
    public void init() {
        robot = RobotHardware.get();
        robot.init(hardwareMap, telemetry, new Pose(72, 72, Math.toRadians(90)));
        RobotHardware.alliance = RobotHardware.Alliance.RED;

        robot.drive.follower.startTeleOpDrive(true);
        gamepad1Ex = new GamepadEx(gamepad1);

//        controller = new PIDFController(robot.drive.follower.constants.coefficientsHeadingPIDF);
//
//        InstantCommand m_initialize = new InstantCommand(robot.shooterAdjust::initializeServos);
//        InstantCommand m_offCommand = new InstantCommand(robot.transfer::off_position);

//        SequentialCommandGroup m_threeBallShoot2 = new SequentialCommandGroup(
//                fork(
//                        new InstantCommand(robot.transfer::shoot_position),
//                        new InstantCommand(() -> {
//                            robot..motor_intake.setPower(0.375);
//                        })
//                ),
//                waitFor(400),
//                fork(
//                        new InstantCommand(m_transfer::transfer),
//                        new InstantCommand(m_intake::forward)
//                ),
//                waitFor(3000),
//                fork(
//                        new InstantCommand(m_transfer::stop),
//                        new InstantCommand(m_intake::stop)
//                ),
//                new InstantCommand(m_transfer::off_position)
//
//        );
//        CommandScheduler.getInstance().schedule(new SequentialCommandGroup(run(() -> robot.shooterAdjust.initializeServos()), run(() -> robot.transfer.off_position())));

//        bind(GamepadKeys.Button.A,
//                new TurnCommand(robot.drive.follower, Math.toRadians(robot.drive.lastAimTarget.heading),
//                        MathFunctions.getTurnDirection(robot.drive.lastPose.getHeading(),
//                                Math.toRadians(robot.drive.lastAimTarget.heading)) == 1),
//                new InstantCommand());
//        bind(GamepadKeys.Button.B, new InstantCommand(() -> {
//            headingLock = !headingLock;
//        }), new InstantCommand());
//        bind(GamepadKeys.Button.DPAD_UP, new InstantCommand(() -> {
//            robot.shooter.setVelocity(1500);
//        }), new InstantCommand());
//        bind(GamepadKeys.Button.DPAD_LEFT, new InstantCommand(() -> {
//
//        }), new InstantCommand());
//        bind(GamepadKeys.Button.DPAD_RIGHT, new InstantCommand(() -> {
//
//        }), new InstantCommand());
    }

    @Override
    public void loop() {
//        controller.setCoefficients(robot.drive.follower.constants.coefficientsHeadingPIDF);
//        controller.updateError(robot.drive.getHeadingError());
//        robot.shooter.setVelocity(getTargetVelocity());
        robot.endLoop();
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

