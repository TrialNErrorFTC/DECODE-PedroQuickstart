package org.firstinspires.ftc.teamcode.opmodes;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODERS;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

@Configurable

@Autonomous(name = "test")
public class test extends LinearOpMode {

    public static double targetPower = 0.85;
    public static double targetPosition = 0.5;
    PanelsTelemetry panelsTelemetry = PanelsTelemetry.INSTANCE;

    public static double Kp = 0;
    public static double Ki = 0;
    public static double Kd = 0;
    public static double Kv = 0;

    public double getTargetPower(){
        return targetPower;
    }

    public double getKp(){
        return Kp;
    }
    public double getKi(){
        return Ki;
    }
    public double getKd(){
        return Kd;
    }
    public double getKv(){
        return Kv;
    }

    public double getTargetPosition(){
        return targetPosition;
    }

@Override
    public void runOpMode() throws InterruptedException {
        MotorEx shooterMotor =new MotorEx(hardwareMap, "motorS", Motor.GoBILDA.BARE);
        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, "motorI");
        Servo intakeServo = hardwareMap.get(Servo.class, "servoTransfer");

        waitForStart();
        while (opModeIsActive()) {
            shooterMotor.setRunMode(Motor.RunMode.VelocityControl);
            shooterMotor.setVeloCoefficients(Kp, Ki, Kd);
            shooterMotor.setFeedforwardCoefficients(0, Kv);
//            intakeMotor.setPower(1.0);
            shooterMotor.setVelocity(1000);
        }
    }


}
