package org.firstinspires.ftc.teamcode.robot;

public class SlewRateLimiter {
    private final double maxRate;
    private double lastValue = 0.0;
    private long lastTime = 0L;

    public SlewRateLimiter(double maxRatePerSecond) {
        this.maxRate = maxRatePerSecond;
    }

    public void reset(double currentValue) {
        lastValue = currentValue;
        lastTime = System.nanoTime();
    }

    public double calculate(double targetValue) {
        long currentTime = System.nanoTime();

        if (lastTime == 0L) {
            lastTime = currentTime;
            lastValue = targetValue;
            return lastValue;
        }

        double elapsedTime = (currentTime - lastTime) / 1e9;
        lastTime = currentTime;

        if (elapsedTime > 0.25) {
            lastValue = targetValue;
            return lastValue;
        }

        double maxChange = maxRate * elapsedTime;
        double error = targetValue - lastValue;
        lastValue += Math.max(-maxChange, Math.min(maxChange, error));

        return lastValue;
    }
}
