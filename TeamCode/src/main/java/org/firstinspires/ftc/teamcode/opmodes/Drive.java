package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Drive(NextFTC)")
public class Drive extends NextFTCOpMode {
    public Drive() {
        addComponents(
                new SubsystemComponent(Intake.INSTANCE, Shooter.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }
    private final MotorEx frontLeftMotor = new MotorEx("motorFL").reversed();
    private final MotorEx frontRightMotor = new MotorEx("motorFR");
    private final MotorEx backLeftMotor = new MotorEx("motorBL").reversed();
    private final MotorEx backRightMotor = new MotorEx("motorBR");

    @Override
    public void onStartButtonPressed() {
        Command driverControlled = new MecanumDriverControlled(
                frontLeftMotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
        );

        driverControlled.schedule();

        Gamepads.gamepad1().triangle()
                .whenBecomesTrue(Intake.INSTANCE.forward)
                .whenBecomesFalse(Intake.INSTANCE.stop);

        Gamepads.gamepad1().circle()
                .whenBecomesTrue(Intake.INSTANCE.reverse)
                .whenBecomesFalse(Intake.INSTANCE.stop);

        Gamepads.gamepad1().square()
                .whenBecomesTrue(Shooter.INSTANCE.forward)
                .whenBecomesFalse(Shooter.INSTANCE.stop);

        Gamepads.gamepad1().cross()
                .whenBecomesTrue(Shooter.INSTANCE.reverse)
                .whenBecomesFalse(Shooter.INSTANCE.stop);

        /*
        * Goals for Today:
        * Setup Buttons
        * Setup Code for Field Driving
        * */

        //Buttons

        //TODO: Setup the right bumper -> forward
        //TODO: Setup left bumper -> shoot
        //TODO: Setup
    }
}
