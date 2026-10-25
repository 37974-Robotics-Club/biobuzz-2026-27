package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

//state machines are life.
public class Shooter {
    private DcMotorEx flywheel;
    private Servo hood;
    private CRServo turret;

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

    public void update() {
        switch (currentState) {
            case IDLE:
                flywheel.setPower(0);
                break;

            case SPINNING:
                flywheel.setPower(1);
                break;

            case READY:
                flywheel.setPower(1);
                break;

        }
    }
}
