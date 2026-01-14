package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.Position;

import java.util.List;

public class LimelightSubsystem extends SubsystemBase {
    Limelight3A limelight;
    LLResult result;
    public LimelightSubsystem(HardwareMap hMap){
        limelight = hMap.get(Limelight3A.class, "limelight");
    }

    public boolean hasValidTarget(){
        result = limelight.getLatestResult();
        return result != null && result.isValid();
    }
    public double getDistanceFromAprilTag(){
        List<LLResultTypes.FiducialResult> fiducialResult = result.getFiducialResults();
        int id = fiducialResult.get(0).getFiducialId();
        Position position = fiducialResult.get(0).getRobotPoseTargetSpace().getPosition();
        return Math.sqrt(Math.pow(position.x, 2) + Math.pow(position.y, 2) + Math.pow(position.z, 2));
    }

    public double getTx(){
        return result.getTx();
    }
    public double getTy(){
        return result.getTy();
    }
}
