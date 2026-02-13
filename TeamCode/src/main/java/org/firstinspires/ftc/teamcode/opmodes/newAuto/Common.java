package org.firstinspires.ftc.teamcode.opmodes.newAuto;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;

public class Common {
    public static Pose AUTO_END_POSE = new Pose(0, 0, 0);
    public static TelemetryManager telemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    public static boolean isRed = false;
}
