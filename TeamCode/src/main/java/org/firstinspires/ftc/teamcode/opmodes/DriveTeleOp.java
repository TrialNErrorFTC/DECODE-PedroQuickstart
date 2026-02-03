package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.cmd.Commandlet.run;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;

@TeleOp(name = "DriveTrain TeleOp")
public class DriveTeleOp extends OpMode {
    private GamepadEx gamepad1Ex;
    private RobotHardware robot;

    private boolean headingLock = false;

    @Override
    public void init() {
        robot = RobotHardware.get();
        robot.init(hardwareMap, telemetry, new Pose(72, 72, Math.toRadians(90)));
        RobotHardware.alliance = RobotHardware.Alliance.RED;

        robot.drive.follower.startTeleOpDrive();
        gamepad1Ex = new GamepadEx(gamepad1);

        CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
                run(() -> robot.shooterAdjust.initializeServos()),
                run(() -> robot.transfer.off_position()))
        );
    }

    @Override
    public void loop() {
        robot.endLoop();
        if (headingLock) {
            robot.drive.setTeleOpDrive(
                    gamepad1Ex.getLeftY(),
                    -gamepad1Ex.getLeftX(),
                    -gamepad1Ex.getRightX()
            );
        } else {
            robot.drive.setTeleOpDrive(
                    gamepad1Ex.getLeftY(),
                    -gamepad1Ex.getLeftX(),
                    -gamepad1Ex.getRightX()
            );
        }
    }

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

    public void bindToggle(GamepadKeys.Button button, Command toggleOnCommand, Command toggleOffCommand) {
        gamepad1Ex.getGamepadButton(button).toggleWhenPressed(toggleOnCommand, toggleOffCommand);
    }
}

