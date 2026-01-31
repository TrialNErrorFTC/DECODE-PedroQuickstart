package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import java.util.List;

public class LimelightSubsystem extends SubsystemBase {
    public Limelight3A limelight;
    public LLResult result;
    public LimelightSubsystem(HardwareMap hMap){
        limelight = hMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(1);
        limelight.start();
    }

    public boolean hasValidTarget(){
        result = limelight.getLatestResult();
        return result != null && result.isValid();
    }
    public Position getDistanceFromAprilTag(){
        result = limelight.getLatestResult();
        List<LLResultTypes.FiducialResult> fiducialResult = result.getFiducialResults();
        int id = fiducialResult.get(0).getFiducialId();
        Position position = fiducialResult.get(0).getRobotPoseTargetSpace().getPosition();
        return position;
    }

    public Pose3D getBotpose(){
        result = limelight.getLatestResult();
        Pose3D botpose = result.getBotpose();
        return botpose;
    }

    public double getTx(){
        result = limelight.getLatestResult();
        return result.getTx();
    }
    public double getTy(){
        result = limelight.getLatestResult();
        return result.getTy();
    }

}
