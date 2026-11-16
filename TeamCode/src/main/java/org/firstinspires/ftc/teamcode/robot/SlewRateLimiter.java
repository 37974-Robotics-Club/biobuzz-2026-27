package org.firstinspires.ftc.teamcode.robot;

public class SlewRateLimiter {
    private final double maxRate;
    private double lastValue = 0.0;
    private long lastTime;

    public SlewRateLimiter(double maxRatePerSecond) {
        this.maxRate = maxRatePerSecond;
        reset();
    }

    public void reset() {
        lastValue = 0.0;
        lastTime = System.nanoTime();
    }

    public double calculate(double targetValue) {
        long currentTime = System.nanoTime();
        double elapsedTime = (currentTime - lastTime) / 1e9;
        lastTime = currentTime;

        elapsedTime = Math.min(elapsedTime, 0.05);

        double maxChange = maxRate * elapsedTime;
        double error = targetValue - lastValue;

        lastValue += Math.max(-maxChange, Math.min(maxChange, error));

        return lastValue;
    }
}
