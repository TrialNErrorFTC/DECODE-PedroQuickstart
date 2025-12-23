package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import dev.nextftc.core.units.Distance
import dev.nextftc.core.units.Angle
import dev.nextftc.core.units.deg
import dev.nextftc.core.units.inch

class distanceEstimation: LinearOpMode() {

    lateinit var limelight: Limelight3A;
    lat

    override fun runOpMode() {
        limelight = hardwareMap.get(Limelight3A::class.java, "limelight");
        limelight.setPollRateHz(100);
        limelight.start();
        limelight.pipelineSwitch(0);


    }
    fun update_limelight_tracking(){

        val result = limelight.getLatestResult()
        if (result != null && result.isValid()) {
            val tx = result.getTx() // How far left or right the target is (degrees)
            val ty = result.getTy() // How far up or down the target is (degrees)
            val ta = result.getTa() // How big the target looks (0%-100% of the image)

            telemetry.addData("Target X", tx)
            telemetry.addData("Target Y", ty)
            telemetry.addData("Target Area", ta)
        } else {
            telemetry.addData("Limelight", "No Targets")
        }

        val limelightMountAngle: Angle = 90.deg; //TODO: Change This
        val limelightLensHeightIn: Distance = 10.inch; //TODO: Change This
        val goalHeightIn: Distance = 60.0.inch; //TODO: Change this

        val angleToGoal: Angle = limelightMountAngle + ty

        val goalToLimelightInches = (goalHeightIn - limelightLensHeightIn) / Math.tan()
    }

}