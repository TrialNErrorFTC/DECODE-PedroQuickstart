package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.PerpetualCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.TriggerReader;

import org.firstinspires.ftc.teamcode.commands.DefaultDrive;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;

/**
 * This TeleOp class manages driver control of the robot using the SolversLib command-based framework.
 * It's responsible for initializing subsystems and mapping gamepad inputs to robot actions.
 */
@TeleOp
public class DriveSolvers extends CommandOpMode {

    /**
     * This method is executed once when the OpMode is initialized.
     * It handles the setup of all necessary components for driver control,
     * such as subsystems and button bindings for commands.
     */
    @Override
    public void initialize() {
        GamepadEx driverOp = new GamepadEx(gamepad1);
        ShooterSubsystem m_shooter = new ShooterSubsystem(hardwareMap);
        IntakeSubsystem m_intake = new IntakeSubsystem(hardwareMap);
        TransferSubsystem m_transfer = new TransferSubsystem(hardwareMap);
        DrivetrainSubsystem m_drivetrain = new DrivetrainSubsystem(hardwareMap);


        InstantCommand m_initialize = new InstantCommand(m_shooter::initializeServos);
        InstantCommand m_offCommand = new InstantCommand(m_transfer::off_position);
        DefaultDrive m_driveCommand = new DefaultDrive(m_drivetrain, driverOp::getLeftY, driverOp::getLeftX, driverOp::getRightX);
        SequentialCommandGroup m_newShootCommand = new SequentialCommandGroup(
                new InstantCommand(m_transfer::shoot_position).withTimeout(350),
                new InstantCommand(m_intake::reverse).withTimeout(50),
                new InstantCommand(m_intake::forward).withTimeout(50)
        );

        m_offCommand.addRequirements(m_transfer);

        m_drivetrain.setDefaultCommand(m_driveCommand);
        m_offCommand.schedule();
        m_initialize.schedule();

        // Binds the 'A' button to a command that incrementally moves the shooter servos up.
        Button moveUpButton = new GamepadButton(
                driverOp, GamepadKeys.Button.A
        ).whenPressed(new InstantCommand(
                () -> {
                    m_shooter.setServos(m_shooter.servoRight.getPosition() - 0.1);
                }
        ));

        // Binds the 'B' button to a command that incrementally moves the shooter servos down.
        Button moveDownButton = new GamepadButton(
                driverOp, GamepadKeys.Button.B
        ).whenPressed(new InstantCommand(
                () -> {
                    m_shooter.setServos(m_shooter.servoRight.getPosition() + 0.1);
                }
        ));

        //Binds the 'X' button to a command that stops the shooter.
        Button stopButton = new GamepadButton(
                driverOp, GamepadKeys.Button.X
        ).toggleWhenPressed(new InstantCommand(m_shooter::highSpeed), new InstantCommand(m_shooter::stop));

        //Binds the 'Y' button to a command that reverse the intake.
        Button reverseButton = new GamepadButton(
                driverOp, GamepadKeys.Button.Y
        ).whenPressed(new InstantCommand(
                m_intake::reverse
        )).whenReleased(new InstantCommand(
                m_intake::stop
        ));

        //Binds the 'Right Bumper' button to a command that runs the intake.
        Button intakeButton = new GamepadButton(
                driverOp, GamepadKeys.Button.RIGHT_BUMPER
        ).whenPressed(new InstantCommand(
                m_intake::forward
        )).whenReleased(new InstantCommand(
                m_intake::stop
        ));

//        //Binds the 'Left Bumper' button to a command that moves the transfer to the shoot position.
//        Button shootButton = new GamepadButton(
//                driverOp, GamepadKeys.Button.LEFT_BUMPER
//        ).whenPressed(new InstantCommand(
//                m_transfer::shoot_position
//        )).whenReleased(new InstantCommand(
//                m_transfer::off_position
//        ));

        Button shootButton2 = new GamepadButton(
                driverOp, GamepadKeys.Button.LEFT_BUMPER
        ).whenPressed(m_newShootCommand);
    }
}
