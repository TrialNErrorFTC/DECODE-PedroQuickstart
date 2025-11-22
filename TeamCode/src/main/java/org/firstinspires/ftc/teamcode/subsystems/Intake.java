package org.firstinspires.ftc.teamcode.subsystems;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;

public class Intake implements Subsystem {
    public static final Intake INSTANCE = new Intake();
    private Intake() {}
    private MotorEx intake_motor = new MotorEx("motorI";

    private double speed = 0.7;
    public Command spin = new SetPower(intake_motor, speed).requires(this);
    public Command toggleReverse = new InstantCommand()
    public Command stop = new SetPower(intake_motor, 0.0).requires(this);
}
 /*
intake - r bumper,
servo that shoots - left bumper

configuration
- intake is all
  */