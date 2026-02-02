package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.cmd.Commandlet.run;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

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
        robot.init(hardwareMap, telemetry, MecanumDrive.lastPose);

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
}

