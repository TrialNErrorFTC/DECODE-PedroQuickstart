package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.PerpetualCommand;
import com.seattlesolvers.solverslib.command.RepeatCommand;
import com.seattlesolvers.solverslib.command.ScheduleCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.TriggerReader;

import org.firstinspires.ftc.robotcontroller.external.samples.UtilityOctoQuadConfigMenu;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.commands.DefaultDrive;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;

/**
 * This TeleOp class manages driver control of the robot using the SolversLib command-based framework.
 * It's responsible for initializing subsystems and mapping gamepad inputs to robot actions.
 */
@TeleOp
public class DriveSolvers extends CommandOpMode {

    private ShooterSubsystem m_shooter;
    private IntakeSubsystem m_intake;
    private TransferSubsystem m_transfer;
    private DrivetrainSubsystem m_drivetrain;
    private LimelightSubsystem m_limelight;

    /**
     * This method is executed once when the OpMode is initialized.
     * It handles the setup of all necessary components for driver control,
     * such as subsystems and button bindings for commands.
     */


    @Override
    public void initialize() {
        GamepadEx driverOp = new GamepadEx(gamepad1);
        m_shooter = new ShooterSubsystem(hardwareMap);
        m_intake = new IntakeSubsystem(hardwareMap);
        m_transfer = new TransferSubsystem(hardwareMap);
        m_drivetrain = new DrivetrainSubsystem(hardwareMap);

        InstantCommand m_initialize = new InstantCommand(m_shooter::initializeServos);
        InstantCommand m_offCommand = new InstantCommand(m_transfer::off_position);


        DefaultDrive m_driveCommand = new DefaultDrive(m_drivetrain,
                ()->{return driverOp.getLeftY() * 0.7;},
                ()->{return driverOp.getLeftX() * 0.7;},
                ()->{return driverOp.getRightX() * 0.7;}
                );
        SequentialCommandGroup m_threeBallShoot = new SequentialCommandGroup(
                //shoot first one
                new InstantCommand(m_transfer::shoot_position),
                new WaitCommand(400),
                //servo down
                new InstantCommand(m_transfer::off_position),
                new WaitCommand(500),
                //intake
                new InstantCommand(() -> {
                    m_intake.motor_intake.setPower(0.375);
                }),
                new WaitCommand(400),
                new InstantCommand(m_intake::stop),
                //shoot
                new InstantCommand(m_transfer::shoot_position),
                new WaitCommand(400),
                //servo down
                new InstantCommand(m_transfer::off_position),
                new WaitCommand(400),
                //outtake
                new InstantCommand(() -> {
                    m_intake.motor_intake.setPower(-0.3);
                }),
                new WaitCommand(200),
                new InstantCommand(m_intake::stop),
                new WaitCommand(100),
                //intake
                new InstantCommand(() -> {
                    m_intake.motor_intake.setPower(0.7);
                }),
                new WaitCommand(1000),
                //stop
                new InstantCommand(m_intake::stop),
                //shoot
                new InstantCommand(m_transfer::shoot_position),
                new WaitCommand(500),
                new InstantCommand(m_transfer::off_position)
        );
//                new InstantCommand(m_intake::reverse).withTimeout(1000),
//                new InstantCommand(m_intake::forward).withTimeout(3000),
//                new InstantCommand(m_intake::stop),
//                new InstantCommand(m_transfer::off_position)
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
        Button highSpeed = new GamepadButton(
                driverOp, GamepadKeys.Button.DPAD_UP
        ).toggleWhenPressed(new InstantCommand(m_shooter::highSpeed));

        Button stopShooter = new GamepadButton(
                driverOp, GamepadKeys.Button.DPAD_DOWN
        ).toggleWhenPressed(new InstantCommand(m_shooter::stop));

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
        ).whenPressed(new ScheduleCommand(
                new InstantCommand(m_intake::forward),
                new InstantCommand(m_transfer::transfer)
        )).whenReleased(new ScheduleCommand(
                new InstantCommand(m_intake::stop),
                new InstantCommand(m_transfer::stop)
        ));

//        //Binds the 'Left Bumper' button to a command that moves the transfer to the shoot position.
        Button shootButton = new GamepadButton(
                driverOp, GamepadKeys.Button.LEFT_BUMPER
        ).whenPressed(new InstantCommand(
                m_transfer::shoot_position
        )).whenReleased(new InstantCommand(
                m_transfer::off_position
        ));
//        Button increaseFlywheelSpeed = new GamepadButton(
//                driverOp, GamepadKeys.Button.DPAD_LEFT
//        ).whenPressed(
//                new InstantCommand(
//                        ()-> {
//                            m_shooter.motorShooter.set(m_shooter.motorShooter.get() + 0.1);
//                        }
//                )
//        );
//        Button decreaseFlywheelSpeed = new GamepadButton(
//                driverOp, GamepadKeys.Button.DPAD_RIGHT
//        ).whenPressed(
//                new InstantCommand(
//                        ()-> {
//                            m_shooter.motorShooter.set(m_shooter.motorShooter.get() - 0.1);
//                        }
//                )
//        );

        Button shootButton2 = new GamepadButton(
                driverOp, GamepadKeys.Button.X
        ).toggleWhenPressed(m_threeBallShoot);
    }
    public void run(){
        super.run();

        telemetry.addData("Shooter Velocity", m_shooter.motorShooter.getVelocity() * 60/28);
        telemetry.addData("Shooter Position", m_shooter.servoRight.getPosition());
        telemetry.addData("Transfer Position", m_transfer.servo_transfer.getPosition());
        telemetry.addData("Shooter Power", m_shooter.motorShooter.getVelocity());
        telemetry.update();
    }
}
