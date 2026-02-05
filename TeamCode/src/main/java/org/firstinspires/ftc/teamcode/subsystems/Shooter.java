package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.util.InterpLUT;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.utilities.PIDFController;
import org.firstinspires.ftc.teamcode.utilities.RunningAverageFilter;

@Configurable
public class Shooter extends SubsystemBase {
    public static double kS = 0.0;

    public enum Mode {
        RAW,
        FIXED,
        DYNAMIC
    }

    public static boolean tuning = true;
    public static Mode mode = Mode.RAW;
    private final RunningAverageFilter velFilter = new RunningAverageFilter(5);

    public static double targetVelocityTicks = 0.0;
    public static double targetRawPower = 0.0;

    public static double IDLE_VELOCITY = 0;
    public static double kV = 0.47;
    public static double kP = 0.06;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double VELOCITY_TOLERANCE = 40.0;
    public static double CLOSE_ZONE_OFFSET = 0.0;
    public static double FAR_ZONE_OFFSET = 0.0;


    private PIDFController flywheelVelocityPID = new PIDFController(kP, kI, kD, 0);

    private double getKD() {
        return kD;
    }

    private double getKI() {
        return kI;
    }

    public double getKP() {
        return kP;
    }

    RobotHardware robot = RobotHardware.get();

    // initialize this thing to persist as is

    //TODO: Create a LUT for RPM
//    public static final InterpLUT distToVeloLUT;
//    static {
//        distToVeloLUT = new InterpLUT();
//
//        distToVeloLUT.add(-100, -980 - CLOSE_ZONE_OFFSET);
//        distToVeloLUT.add(4.6, -980 - CLOSE_ZONE_OFFSET);
//        distToVeloLUT.add(5, -1030 - CLOSE_ZONE_OFFSET);
//        distToVeloLUT.add(5.7, -1055 - CLOSE_ZONE_OFFSET);
//        distToVeloLUT.add(6, -1070 - CLOSE_ZONE_OFFSET);
//        distToVeloLUT.add(6.5, -1090 - CLOSE_ZONE_OFFSET);
//        distToVeloLUT.add(7.15, -1090 - CLOSE_ZONE_OFFSET);
//        distToVeloLUT.add(7.7, -1110 - CLOSE_ZONE_OFFSET);
//        distToVeloLUT.add(8.4, -1190 - CLOSE_ZONE_OFFSET);
//        distToVeloLUT.add(9.4, -1215 - CLOSE_ZONE_OFFSET);
//        // ======
//        distToVeloLUT.add(11.6, -1385 - FAR_ZONE_OFFSET);
//        distToVeloLUT.add(12, -1440 - FAR_ZONE_OFFSET);
//        distToVeloLUT.add(12.5, -1500 - FAR_ZONE_OFFSET);
//        distToVeloLUT.add(12.7, -1520 - FAR_ZONE_OFFSET);
//        distToVeloLUT.add(13.07, -1540 - FAR_ZONE_OFFSET);
//        // to do: add
//        distToVeloLUT.createLUT();
//    }

    public Shooter() {
    }

    @Override
    public void periodic() {
        switch (mode) {
            case RAW:
                rawMode();
                break;
            case FIXED:
                velocityMode();
                break;
            case DYNAMIC:
//                dynamicMode();
                break;
            default:
                break;
        }

        TelemetryManager p = PanelsTelemetry.INSTANCE.getTelemetry();
        p.addData("Target Velocity", targetVelocityTicks);
        p.addData("Current Velocity", velFilter.getFilteredOutput());
        p.update();

        velFilter.updateValue(robot.shooterMotor.getVelocity());

        logData();
    }

    public void setMode(Mode mode) {
        Shooter.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

//    public double getOptimalVelocityForDist(double distance_ft) {
//        return distToVeloLUT.get(distance_ft);
//    }

    public void setVelocity(double velo) {
        mode = Mode.FIXED;
        targetVelocityTicks = velo;
    }

    public void setPower(double pow) {
        mode = Mode.RAW;
        targetRawPower = pow;
    }

    public void setIdle() {
        setVelocity(IDLE_VELOCITY);
    }

    private void velocityMode() {
        robot.shooterMotor.setPower(
                flywheelVelocityPID.calculate(targetVelocityTicks - robot.shooterMotor.getVelocity())
        );
    }

    private double getKS() {
        return kS;
    }

    private double getKV() {
        return kV;
    }


//    private void dynamicMode() {
//        double currentVelocity = velFilter.getFilteredOutput();
//        // clip to a distance
//        double distanceToGoal = Range.clip(robot.drive.getAimTarget().distance, 1.0, 12.0);
//        double optimalVelocityForDist = getOptimalVelocityForDist(distanceToGoal);
//        double output = flywheelVelocityPID.calculate(currentVelocity, optimalVelocityForDist) * robot.getVoltageFeedforwardConstant()
//                + kV * optimalVelocityForDist * robot.getVoltageFeedforwardConstant();
//
//        robot.shooterMotor.set(output);
//    }

    private void rawMode() {
        robot.shooterMotor.setPower(targetRawPower);
    }

    private void logData() {
        robot.flightRecorder.addLine("========SHOOTER========");
        robot.flightRecorder.addData("Flywheel Target velocity", targetVelocityTicks);
        robot.flightRecorder.addData("Flywheel Current velocity", robot.shooterMotor.getVelocity());
        robot.flightRecorder.addData("Flywheel raw power output", robot.shooterMotor.getPower());
    }

//    public boolean isAtTargetVelocity() {
//        return flywheelVelocityPID.atSetPoint();
//    }

}
