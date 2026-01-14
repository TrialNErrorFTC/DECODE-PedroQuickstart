package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commands.DefaultDrive;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;

@TeleOp
public class driveTrainTestSolvers extends CommandOpMode {
    @Override
    public void initialize() {
        GamepadEx driverOp = new GamepadEx(gamepad1);
        ShooterSubsystem m_shooter = new ShooterSubsystem(hardwareMap);
        IntakeSubsystem m_intake = new IntakeSubsystem(hardwareMap);
        TransferSubsystem m_transfer = new TransferSubsystem(hardwareMap);
        DrivetrainSubsystem m_drivetrain = new DrivetrainSubsystem(hardwareMap);

        DefaultDrive m_driveCommand = new DefaultDrive(m_drivetrain, ()->{return 0.6;},()->{return 0;}, ()->{return 0;});


        Button shootButton2 = new GamepadButton(
                driverOp, GamepadKeys.Button.LEFT_BUMPER
        ).whenPressed(m_driveCommand);
    }
}
