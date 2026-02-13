package org.firstinspires.ftc.teamcode.opmodes.newAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Change Auto", group = "Linear Opmode")
public class ChangeAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        configure();

        if (isStopRequested()) return;

        waitForStart();

    }

    private void configure() {
        while (opModeInInit()) {

            telemetry.addLine("PRESS A TO TOGGLE SIDES");
            telemetry.addData("IS RED: ", Common.isRed);
            if (gamepad1.aWasPressed()) Common.isRed = !Common.isRed;
            telemetry.update();


        }

    }
}
