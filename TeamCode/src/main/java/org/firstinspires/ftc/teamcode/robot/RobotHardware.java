package org.firstinspires.ftc.teamcode.robot;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.LimelightPoseEstimator;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.MecanumTeleDrive;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.ShooterAdjust;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.utilities.BasicFilter;
import org.firstinspires.ftc.teamcode.utilities.RunningAverageFilter;

import java.util.List;

public class RobotHardware {
    private static final RobotHardware instance = new RobotHardware();
    private static final double IDEAL_VOLTAGE = 12.5;
    public ShooterAdjust shooterAdjust;
    public ElapsedTime timer;
    private double lastMeasuredVoltage;
    public LimelightPoseEstimator limelightPoseEstimator;
    public DcMotorEx shooterMotor2;

    public static enum Alliance {
        BLUE,
        RED
    }

    public static enum Mode {
        TELEOP,
        AUTO
    }

    public VoltageSensor batterySensor;
    private final BasicFilter batteryFilter = new RunningAverageFilter(5);
    public static Alliance alliance;
    public Limelight3A limelight;
    public DcMotorEx shooterMotor;
    public MotorEx intakeMotor;
    public ServoEx servoLeft;
    public ServoEx servoRight;
    public ServoEx servoTransferShooter;
    public CRServoEx servoTransferIntake;
    public JoinedTelemetry flightRecorder;
    public Intake intake;
    public Shooter shooter;
    public Transfer transfer;
    public MecanumDrive drive;

    public MecanumTeleDrive teleDrive;
    public Mode mode;

    public RobotHardware() {
    }


    /**
     * Return instance of RobotHardware
     *
     * @return RobotHardware
     */
    public static RobotHardware get() {
        return instance;
    }

    public void reset() {
    }

    /**
     * Instantiates all hardware and creates all subsystems
     *
     * @param map       hardwareMap to initialize with
     * @param telemetry telemetry to use
     * @return RobotHardware
     **/
    public RobotHardware init(HardwareMap map, Mode mode, Telemetry telemetry, Pose pose) {
        CommandScheduler.getInstance().reset();
        //instantiate all hardware
        this.mode = mode;

        //shooter motor
        shooterMotor = map.get(DcMotorEx.class, "motorS");
        shooterMotor2 = map.get(DcMotorEx.class, "motorS2");

        intakeMotor = new MotorEx(map, "motorI");
        intakeMotor.setInverted(true);
        shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        servoLeft = new ServoEx(map, "servoLeft");
        servoRight = new ServoEx(map, "servoRight");
        servoLeft.setInverted(true);

        batterySensor = map.getAll(VoltageSensor.class)
                .iterator()
                .next();

        servoTransferShooter = new ServoEx(map, "servoTransfer");
        servoTransferIntake = new CRServoEx(map, "servoTransfer2");
        servoTransferIntake.setInverted(true);
        List<LynxModule> allHubs = map.getAll(LynxModule.class);

        limelight = map.get(Limelight3A.class, "limelight");

        timer = new ElapsedTime();

        flightRecorder = new JoinedTelemetry(PanelsTelemetry.INSTANCE.getFtcTelemetry(), telemetry);
        //TODO: Set Up Telemetry

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        //create all subsystems
        intake = new Intake();
        shooter = new Shooter();
        transfer = new Transfer();
        shooterAdjust = new ShooterAdjust();
        limelightPoseEstimator = new LimelightPoseEstimator();

        if (mode == Mode.AUTO) {
            drive = new MecanumDrive(map, pose);
        } else if (mode == Mode.TELEOP) {
            teleDrive = new MecanumTeleDrive(map);
//            drive = new MecanumDrive(map, pose);
        }
        flightRecorder.addLine("======DRIVETRAIN:=======");
        return this;
    }

    /**
     * Returns Voltage Compensation Ratio to multiply with (prolly not needed)
     **/
    public double getVoltageFeedforwardConstant() {
        // 11.0 is just we don't place unnecessary strain if somehow the battery drops to something like 4v.
        double safeVoltage = Math.max(lastMeasuredVoltage, 9.0);
        batteryFilter.updateValue(IDEAL_VOLTAGE / safeVoltage);
        return batteryFilter.getFilteredOutput();
    }


    /**
     * Main Loop Code for Running OpModes
     *
     */
    public void endLoop() {
        //show alliance
        flightRecorder.addData("ALLIANCE", alliance.toString());

        if (limelightPoseEstimator.isValidTarget()) {
            flightRecorder.addLine("======LIMELIGHT:=======");
            flightRecorder.addData("TX:", limelightPoseEstimator.getTx());
            flightRecorder.addData("TY:", limelightPoseEstimator.getTy());
            flightRecorder.addData("Distance From Goal:", limelightPoseEstimator.distanceToGoal());
        }
//        if (mode == Mode.AUTO) {
//            flightRecorder.addLine("======DRIVETRAIN:=======");
//            flightRecorder.addData("goal heading", drive.lastAimTarget.heading);
//            flightRecorder.addData("goal distance", drive.lastAimTarget.distance);
//            flightRecorder.addData("goal x", drive.aimAtPose.getX());
//            flightRecorder.addData("goal y", drive.aimAtPose.getY());
//
//            flightRecorder.addData("X:", drive.lastPose.getX());
//            flightRecorder.addData("Y:", drive.lastPose.getY());
//            flightRecorder.addData("Heading", Math.toDegrees(drive.lastPose.getHeading()));
//
//            flightRecorder.addData("Heading Error", Math.toDegrees(drive.getHeadingError()));
//        }

        flightRecorder.addLine("========SHOOTER========");
        flightRecorder.addData("Flywheel Target velocity", shooter.getTargetVelocity());
        flightRecorder.addData("Flywheel Current velocity", shooterMotor.getVelocity() * 60 / 28);
        flightRecorder.addData("Flywheel raw power output", shooterMotor.getPower());
        // show battery
        lastMeasuredVoltage = batterySensor.getVoltage();
        flightRecorder.addData("BATTERY STATE", lastMeasuredVoltage);
        //run the scheduler
        CommandScheduler.getInstance().run();

        // update telemetry
        flightRecorder.update();
    }
}
