package org.firstinspires.ftc.teamcode.opmodes.newAuto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class PathsBlue {

    private final Follower f;

    public PathChain shootPreload;

    public PathChain intake3;
    public PathChain shoot3;

    public PathChain intake6;
    public PathChain shoot6;

    public PathChain unloadRamp;

    public PathChain intake9;
    public PathChain shoot9;

    public PathChain intakeHuman;

    public PathChain shootHuman;


    public PathChain intakeExtra1;
    public PathChain shootExtra1;
    public PathChain intakeExtra2;
    public PathChain shootExtra2;
    public PathChain intakeExtra3;
    public PathChain shootExtra3;

    public PathChain leave;

    // =============================
    // POSES (ALL CENTRALIZED)
    // =============================

    public static Pose P_START = new Pose(144 - 112.094, 138.091, Math.toRadians(180));
    public static Pose P_SHOOT = new Pose(144 - 89.000, 102.000);

    // Intake 3
    public static Pose P_I3_CP = new Pose(144 - 79.512, 85.6);
    public static Pose P_I3_END = new Pose(144 - 95.550, 85.6);
    public static Pose P_I3_WALL = new Pose(144 - 123, 85.6);

    // Intake 6
    public static Pose P_I6_CP = new Pose(144 - 73.435, 62.5);
    public static Pose P_I6_END = new Pose(144 - 96.563, 62.5);
    public static Pose P_I6_WALL = new Pose(144 - 129, 62.5);

    // Ramp
    public static Pose P_RAMP_CP = new Pose(144 - 92.680, 75.123);
    public static Pose P_RAMP_END = new Pose(144 - 127.8, 75);

    // Intake 9
    public static Pose P_I9_CP = new Pose(144 - 90, 39);
    public static Pose P_I9_END = new Pose(144 - 99.264, 39);
    public static Pose P_I9_WALL = new Pose(144 - 137, 39);
    public static Pose P_I9_RETURN = new Pose(144 - 99.433, 39);

    // Extra balls
    public static Pose P_HP_CP1 = new Pose(144 - 111.920, 56.783);
    public static Pose P_HP_END = new Pose(144 - 137.788, 49.329);
    public static Pose P_HP_WALL = new Pose(144 - 137.807, 6);
    public static Pose P_HP_RETURN = new Pose(144 - 137.638, 49.348);

    public static Pose P_EX_CP1 = new Pose(144 - 93.862, 66.851);
    public static Pose P_EX_END1 = new Pose(144 - 133.027, 60.774);

    public static Pose P_EX_CP2 = new Pose(144 - 93.862, 66.682);
    public static Pose P_EX_END2 = new Pose(144 - 133.027, 60.774);

    public static Pose P_EX_CP3 = new Pose(144 - 94.368, 66.513);
    public static Pose P_EX_END3 = new Pose(144 - 133.027, 60.605);

    // Leave
    public static Pose P_LEAVE_END = new Pose(144 - 89.135, 80);
    public static double H_38 = Math.toRadians(180 - 38);
    public static double H_0 = Math.toRadians(180);
    public static double H_43 = Math.toRadians(180 - 43);
    public static double H_270 = Math.toRadians(270);

    public PathsBlue(Follower follower) {
        f = follower;
    }

    public double mirrorAngleRad(double angle) {
        return Math.PI - angle;
    }

    public static boolean isPathRed = true;

    public void mirrorAll() {
        P_START = mirrorPose(P_START);
        P_SHOOT = mirrorPose(P_SHOOT);

        P_I3_CP = mirrorPose(P_I3_CP);
        P_I3_END = mirrorPose(P_I3_END);
        P_I3_WALL = mirrorPose(P_I3_WALL);

        P_I6_CP = mirrorPose(P_I6_CP);
        P_I6_END = mirrorPose(P_I6_END);
        P_I6_WALL = mirrorPose(P_I6_WALL);

        P_RAMP_CP = mirrorPose(P_RAMP_CP);
        P_RAMP_END = mirrorPose(P_RAMP_END);

        P_I9_CP = mirrorPose(P_I9_CP);
        P_I9_END = mirrorPose(P_I9_END);
        P_I9_WALL = mirrorPose(P_I9_WALL);
        P_I9_RETURN = mirrorPose(P_I9_RETURN);

        P_HP_CP1 = mirrorPose(P_HP_CP1);
        P_HP_END = mirrorPose(P_HP_END);
        P_HP_WALL = mirrorPose(P_HP_WALL);
        P_HP_RETURN = mirrorPose(P_HP_RETURN);

        P_EX_CP1 = mirrorPose(P_EX_CP1);
        P_EX_END1 = mirrorPose(P_EX_END1);

        P_EX_CP2 = mirrorPose(P_EX_CP2);
        P_EX_END2 = mirrorPose(P_EX_END2);

        P_EX_CP3 = mirrorPose(P_EX_CP3);
        P_EX_END3 = mirrorPose(P_EX_END3);

        P_LEAVE_END = mirrorPose(P_LEAVE_END);

        H_0 = mirrorAngleRad(H_0);
        H_38 = mirrorAngleRad(H_38);
        H_43 = mirrorAngleRad(H_43);
        H_270 = mirrorAngleRad(H_270);
    }

    public Pose mirrorPose(Pose pose) {
        return new Pose(144 - 144 - pose.getX(), pose.getY());
    }

    // =============================
    // BUILD ALL PATHS
    // =============================

    public void goalRocketBuild() {
        shootPreload = f.pathBuilder()
                .addPath(new BezierLine(P_START, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();
        intake6 = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_I6_CP, P_I6_END))
                .setLinearHeadingInterpolation(H_38, H_0)
                .addPath(new BezierLine(P_I6_END, P_I6_WALL))
                .setTangentHeadingInterpolation()
                .addPath(new BezierLine(P_I6_WALL, P_I6_END))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();

        shoot6 = f.pathBuilder()
                .addPath(new BezierCurve(P_I6_END, P_I6_CP, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();
        // ---------- Ramp ----------
        unloadRamp = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_RAMP_CP, P_RAMP_END))
                .setLinearHeadingInterpolation(H_38, H_0)
                .build();
        intake3 = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_I3_CP, P_I3_END))
                .setLinearHeadingInterpolation(H_38, H_0)
                .addPath(new BezierLine(P_I3_END, P_I3_WALL))
                .setConstantHeadingInterpolation(H_0)
                .addPath(new BezierLine(P_I3_WALL, P_I3_END))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();

        shoot3 = f.pathBuilder()
                .addPath(new BezierCurve(P_I3_END, P_I3_CP, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();
    }

    public void goal3Build() {
        shootPreload = f.pathBuilder()
                .addPath(new BezierLine(P_START, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();

        leave = f.pathBuilder()
                .addPath(new BezierLine(P_SHOOT, P_LEAVE_END))
                .setConstantHeadingInterpolation(H_38)
                .build();
    }

    public void goal6Build() {
        shootPreload = f.pathBuilder()
                .addPath(new BezierLine(P_START, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();

        // ---------- Intake + Shoot 3 ----------
        intake3 = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_I3_CP, P_I3_END))
                .setLinearHeadingInterpolation(H_38, H_0)
                .addPath(new BezierLine(P_I3_END, P_I3_WALL))
                .setConstantHeadingInterpolation(H_0)
                .addPath(new BezierLine(P_I3_WALL, P_I3_END))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();

        shoot3 = f.pathBuilder()
                .addPath(new BezierCurve(P_I3_END, P_I3_CP, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();

        leave = f.pathBuilder()
                .addPath(new BezierLine(P_SHOOT, P_LEAVE_END))
                .setConstantHeadingInterpolation(H_38)
                .build();
    }

    public void goal9Build() {

        // ---------- Preload ----------
        shootPreload = f.pathBuilder()
                .addPath(new BezierLine(P_START, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();

        // ---------- Intake + Shoot 3 ----------
        intake3 = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_I3_CP, P_I3_END))
                .setLinearHeadingInterpolation(H_38, H_0)
                .addPath(new BezierLine(P_I3_END, P_I3_WALL))
                .setConstantHeadingInterpolation(H_0)
                .addPath(new BezierLine(P_I3_WALL, P_I3_END))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();

        shoot3 = f.pathBuilder()
                .addPath(new BezierCurve(P_I3_END, P_I3_CP, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();

        // ---------- Intake + Shoot 6 ----------
        intake6 = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_I6_CP, P_I6_END))
                .setLinearHeadingInterpolation(H_38, H_0)
                .addPath(new BezierLine(P_I6_END, P_I6_WALL))
                .setTangentHeadingInterpolation()
                .addPath(new BezierLine(P_I6_WALL, P_I6_END))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();

        shoot6 = f.pathBuilder()
                .addPath(new BezierCurve(P_I6_END, P_I6_CP, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();

        // ---------- Ramp ----------
        unloadRamp = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_RAMP_CP, P_RAMP_END))
                .setLinearHeadingInterpolation(H_38, H_0)
                .build();
    }

    public void goal21Build() {

        // ---------- Preload ----------
        shootPreload = f.pathBuilder()
                .addPath(new BezierLine(P_START, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();

        // ---------- Intake + Shoot 3 ----------
        intake3 = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_I3_CP, P_I3_END))
                .setLinearHeadingInterpolation(H_38, H_0)
                .addPath(new BezierLine(P_I3_END, P_I3_WALL))
                .setConstantHeadingInterpolation(H_0)
                .addPath(new BezierLine(P_I3_WALL, P_I3_END))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();

        shoot3 = f.pathBuilder()
                .addPath(new BezierCurve(P_I3_END, P_I3_CP, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();

        // ---------- Intake + Shoot 6 ----------
        intake6 = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_I6_CP, P_I6_END))
                .setLinearHeadingInterpolation(H_38, H_0)
                .addPath(new BezierLine(P_I6_END, P_I6_WALL))
                .setTangentHeadingInterpolation()
                .addPath(new BezierLine(P_I6_WALL, P_I6_END))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();

        shoot6 = f.pathBuilder()
                .addPath(new BezierCurve(P_I6_END, P_I6_CP, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();

        // ---------- Ramp ----------
//        unloadRamp = f.pathBuilder()
//                .addPath(new BezierCurve(P_SHOOT, P_RAMP_CP, P_RAMP_END))
//                .setLinearHeadingInterpolation(H_38, H_0)
//                .build();

        // ---------- Intake + Shoot 9 ----------
        intake9 = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_I9_CP, P_I9_END))
                .setConstantHeadingInterpolation(H_0)
                .addPath(new BezierLine(P_I9_END, P_I9_WALL))
                .setTangentHeadingInterpolation()
                .addPath(new BezierLine(P_I9_WALL, P_I9_RETURN))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();

        shoot9 = f.pathBuilder()
                .addPath(new BezierCurve(P_I9_RETURN, P_I9_CP, P_SHOOT))
                .setLinearHeadingInterpolation(H_0, H_38)
                .build();

        intakeHuman = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_HP_CP1, P_HP_END))
                .setLinearHeadingInterpolation(H_38, H_270)
                .addPath(new BezierLine(P_HP_END, P_HP_WALL))
                .setTangentHeadingInterpolation()
                .addPath(new BezierLine(P_HP_WALL, P_HP_RETURN))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build();

        // ---------- Shoot Human ----------
        shootHuman = f.pathBuilder()
                .addPath(new BezierCurve(P_HP_RETURN, P_HP_CP1, P_SHOOT))
                .setLinearHeadingInterpolation(H_270, H_38)
                .build();

        // ---------- Extra Balls ----------
        intakeExtra1 = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_EX_CP1, P_EX_END1))
                .setLinearHeadingInterpolation(H_38, H_43)
                .build();

        shootExtra1 = f.pathBuilder()
                .addPath(new BezierCurve(P_EX_END1, P_EX_CP1, P_SHOOT))
                .setLinearHeadingInterpolation(H_43, H_38)
                .build();

        intakeExtra2 = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_EX_CP2, P_EX_END2))
                .setLinearHeadingInterpolation(H_38, H_43)
                .build();

        shootExtra2 = f.pathBuilder()
                .addPath(new BezierCurve(P_EX_END2, P_EX_CP2, P_SHOOT))
                .setLinearHeadingInterpolation(H_43, H_38)
                .build();

        intakeExtra3 = f.pathBuilder()
                .addPath(new BezierCurve(P_SHOOT, P_EX_CP3, P_EX_END3))
                .setLinearHeadingInterpolation(H_38, H_43)
                .build();

        shootExtra3 = f.pathBuilder()
                .addPath(new BezierLine(P_EX_END3, P_SHOOT))
                .setLinearHeadingInterpolation(H_43, H_38)
                .build();

        // ---------- Leave ----------
        leave = f.pathBuilder()
                .addPath(new BezierLine(P_SHOOT, P_LEAVE_END))
                .setConstantHeadingInterpolation(H_38)
                .build();
    }
}
