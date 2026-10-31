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
    }

    public void setState(FlywheelState state) {
        this.state = state;
    }

    public void setTargetVelocity(double velocity) {
        this.targetVelocity = velocity;
    }

    public void setHoodPosition(double pos) {
        if (hood != null) {
            hood.setPosition(Math.max(0.0, Math.min(1.0, pos)));
        }
    }

    public void setTurretPower(double power) {
        if (turret != null) {
            turret.setPower(power);
        }
    }

    public void update() {
        if (flywheel == null) return;

        switch (state) {
            case IDLE:
                flywheel.setPower(0);
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