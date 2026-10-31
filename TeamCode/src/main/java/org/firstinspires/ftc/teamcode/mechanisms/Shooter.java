package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

public class Shooter {
    private final DcMotorEx flywheel;
    private final Servo hood;
    private final CRServo turret;

    public enum FlywheelState {
        IDLE, SPINNING, READY
    }

    private FlywheelState state = FlywheelState.IDLE;

    private double targetVelocity;
    private boolean isReady;

    public Shooter(DcMotorEx flywheel, Servo hood, CRServo turret) {
        this.flywheel = flywheel;
        this.hood = hood;
        this.turret = turret;

        if (this.flywheel != null) {
            this.flywheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        if (this.hood != null) {
            this.setHoodPosition(0); //start at zero
        }
    }

    public void setState(FlywheelState state) {
        this.state = state;
        this.update();
    }

    public void setTargetVelocity(double velocity) {
        this.targetVelocity = velocity;
        this.update();
    }

    public void setHoodPosition(double position) {
        if (hood != null) {
            hood.setPosition(Math.max(0.0, Math.min(1.0, position)));
        }
    }

    public void setTurretPower(double turretPower) {
        if (turret != null) {
            turret.setPower(turretPower);

        }
    }

    public void update() {
        if (flywheel == null) return;

        switch (state) {
            case IDLE:
                flywheel.setVelocity(0);
                isReady = false;
                break;

            case SPINNING:
                flywheel.setVelocity(targetVelocity);
                isReady = false;
                break;

            case READY:
                flywheel.setVelocity(targetVelocity);
                isReady = flywheel.getVelocity() >= targetVelocity * 0.95;
                break;
        }
    }
}