package org.firstinspires.ftc.teamcode.opmodes;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

@TeleOp(name="Velocity Tuner for shooter", group="Tuning")
public class tuneVelocity extends LinearOpMode {

    private static double kP = 0;
    private static double kI = 0;
    private static double kD = 0;

    private double getkP(){
        return kP;
    }

    private double getkI(){
        return kI;
    }
    private double getkD(){
        return kD;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        MotorEx shooterMotor = new MotorEx(hardwareMap, "motorS", Motor.GoBILDA.BARE);

        Servo intakeServo = hardwareMap.get(Servo.class, "servoTransfer");

        shooterMotor.setRunMode(Motor.RunMode.VelocityControl);
        shooterMotor.setInverted(true);
        waitForStart();
        while (opModeIsActive()) {
            shooterMotor.set(1.0);
            telemetry.addData("Shooter Motor",shooterMotor.getVelocity());
            telemetry.update();

//            intakeMotor.setPower(1.0);
        }
    }
}
