package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.commands.TeleOpDrive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.NonPedroDrive;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
@TeleOp
public class Drive extends CommandOpMode {
    private NonPedroDrive m_robotDrive;
    private Motor fL, fR, bL, bR, shooter, intake;
    private GamepadEx m_driverOp;
    private TeleOpDrive m_driveCommand;

    private Intake m_intake;
    private ShooterSubsystem m_shooter;
    private InstantCommand m_intakeSpinCommand;
    private Button m_intakeSpinButton;
    private InstantCommand m_intakeReverseCommand;
    private InstantCommand m_intakeStopCommand;
    private InstantCommand m_shooterSpinCommand;
    private InstantCommand m_shooterReverseCommand;
    private InstantCommand m_shooterStopCommand;
    private Button m_intakeReverseButton;
    private Trigger m_intakeStopTrigger;
    private Trigger m_shooterStopTrigger;

    public void initialize(){
        // create our drive object
        m_robotDrive = new NonPedroDrive(hardwareMap);

        // create shooter object
        m_intake = new Intake(hardwareMap, "motorI");

        // create intake object
        m_shooter = new ShooterSubsystem(hardwareMap, "motorS");

        //driver
        m_driverOp = new GamepadEx(gamepad1);

        //setup commands
        m_driveCommand = new TeleOpDrive(m_robotDrive, () -> m_driverOp.getLeftX(), () -> m_driverOp.getRightX(), () -> m_driverOp.getLeftY());


        //intake commands
        m_intakeSpinCommand = new InstantCommand(() -> {
            telemetry.addLine("Intake Spinning Forwards");
            telemetry.update();

            m_intake.spin();

        }, m_intake);
        m_intakeReverseCommand = new InstantCommand(() -> {
            telemetry.addLine("Intake Spinning Reverse");
            telemetry.update();

            m_intake.reverse();
        }, m_intake);
        m_intakeStopCommand = new InstantCommand(() -> {
            telemetry.addLine("Intake Stop");
            telemetry.update();
            m_intake.stop();
        }, m_intake);


        //shooter commands
        m_shooterSpinCommand = new InstantCommand(() -> {
            telemetry.addLine("Shooter Spinning Forwards");
            telemetry.update();
            m_shooter.spin();
        }, m_shooter);
        m_shooterReverseCommand = new InstantCommand(() -> {
            telemetry.addLine("Shooter Spinning Reverse");
            telemetry.update();
            m_shooter.reverse();
        }, m_shooter);
        m_shooterStopCommand = new InstantCommand(() -> {
            telemetry.addLine("Shooter Stopped");
            telemetry.update();
            m_shooter.stop();
        }, m_shooter);

        //create intake buttons(2 buttons for now)
        m_intakeSpinButton = (new GamepadButton(m_driverOp, GamepadKeys.Button.TRIANGLE).whileHeld(m_intakeSpinCommand));
        m_intakeReverseButton = (new GamepadButton(m_driverOp, GamepadKeys.Button.CIRCLE).whileHeld(m_intakeReverseCommand));

        //create shooter buttons(2 buttons for now)
        m_intakeSpinButton = (new GamepadButton(m_driverOp, GamepadKeys.Button.SQUARE).whileHeld(m_intakeSpinCommand));
        m_intakeReverseButton = (new GamepadButton(m_driverOp, GamepadKeys.Button.CROSS).whileHeld(m_intakeReverseCommand));

        // create stop triggers for intake and shooter
        m_intakeStopTrigger = (new GamepadButton(m_driverOp, GamepadKeys.Button.TRIANGLE)
                .or(new GamepadButton(m_driverOp, GamepadKeys.Button.CIRCLE))
                .whenInactive(m_intakeStopCommand)
        );

        m_shooterStopTrigger = (new GamepadButton(m_driverOp, GamepadKeys.Button.SQUARE)
                .or(new GamepadButton(m_driverOp, GamepadKeys.Button.CROSS))
                .whenInactive(m_intakeStopCommand)
        );

        telemetry.addLine("Setting Up All subsystems:");
        telemetry.update();

        register(m_shooter);

        telemetry.addLine("Shooter is set up");
        telemetry.update();

        register(m_intake);

        telemetry.addLine("Intake is set up");
        telemetry.update();

        register(m_robotDrive);

        telemetry.addLine("DriveTrain is set up");
        telemetry.update();

        //initial try

        telemetry.addLine("Default Commands are setting up");
        m_robotDrive.setDefaultCommand(m_driveCommand);
    }

}
