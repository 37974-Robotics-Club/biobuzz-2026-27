package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

public class Shooter {
    private DcMotorEx flywheel;
    private Servo hood;
    private CRServo turret;
    private AnalogInput turretFeedback;

    private static final double HOOD_INCREMENT = 0.05;
    private static final long HOOD_INTERVAL_MS = 75;

    private static final double TURRET_MAX_VOLTAGE = 3.3;
    private static final double TURRET_MIN_VOLTAGE = 0.0;

    public enum FlywheelState {
        IDLE, SPINNING, READY, REVERSE
    }

    private FlywheelState state = FlywheelState.IDLE;

    private double targetHoodPosition = 0;

    private double targetVelocity = 0;
    public static double kP = 20.0;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 11.8;

    private long hoodElapsedTime;

    public Shooter(DcMotorEx flywheel, Servo hood, CRServo turret, AnalogInput turretFeedback) {
        this(flywheel, hood, turret, turretFeedback, kP, kI, kD, kF);
    }

    public Shooter(DcMotorEx flywheel, Servo hood, CRServo turret, AnalogInput turretFeedback,
                   double kP, double kI, double kD, double kF) {
        this.flywheel = flywheel;
        this.hood = hood;
        this.turret = turret;
        this.turretFeedback = turretFeedback;

        this.flywheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        if (kP != 0 || kI != 0 || kD != 0 || kF != 0) {
            setPIDFCoefficients(kP, kI, kD, kF);
        }
        this.setHoodPosition(targetHoodPosition);
    }

    public void setPIDFCoefficients(double kP, double kI, double kD, double kF) {
        this.flywheel.setPIDFCoefficients(
                DcMotorEx.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(kP, kI, kD, kF)
        );
    }

    public FlywheelState getState() {
        double currentVelocity = flywheel.getVelocity();
        double velocityError = Math.abs(targetVelocity - currentVelocity);

        if (currentVelocity == 0 && targetVelocity == 0) {
            setState(FlywheelState.IDLE);
            return state;
        }
        if (velocityError <= targetVelocity * 0.10) {
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
        hood.setPosition(targetHoodPosition);
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
        if (turret == null) {
            return;
        }

        if (turretFeedback != null) {
            double currentVoltage = turretFeedback.getVoltage();
            if (currentVoltage <= TURRET_MIN_VOLTAGE && turretPower < 0) {
                turret.setPower(0.0);
                return;
            }
            if (currentVoltage >= TURRET_MAX_VOLTAGE && turretPower > 0) {
                turret.setPower(0.0);
                return;
            }
        }

        turret.setPower(turretPower);
    }

    public double getTurretVoltage() {
        if (turretFeedback != null) {
            return turretFeedback.getVoltage();
        }
        return 0.0;
    }

    public double getHoodPosition() {
        return hood.getPosition();
    }
}