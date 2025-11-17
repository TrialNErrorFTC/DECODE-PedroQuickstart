package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class DriveIntake extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor test01 = hardwareMap.dcMotor.get("test01");
        DcMotor test02 = hardwareMap.dcMotor.get("test02");
        DcMotor test03 = hardwareMap.dcMotor.get("test03");
        DcMotor test04 = hardwareMap.dcMotor.get("test04");
        DcMotor test05 = hardwareMap.dcMotor.get("test05");
        DcMotor test06 = hardwareMap.dcMotor.get("test06");
        DcMotor test07 = hardwareMap.dcMotor.get("test07");
        DcMotor test08 = hardwareMap.dcMotor.get("test08");
        DcMotor[] dcMotors = {test01, test02, test03, test04, test05, test06, test07, test08};

        telemetry.addLine("Motor Initialized");
        telemetry.update();
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            for (DcMotor testMotor: dcMotors) {
                testMotor.setPower(1.0);
            }
        }
    }
}