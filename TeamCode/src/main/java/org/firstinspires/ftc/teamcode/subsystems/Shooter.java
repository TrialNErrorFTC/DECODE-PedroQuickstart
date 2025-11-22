package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Shooter implements Subsystem {
    public static final Shooter INSTANCE = new Shooter();
    private Shooter() {}

    private MotorEx shooter_motor = new MotorEx("motorS");
    public Command forward = new SetPower(shooter_motor, 0.7).requires(this);
    public Command reverse = new SetPower(shooter_motor, -0.7).requires(this);
    public Command stop = new SetPower(shooter_motor, 0.0).requires(this);

}
