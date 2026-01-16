package org.firstinspires.ftc.teamcode.subsystems;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODERS;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class ShooterSubsystem extends SubsystemBase {
    public Servo servoLeft, servoRight;
    DcMotorEx motorShooter;
    public ShooterSubsystem(final HardwareMap hMap){
        servoLeft = hMap.get(Servo.class, "servoLeft");
        servoRight = hMap.get(Servo.class, "servoRight");
        motorShooter = hMap.get(DcMotorEx.class, "motorS");
        motorShooter.setMode(RUN_WITHOUT_ENCODER);
        servoLeft.setDirection(Servo.Direction.REVERSE);
        servoRight.setDirection(Servo.Direction.FORWARD);
        motorShooter.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void setServos(double angle){
        servoLeft.setPosition(angle);
        servoRight.setPosition(angle);

    }

    public void initializeServos(){
        setServos(1);
    }
    public void highSpeed(){
        motorShooter.setPower(0.5);
    }
    public void lowSpeed(){motorShooter.setPower(0.3); }

    public void stop(){
        motorShooter.setPower(0);
    }
}