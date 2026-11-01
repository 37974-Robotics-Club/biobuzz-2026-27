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

    public boolean isReady() { //allows opmode to check readiness of flywheel
        return isReady;
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
        double velocityError = Math.abs(targetVelocity - flywheel.getVelocity()); //previous code (flywheel.getVelocity() >= targetVelocity * 0.95) set state ready as long as min speed reached. that can cause uncorrected overshooting

        switch (state) {
            case IDLE:
                flywheel.setVelocity(0);
                isReady = false;
                break;

            case SPINNING:
                flywheel.setVelocity(targetVelocity);
                if (velocityError <= targetVelocity * 0.05) { //now waits until error is reasonabe
                    state = FlywheelState.READY; //auto transition
                    isReady = true;
                }
                break;

            case READY:
                flywheel.setVelocity(targetVelocity);
                if (velocityError > targetVelocity * 0.10) {
                    state = FlywheelState.SPINNING;
                    isReady = false;
                }
                break;
        }
    }
}