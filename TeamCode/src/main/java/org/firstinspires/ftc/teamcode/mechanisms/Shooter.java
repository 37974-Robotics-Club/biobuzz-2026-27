package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

//state machines are life.
public class Shooter {
    private DcMotorEx flywheel;
    private Servo hood;
    private CRServo turret;

    private double targetVelocity;
    private double currentVelocity;

    private boolean isReady = false;

    public enum FlywheelState {
        IDLE,
        SPINNING,
        READY
    }

    private FlywheelState currentState = FlywheelState.IDLE;

    public Shooter(DcMotorEx flywheel, Servo hood, CRServo turret) {
        this.flywheel = flywheel;
        this.hood = hood;
        this.turret = turret;
        this.flywheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public void setState(FlywheelState state) {
        currentState = state;
    }

    public FlywheelState getState() {
        return currentState;
    }

    public void setTargetVelocity(double targetVelocity) {
        this.targetVelocity = targetVelocity;
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

    public double getCurrentVelocity() {
        if (flywheel != null) {
            this.currentVelocity = flywheel.getVelocity();
        }
        return currentVelocity;
    }

    public void setHoodPosition(double hoodPosition) {
        if (hood != null) {
            hood.setPosition(hoodPosition);
        }
    }

    public void setTurretPower(double turretPower) {
        if (turret != null) {
            turret.setPower(turretPower);
        }
    }

    public void update() { //incomplete update method
        switch (currentState) {
            case IDLE:
                flywheel.setPower(0);
                break;

            case SPINNING:
                flywheel.setVelocity(targetVelocity);
                break;

            case READY:
                flywheel.setVelocity(targetVelocity);
                if (currentVelocity >= targetVelocity * 0.95) {
                    isReady = true;
                }
                break;

        }
    }
}
