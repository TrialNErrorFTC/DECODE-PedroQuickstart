package org.firstinspires.ftc.teamcode.subsystems;

import java.util.function.Supplier;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;

public class DriveTrain implements Subsystem {
    public static final DriveTrain INSTANCE = new DriveTrain();
    private DriveTrain() {}


    private final MotorEx frontLeftMotor = new MotorEx("motorFL").reversed();
    private final MotorEx frontRightMotor = new MotorEx("motorFR");
    private final MotorEx backLeftMotor = new MotorEx("motorBL").reversed();
    private final MotorEx backRightMotor = new MotorEx("motorBR");
    public Command drive(Supplier<Double> forwards, Supplier<Double> strafe, Supplier<Double> turn){
        return new MecanumDriverControlled(
                frontLeftMotor,
                frontRightMotor,
                backLeftMotor,
                backRightMotor,
                forwards,
                strafe,
                turn
        );
    }
}
