package org.firstinspires.ftc.teamcode.robot;

import android.provider.Settings;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.ShooterAdjust;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.utilities.BasicFilter;
import org.firstinspires.ftc.teamcode.utilities.RunningAverageFilter;

public class RobotHardware {
    private static final RobotHardware instance = new RobotHardware();
    private static final double IDEAL_VOLTAGE = 12.5;
    public ShooterAdjust shooterAdjust;
    private double lastMeasuredVoltage;

    public static enum Alliance {
        BLUE,
        RED
    }

    public VoltageSensor batterySensor;
    private final BasicFilter batteryFilter = new RunningAverageFilter(5);
    public static Alliance alliance;
    private Limelight3A limelight;
    public MotorEx shooterMotor;
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
    public RobotHardware init(HardwareMap map, Telemetry telemetry, Pose pose) {
        //instantiate all hardware

        //shooter motor
        shooterMotor = new MotorEx(map, "motorS");
        intakeMotor = new MotorEx(map, "motorI");
        shooterMotor.setInverted(true);

        servoLeft = new ServoEx(map, "servoLeft");
        servoRight = new ServoEx(map, "servoRight");
        servoLeft.setInverted(true);

        batterySensor = map.getAll(VoltageSensor.class)
                .iterator()
                .next();

        servoTransferShooter = new ServoEx(map, "servoTransfer");
        servoTransferIntake = new CRServoEx(map, "servoTransfer2");
        servoTransferIntake.setInverted(true);


        limelight = map.get(Limelight3A.class, "limelight");


        flightRecorder = new JoinedTelemetry(PanelsTelemetry.INSTANCE.getFtcTelemetry(), telemetry);
        //TODO: Set Up Telemetry

        //create all subsystems
        intake = new Intake();
        shooter = new Shooter();
        shooter.setMode(Shooter.Mode.FIXED);
        shooterMotor.setRunMode(Motor.RunMode.RawPower);
        transfer = new Transfer();
        shooterAdjust = new ShooterAdjust();
        drive = new MecanumDrive(map, pose);
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
        // show battery
        lastMeasuredVoltage = batterySensor.getVoltage();
        flightRecorder.addData("BATTERY STATE", lastMeasuredVoltage);
        //run the scheduler
        CommandScheduler.getInstance().run();
        // update telemetry
        flightRecorder.update();
    }
}
