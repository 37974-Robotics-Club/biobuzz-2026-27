package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.util.ElapsedTime;

public class SlewRateLimiter {
    private final double maxRate; // Units per second
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
        double dt = timer.seconds();
        timer.reset();

        // Guard against initial loop spikes or hangs
        if (dt > 0.2) dt = 0.02;

        double maxChange = maxRate * dt;
        double error = targetValue - lastValue;

        // Constrain the change to the maximum allowed step
        double step = Math.max(-maxChange, Math.min(maxChange, error));

        lastValue += step;
        return lastValue;
    }
}
