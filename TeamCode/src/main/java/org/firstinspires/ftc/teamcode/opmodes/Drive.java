package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

/**
 * The Drive class is the main TeleOp opmode for the robot.
 * It initializes the robot's subsystems and components, and maps gamepad controls to robot actions.
 */
@TeleOp(name = "Drive(NextFTC)")
public class Drive extends NextFTCOpMode {
    private static final Logger log = LoggerFactory.getLogger(Drive.class);

    /**
     * Constructor for the Drive opmode.
     * It adds the necessary components for the robot to function.
     */
    public Drive() {
        addComponents(
                new SubsystemComponent(Intake.INSTANCE, Shooter.INSTANCE, DriveTrain.INSTANCE), // Adds the Intake and Shooter subsystems
                BulkReadComponent.INSTANCE, // Enables bulk reading of sensor data
                BindingsComponent.INSTANCE // Enables gamepad control bindings
        );
    }
    // Motor declarations for the mecanum drive
    private final MotorEx frontLeftMotor = new MotorEx("motorFL").reversed();
    private final MotorEx frontRightMotor = new MotorEx("motorFR");
    private final MotorEx backLeftMotor = new MotorEx("motorBL").reversed();
    private final MotorEx backRightMotor = new MotorEx("motorBR");

    /**
     * This method is called when the start button is pressed on the driver station.
     * It sets up the driver-controlled mecanum drive and the gamepad button bindings.
     */

    @Override
    public void onInit(){
        Transfer.INSTANCE.off_position.schedule();
    }
    @Override
    public void onStartButtonPressed() {
        Double speed = 0.7; // Default driving speed
        // Command for driver-controlled mecanum drive
        Command driverControlled = new MecanumDriverControlled(
                frontLeftMotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                Gamepads.gamepad1().leftStickY().negate().map((x)->speed * x),  // Forward/backward movement
                Gamepads.gamepad1().leftStickX(), // Strafe movement
                Gamepads.gamepad1().rightStickX().map((x) -> x) // Rotational movement
        );


        driverControlled.schedule(); // Schedules the driver-controlled command to run

        // Gamepad 1 Button Mappings

        // Right Bumper: Toggles the intake on and off
        Gamepads.gamepad1().rightBumper()
                .whenBecomesTrue(Intake.INSTANCE.forward)
                .whenBecomesFalse(Intake.INSTANCE.stop);

        // Left Bumper: Activates the shooter
        Gamepads.gamepad1().leftBumper()
                .whenBecomesTrue(Transfer.INSTANCE.shoot_position)
                .whenBecomesFalse(Transfer.INSTANCE.off_position);

        // D-pad Up: Sets the shooter to high speed
        Gamepads.gamepad1().cross()
                .toggleOnBecomesTrue()
                .whenBecomesTrue(Shooter.INSTANCE.highSpeed)
                .whenBecomesFalse(Shooter.INSTANCE.stop);

        // Triangle: Sets the intake to reverse direction
        Gamepads.gamepad1().triangle()
                .whenBecomesTrue(Intake.INSTANCE.reverse)
                .whenBecomesFalse(Intake.INSTANCE.stop);


    }
}
