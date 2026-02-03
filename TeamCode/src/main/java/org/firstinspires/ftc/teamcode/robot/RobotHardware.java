package org.firstinspires.ftc.teamcode.robot;

import android.provider.Settings;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.ShooterAdjust;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

public class RobotHardware {
    private static final RobotHardware instance = new RobotHardware();
    public ShooterAdjust shooterAdjust;

    public static enum Alliance {
        BLUE,
        RED
    }

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
    private Shooter shooter;
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

        servoTransferShooter = new ServoEx(map, "servoTransfer");
        servoTransferIntake = new CRServoEx(map, "servoTransfer2");
        servoTransferIntake.setInverted(true);


        limelight = map.get(Limelight3A.class, "limelight");


        flightRecorder = new JoinedTelemetry(PanelsTelemetry.INSTANCE.getFtcTelemetry(), telemetry);
        //TODO: Set Up Telemetry

        //create all subsystems
        intake = new Intake();
        shooter = new Shooter();
        transfer = new Transfer();
        shooterAdjust = new ShooterAdjust();
        drive = new MecanumDrive(map, pose);
        return this;
    }

    /**
     * Returns Voltage Compensation Ratio to multiply with (prolly not needed)
     *
     **/
    public double getVoltageFeedforwardConstant() {
        return 0;
    }


    /**
     * Main Loop Code for Running OpModes
    * */
    public void endLoop() {
        //show alliance
        flightRecorder.addData("ALLIANCE", alliance.toString());
        // show battery
        //run the scheduler
        CommandScheduler.getInstance().run();
        // update telemetry
        flightRecorder.update();
    }
}
