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
    Limelight3A limelight;
    LLResult result;
    public LimelightSubsystem(HardwareMap hMap){
        limelight = hMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0);
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

    public Pose3D getBotpose_M2(){
        result = limelight.getLatestResult();
        Pose3D botpose_mt2 = result.getBotpose_MT2();
        return botpose_mt2;
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
