package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

/**
 * The Shooter subsystem is responsible for controlling the robot's shooter mechanism.
 * It uses a single motor to launch items.
 */
public class Shooter implements Subsystem {
    /**
     * Singleton instance of the Shooter subsystem.
     */
    public static final Shooter INSTANCE = new Shooter();


    /**
     * Private constructor to prevent instantiation outside of the singleton.
     */
    private Shooter() {}

    /**
     * The motor for the shooter system.
     */
    private final MotorEx shooter_motor = new MotorEx("motorS").reversed();


    /**
     * Runnable to set the shooter to high speed.
     */
    public Command highSpeed = new SetPower(shooter_motor, 0.75).requires(this);
    /**
     * Runnable to set the shooter to normal speed. (this is adjustable speed for the tuning in order to accurately shoot)
     */
    public Command normalSpeed = new SetPower(shooter_motor, 0.5).requires(this);
    /**
     * Runnable to set the shooter to low speed.
     */
    public Command lowSpeed = new SetPower(shooter_motor, 0.3).requires(this);


    /**
     * Command to stop the shooter motor.
     */
    public Command stop = new SetPower(shooter_motor, 0.0).requires(this);

}
