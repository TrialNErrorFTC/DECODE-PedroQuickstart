package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.cmd.Commandlet.run;

import com.pedropathing.control.PIDFController;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;

@TeleOp(name = "DriveTrain TeleOp")
public class DriveTeleOp extends OpMode {
    private GamepadEx gamepad1Ex;
    private RobotHardware robot;

    private boolean headingLock = false;
    private PIDFController controller;

    @Override
    public void init() {
        robot = RobotHardware.get();
        robot.init(hardwareMap, telemetry, new Pose(144 - 12, 130, Math.toRadians(135 - 90)));
        controller = new PIDFController(robot.drive.follower.constants.coefficientsHeadingPIDF);
        robot.drive.follower.startTeleOpDrive();
        gamepad1Ex = new GamepadEx(gamepad1);

        CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
                run(() -> robot.shooterAdjust.initializeServos()),
                run(() -> robot.transfer.off_position()))
        );

        Button enableHeadingLock = new GamepadButton(gamepad1Ex, GamepadKeys.Button.A)
                .toggleWhenPressed(() -> {
                    headingLock = true;
                }, () -> {
                    headingLock = false;
                });


    }


    @Override
    public void loop() {
        robot.endLoop();
        controller.updateError(robot.drive.lastAimTarget.heading);
        if (headingLock) {
            robot.drive.setTeleOpDrive(
                    gamepad1Ex.getLeftY(),
                    gamepad1Ex.getLeftX(),
                    controller.run()
            );
        } else {
            robot.drive.setTeleOpDrive(
                    -gamepad1Ex.getLeftY(),
                    -gamepad1Ex.getLeftX(),
                    -gamepad1Ex.getRightX()
            );
        }
    }

}

