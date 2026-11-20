package org.firstinspires.ftc.teamcode.robot.controllers;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDF {

    private PIDFCoefficients pid;
    private double lastError = 0.0;
    private double integral = 0.0;
    private double maxIntegral = Double.MAX_VALUE;

    private final ElapsedTime timer = new ElapsedTime();

    public PIDF(double kP, double kI, double kD, double kF) {
        pid = new PIDFCoefficients(kP, kI, kD, kF);
    }

    public double calculate(double position, double target) {
        double error = target - position;
        return calculate(error);
    }

    public double calculate(double error) {
        double dt = timer.seconds();

        // Avoid division by zero on the first calculation.
        if (dt <= 0.0) {
            dt = 0.001;
        }

        double derivative = (error - lastError) / dt;
        integral += error * dt;

        // Integral clamping
        integral = Math.max(-maxIntegral, Math.min(maxIntegral, integral));

        double output =
                (pid.p * error) +
                        (pid.i * integral) +
                        (pid.d * derivative) +
                        pid.f;

        lastError = error;
        timer.reset();

        return output;
    }

    public void setMaxI(double maxI) {
        maxIntegral = Math.abs(maxI);
    }

    public void setP(double p) {
        pid.p = p;
    }

    public void setI(double i) {
        pid.i = i;
    }

    public void setD(double d) {
        pid.d = d;
    }

    public void setF(double f) {
        pid.f = f;
    }

    public void reset() {
        lastError = 0.0;
        integral = 0.0;
        timer.reset();
    }
}
