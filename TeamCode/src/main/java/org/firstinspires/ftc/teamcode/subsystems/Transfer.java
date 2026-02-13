package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;


public class Transfer extends SubsystemBase {

    //TODO: Rename variables
    public Servo servo_transfer;
    public CRServo servo_transfer_2;
    RobotHardware robot;


    public Transfer() {
        robot = RobotHardware.get();
    }

    public void shoot_position() {
        robot.servoTransferShooter.set(0);
    }

    public void off_position() {
        robot.servoTransferShooter.set(0.84);
    }

    public void stop() {
        robot.servoTransferIntake.set(0);
    }
}
