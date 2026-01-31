package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class DrivetrainSubsystem extends SubsystemBase {
    public final GoBildaPinpointDriver odo;
    public DcMotor frontLeft, frontRight, backLeft, backRight;
   public DrivetrainSubsystem(HardwareMap hardwareMap){
       //initialize all motors with hardware map
       frontLeft = hardwareMap.get(DcMotor.class, "motorFL");
       frontRight = hardwareMap.get(DcMotor.class, "motorFR");
       backLeft = hardwareMap.get(DcMotor.class, "motorBL");
       backRight = hardwareMap.get(DcMotor.class, "motorBR");

       odo = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

       //Reverse the left side motors
       frontLeft.setDirection(DcMotor.Direction.REVERSE);
       backLeft.setDirection(DcMotor.Direction.REVERSE);

       //set offsets
       odo.setOffsets(-8, 4, DistanceUnit.INCH); //these are tuned for 3110-0002-0001 Product Insight #1


       odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
       odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

       odo.resetPosAndIMU();

       odo.getHeading(AngleUnit.DEGREES);
   }

   public void drive(double forward, double strafe, double rotation){
       frontLeft.setPower(forward + strafe + rotation);
       frontRight.setPower(forward - strafe - rotation);
       backLeft.setPower(forward - strafe + rotation);
       backRight.setPower(forward + strafe - rotation);
   }


}
