package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class Transfer implements Subsystem {
    public static final Transfer INSTANCE = new Transfer();

    private Transfer() {
    }

   public ServoEx servo_transfer =  new ServoEx("servoTransfer");
    public Command shoot_position = new SetPosition(servo_transfer, 0).requires(this);
    public Command off_position = new SetPosition(servo_transfer, 0.84).requires(this);

}

