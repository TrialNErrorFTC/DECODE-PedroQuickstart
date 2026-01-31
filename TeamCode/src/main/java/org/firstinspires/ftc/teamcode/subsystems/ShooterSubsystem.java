package org.firstinspires.ftc.teamcode.subsystems;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;

import com.qualcomm.hardware.motors.GoBILDA5201Series;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class ShooterSubsystem extends SubsystemBase {
    public Servo servoLeft, servoRight;
    public DcMotorEx motorShooter;
    public ShooterSubsystem(final HardwareMap hMap){
        //initalize servo
        servoLeft = hMap.get(Servo.class, "servoLeft");
        servoRight = hMap.get(Servo.class, "servoRight");

        motorShooter = hMap.get(DcMotorEx.class, "motorS");
        motorShooter.setPIDFCoefficients(RUN_USING_ENCODER, new PIDFCoefficients(60.598,0,0,16.378));

        motorShooter.setDirection(DcMotorSimple.Direction.REVERSE);

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
        motorShooter.setVelocity((double) (2100 * 28) /60);
    }
    public void lowSpeed(){
        motorShooter.setVelocity((double) (1071 * 28)/60);
    }

    public void setSpeed(){

    }

    public void stop(){
        motorShooter.setVelocity(0);
    }
}