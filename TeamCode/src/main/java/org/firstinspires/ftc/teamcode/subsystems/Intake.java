package org.firstinspires.ftc.teamcode.subsystems;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() {}
    private MotorEx intake_motor = new MotorEx("motorI");
    public Command forward = new SetPower(intake_motor, 0.7).requires(this);
    public Command reverse = new SetPower(intake_motor, -0.7).requires(this);
    public Command stop = new SetPower(intake_motor, 0.0).requires(this);
}
