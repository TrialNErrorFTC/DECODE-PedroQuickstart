package org.firstinspires.ftc.teamcode.testing;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Configurable
public class tuningLimelight extends LinearOpMode {

    Limelight3A limelight;
    DcMotor frontLeft, frontRight, backLeft, backRight;
    MotorGroup left;
    MotorGroup right;

    double limelight_aim_proportional(double getTX) {
        double kP = .035;
        return kP * getTX * -1.0;
    }

    void init(HardwareMap aHardwareMap){
        //setup motors
        MotorGroup left = new MotorGroup(aHardwareMap.get(DcMotor.class, "frontLeft"),aHardwareMap.get(DcMotor.class, "backLeft"));
        MotorGroup right = new MotorGroup(aHardwareMap.get(DcMotor.class, "frontRight"),aHardwareMap.get(DcMotor.class, "frontRight"));

        //setup limelight
        limelight = aHardwareMap.get(Limelight3A.class, "limelight");
    }

    class MotorGroup{
        public DcMotor front;
        public DcMotor back;
        public MotorGroup(DcMotor front, DcMotor back) {
            back.setDirection(DcMotorSimple.Direction.REVERSE);

            //set to brake on zero power
            front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            //set to encoders off
            front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        }
        public void adjustPower(double adjust){
            //get currentpower

            //add currentpower to motors
        }
    }
    @Override
    public void runOpMode() throws InterruptedException {
        //initialize limelight and motors
        init(hardwareMap);
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)

        //start limelight
        limelight.start();


        //pipeline management
        limelight.pipelineSwitch(1);


        waitForStart();

        while (opModeIsActive()) {
            //get results
            LLResult result = limelight.getLatestResult();
            validateLLResult(result);

            //set power to motors
            adjustPowers();
            adjustPowers();

        }

    }

    private void validateLLResult(LLResult result) {
        if (result != null && result.isValid()) {
            //left or right(degrees)
            double tx = result.getTx();
            //up or down(degrees)
            double ty = result.getTy();
            //area of the target (how big it is)
            double ta = result.getTa();

            telemetry.addData("Target X", tx);
            telemetry.addData("Target Y", ty);
            telemetry.addData("Target Area", ta);

        } else {
            telemetry.addData("Limelight", "No Targets");
        }
    }
}
