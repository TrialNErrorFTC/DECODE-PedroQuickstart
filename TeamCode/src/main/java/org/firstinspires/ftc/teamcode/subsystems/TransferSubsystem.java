package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class TransferSubsystem extends SubsystemBase {
    public Servo servo_transfer;
    public TransferSubsystem(HardwareMap hMap){
        servo_transfer = hMap.get(Servo.class, "servoTransfer");
    }
    public void shoot_position(){
        servo_transfer.setPosition(0);
    }
    public void off_position(){
        servo_transfer.setPosition(0.84);
    }
}