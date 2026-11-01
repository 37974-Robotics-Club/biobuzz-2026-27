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

    private double targetHoodPosition;
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

    public FlywheelState getState() { //allows opmode to check readiness of flywheel
        double velocityError = Math.abs(targetVelocity - flywheel.getVelocity()); //previous code (flywheel.getVelocity() >= targetVelocity * 0.95) set state ready as long as min speed reached. that can cause uncorrected overshooting

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

    public double getCurrentVelocity() {
        return flywheel.getVelocity();
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

    public void setState(FlywheelState state) {
        this.state = state;
        //should only set value (state)
        //update should be running constntly regardlss of ths
    }

    public void setTargetVelocity(double velocity) {
        this.targetVelocity = Math.max(0, velocity);
        //selects whichever is larger, 0 or velocity
        //if distance alculation (future) gives neg val,
        //prevent flywheel from reversing
        flywheel.setVelocity(this.targetVelocity);
    }

    public void setHoodPosition(double position) {
        targetHoodPosition = Math.max(0.0, Math.min(1.0, position));
        if (hood != null) {
            hood.setPosition(targetHoodPosition);
        }
    }

    public void setTurretPower(double turretPower) {
        if (turret != null) {
            turret.setPower(turretPower);

        }
    }
}