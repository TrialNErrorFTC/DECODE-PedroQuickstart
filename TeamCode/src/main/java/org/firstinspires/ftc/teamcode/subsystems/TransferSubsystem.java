package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class TransferSubsystem extends SubsystemBase {

    //TODO: Rename variables
    public Servo servo_transfer;
    public CRServo servo_transfer_2;
    public TransferSubsystem(HardwareMap hMap){
        servo_transfer = hMap.get(Servo.class, "servoTransfer");
        servo_transfer_2 = hMap.get(CRServo.class, "servoTransfer2");
        servo_transfer_2.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void shoot_position() {
        servo_transfer.setPosition(0);
    }
    public void off_position(){
        servo_transfer.setPosition(0.84);
    }

    public void transfer(){
        servo_transfer_2.setPower(1);
    }
    public void stop(){
        servo_transfer_2.setPower(0);
    }
}