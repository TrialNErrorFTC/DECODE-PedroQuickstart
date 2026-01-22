package org.firstinspires.ftc.teamcode.opmodes;

import com.bylazar.graph.GraphManager;
import com.bylazar.graph.PanelsGraph;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@ TeleOp
public class FlywheelTunerTutorial extends OpMode {
    public DcMotorEx flywheelMotor;

    public double lowVelocity = 1000;
    public double highVelocity = 1500;
    TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    GraphManager graphManager = PanelsGraph.INSTANCE.getManager();

    double curTargetVelocity = lowVelocity;

    double F = 0;
    double P = 0;
    double I = 0;
    double D = 0;

    double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};
    int stepIndex = 1;
    private ElapsedTime elapsedTime = new ElapsedTime();
    @Override
    public void init() {

        flywheelMotor = hardwareMap.get(DcMotorEx.class, "motorS");
        flywheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheelMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        flywheelMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        telemetry.addLine("Init Complete");

    }

    @Override
    public void loop() {
        //get all gamepad commands
        if (gamepad1.yWasPressed()){
            if (curTargetVelocity == lowVelocity){
                curTargetVelocity = highVelocity;
            } else {
                curTargetVelocity = lowVelocity;
            }
        }

        if (gamepad1.bWasPressed()){
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (gamepad1.dpadLeftWasPressed()){
            F += stepSizes[stepIndex];
        }
        if (gamepad1.dpadRightWasPressed()){
            F -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadUpWasPressed()){
            P += stepSizes[stepIndex];
        }
        if (gamepad1.dpadDownWasPressed()){
            P -= stepSizes[stepIndex];
        }



        //set new PIDF coefficients
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        flywheelMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        //set Velocity
        flywheelMotor.setVelocity(curTargetVelocity);

        double curVelocity = flywheelMotor.getVelocity();
        double error = curTargetVelocity - curVelocity;

        panelsTelemetry.addData("Target Velocity", curTargetVelocity * 60/28);
        panelsTelemetry.addData("Current Velocity", curVelocity * 60/28);
        panelsTelemetry.addData("Error",error * 60/28);
        panelsTelemetry.addLine("---------------------------");
        panelsTelemetry.addData("Tuning P", P);
        panelsTelemetry.addData("Tuning F", F);
        panelsTelemetry.addData("Step Size", stepSizes[stepIndex]);
        panelsTelemetry.update(telemetry);

        graphManager.addData("Target Velocity", curTargetVelocity * 60/28);
        graphManager.addData("Current Velocity", curVelocity * 60/28);
        graphManager.update();
        }

    }



