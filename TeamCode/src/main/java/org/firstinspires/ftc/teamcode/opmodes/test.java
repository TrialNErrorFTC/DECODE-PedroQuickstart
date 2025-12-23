package org.firstinspires.ftc.teamcode.opmodes;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODERS;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable

@Autonomous(name = "test")
public class test extends LinearOpMode {

    public static double targetPower = 0.85;
    public static double targetPosition = 0.5;


    public double getTargetPower(){
        return targetPower;
    }

    public double getTargetPosition(){
        return targetPosition;
    }

@Override
    public void runOpMode() throws InterruptedException {
        DcMotor shooterMotor = hardwareMap.get(DcMotor.class, "motorS");
        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, "motorI");
        Servo intakeServo = hardwareMap.get(Servo.class, "servoTransfer");

        waitForStart();
        while (opModeIsActive()) {
            shooterMotor.setMode(RUN_WITHOUT_ENCODER);
            shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            shooterMotor.setPower(getTargetPower());
            intakeServo.setPosition(getTargetPosition());
//            intakeMotor.setPower(1.0);
        }
    }


}
