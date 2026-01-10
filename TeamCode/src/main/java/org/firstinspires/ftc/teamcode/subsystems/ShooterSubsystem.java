package org.firstinspires.ftc.teamcode.subsystems;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODERS;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    public Servo servoLeft, servoRight;
    DcMotor motorShooter;
    public ShooterSubsystem(final HardwareMap hMap){
        servoLeft = hMap.get(Servo.class, "servoLeft");
        servoRight = hMap.get(Servo.class, "servoRight");
        motorShooter = hMap.get(DcMotor.class, "motorS");
        motorShooter.setMode(RUN_WITHOUT_ENCODER);
        servoLeft.setDirection(Servo.Direction.REVERSE);
        servoRight.setDirection(Servo.Direction.FORWARD);
    }
    public void setServos(double angle){
        servoLeft.setPosition(angle);
        servoRight.setPosition(angle);

    }

    public void initializeServos(){
        setServos(1);
    }
    public void highSpeed(){
        motorShooter.setPower(0.75);
    }

    public void stop(){
        motorShooter.setPower(0);
    }
}