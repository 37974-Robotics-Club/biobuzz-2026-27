package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.util.ElapsedTime;

public class SlewRateLimiter {
    private final double maxRate;
    private double lastValue = 0.0;
    private final ElapsedTime timer = new ElapsedTime();

    public SlewRateLimiter(double maxRatePerSecond) {
        this.maxRate = maxRatePerSecond;
    }

    public void reset() {
        lastValue = 0.0;
        timer.reset();
    }

    public double calculate(double targetValue) {
        double elapsedTimeSeconds = timer.seconds();
        timer.reset();

        if (elapsedTimeSeconds > 0.2) {
            elapsedTimeSeconds = 0.02;
        }

        double maxChange = maxRate * elapsedTimeSeconds;
        double error = targetValue - lastValue;

        double step = Math.max(-maxChange, Math.min(maxChange, error));

        lastValue += step;
        return lastValue;
    }
}
