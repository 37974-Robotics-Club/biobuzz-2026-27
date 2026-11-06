package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

public class Shooter_PServo {
    private final DcMotorEx flywheel;
    private final Servo hood;
    private final Servo turret;

    public enum FlywheelState {
        IDLE, SPINNING, READY
    }

    private FlywheelState state = FlywheelState.IDLE;

    private double targetHoodPosition = 0;
    private double targetTurretPosition = 0.5;
    private double targetVelocity = 0;

    private long hoodElapsedTime;
    private long turretElapsedTime;

    public Shooter_PServo(DcMotorEx flywheel, Servo hood, Servo turret) {
        this.flywheel = flywheel;
        this.hood = hood;
        this.turret = turret;

        if (this.flywheel != null) {
            this.flywheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        if (this.hood != null) {
            this.setHoodPosition(targetHoodPosition);
        }
        if (this.turret != null) {
            this.setTurretPosition(targetTurretPosition);
        }
    }

    public FlywheelState getState() {
        double velocityError = Math.abs(targetVelocity - flywheel.getVelocity());

        if (flywheel.getVelocity() == 0 && targetVelocity == 0) {
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
        this.targetVelocity = Math.max(0, velocity);
        flywheel.setVelocity(this.targetVelocity);
    }

    public void setHoodPosition(double position) {
        targetHoodPosition = Math.max(0.0, Math.min(1.0, position));
        if (hood != null) {
            hood.setPosition(targetHoodPosition);
        }
    }

    public void hoodUp() {
        if (System.currentTimeMillis() - hoodElapsedTime > 75) {
            setHoodPosition(targetHoodPosition + 0.1);
            hoodElapsedTime = System.currentTimeMillis();
        }
    }

    public void hoodDown() {
        if (System.currentTimeMillis() - hoodElapsedTime > 75) {
            setHoodPosition(targetHoodPosition - 0.1);
            hoodElapsedTime = System.currentTimeMillis();
        }
    }

    public void setTurretPosition(double position) {
        targetTurretPosition = Math.max(0.0, Math.min(1.0, position));
        if (turret != null) {
            turret.setPosition(targetTurretPosition);
        }
    }

    public void turretLeft() {
        setTurretPosition(targetTurretPosition - 0.01);
        turretElapsedTime = System.currentTimeMillis();
    }

    public void turretRight() {
        setTurretPosition(targetTurretPosition + 0.01);
        turretElapsedTime = System.currentTimeMillis();
    }
}