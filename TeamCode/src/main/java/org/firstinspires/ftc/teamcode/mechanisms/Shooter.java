package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

public class Shooter {
    private final DcMotorEx flywheel;
    private final Servo hood;
    private final CRServo turret;

    private final AnalogInput turretFeedback;

    private static final double HOOD_INCREMENT = 0.05;
    private static final long HOOD_INTERVAL_MS = 75;

    public enum FlywheelState {
        IDLE, SPINNING, READY
    }

    private FlywheelState state = FlywheelState.IDLE;

    private double targetHoodPosition = 0;
    private double targetVelocity = 0;

    private long hoodElapsedTime;

    public Shooter(DcMotorEx flywheel, Servo hood, CRServo turret, AnalogInput turretFeedback) {
        this.flywheel = flywheel;
        this.hood = hood;
        this.turret = turret;
        this.turretFeedback = turretFeedback;

        if (this.flywheel != null) {
            this.flywheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        if (this.hood != null) {
            //this.setHoodPosition(targetHoodPosition);
        }
    }

    public FlywheelState getState() {
        double currentVelocity = flywheel.getVelocity();
        double velocityError = Math.abs(targetVelocity - currentVelocity);

        if (currentVelocity == 0 && targetVelocity == 0) {
            setState(FlywheelState.IDLE);
            return state;
        }
        if (velocityError <= targetVelocity * 0.05) {
            state = FlywheelState.READY;
            return state;
        }
        if (velocityError > targetVelocity * 0.10) {
            state = FlywheelState.SPINNING;
            return state;
        }
        return state;
    }

    public boolean isReady() {
        return getState() == FlywheelState.READY;
    }

    public double getCurrentVelocity() {
        return flywheel.getVelocity();
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

    public void setState(FlywheelState state) {
        this.state = state;
    }

    public void setTargetVelocity(double velocity) {
        double newTarget = Math.max(0, velocity);

        if (newTarget == targetVelocity) return;

        targetVelocity = newTarget;
        flywheel.setVelocity(targetVelocity);
    }

    public void setHoodPosition(double position) {
        targetHoodPosition = Math.max(0.25, Math.min(0.97, position));
        if (hood != null) {
            hood.setPosition(targetHoodPosition);
        }
    }

    public void hoodUp() {
        if (System.currentTimeMillis() - hoodElapsedTime > HOOD_INTERVAL_MS) {
            setHoodPosition(targetHoodPosition + HOOD_INCREMENT);
            hoodElapsedTime = System.currentTimeMillis();
        }
    }

    public void hoodDown() {
        if (System.currentTimeMillis() - hoodElapsedTime > HOOD_INTERVAL_MS) {
            setHoodPosition(targetHoodPosition - HOOD_INCREMENT);
            hoodElapsedTime = System.currentTimeMillis();
        }
    }

    public void setTurretPower(double turretPower) {
        if (turret != null) {
            turret.setPower(turretPower);

        }
    }

    public double getHoodPosition() {
        return hood.getPosition();
    }
}