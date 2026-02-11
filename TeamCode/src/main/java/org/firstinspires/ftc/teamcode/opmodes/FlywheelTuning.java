package org.firstinspires.ftc.teamcode.opmodes;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.PIDFControllerNew;

@Configurable
@TeleOp
public class FlywheelTuning extends OpMode {
    private PIDFControllerNew controller;
    private DcMotorEx motor;
    public static double targetVelocity, velocity;

    public ElapsedTime timer;
    public static double P, I, kV, kS;
    JoinedTelemetry joinedTelemetry = new JoinedTelemetry(PanelsTelemetry.INSTANCE.getFtcTelemetry(), telemetry);
    private DcMotorEx motor2;

    @Override
    public void init() {
        //TODO: Set motor name and direction
        motor = hardwareMap.get(DcMotorEx.class, "motorS");
        motor2 = hardwareMap.get(DcMotorEx.class, "motorS2");
        timer = new ElapsedTime();
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        controller = new PIDFControllerNew(getP(), getI(), 0.0, 0.0, timer);
    }

    private double getI() {
        return I;
    }

    private double getP() {
        return P;
    }

    @Override
    public void loop() {
        joinedTelemetry.addData("TargetVel", getTargetVelocity());
        joinedTelemetry.addData("CurrentVel", velocity * 60 / 28);
        joinedTelemetry.addData("Vel Error", getTargetVelocity() - velocity * 60 / 28);
        joinedTelemetry.addData("Time", 1.0 / timer.seconds());
        controller.setPIDF(getP(), getI(), 0.0, getKV());
        velocity = (motor.getVelocity() + motor2.getVelocity()) / 2;
        motor.setPower(controller.calculate(getTargetVelocity() * 28 / 60, velocity));
        motor2.setPower(controller.calculate(getTargetVelocity() * 28 / 60, velocity));
        joinedTelemetry.update();


    }

    private double getKS() {
        return kS;
    }

    private double getTargetVelocity() {
        return targetVelocity;
    }

    private double getKV() {
        return kV;
    }
}
