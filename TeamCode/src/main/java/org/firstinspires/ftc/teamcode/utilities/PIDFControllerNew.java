package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDFControllerNew {
    private double Kv, Kp, Ki, Kd;
    private double error, integral;
    private double lastError = 0;
    private ElapsedTime timer;

    public PIDFControllerNew(double Kp, double Ki, double Kd, double Kv, ElapsedTime timer) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        this.Kv = Kv;
        this.timer = timer;
    }

    public void setPIDF(double Kp, double Ki, double Kd, double Kv) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        this.Kv = Kv;

    }

    public double calculate(double reference, double state) {
        double error = reference - state;
        integral += error * timer.seconds();
        double derivative = (error - lastError) / timer.seconds();
        lastError = error;

        timer.reset();

        double output = (error * Kp) + (derivative * Kd) + integral * Ki + Kv * reference;
        return output;
    }
}
