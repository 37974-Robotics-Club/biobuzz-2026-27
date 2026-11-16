package org.firstinspires.ftc.teamcode.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

//state machines are life.
public class Pusher {
    private DcMotorEx pusherMotor;

    public enum PusherState {
        IDLE,
        FEEDING,
        REVERSE
    }

    private PusherState currentState = PusherState.IDLE;

    public Pusher(DcMotorEx pusher) {
        this.pusherMotor = pusher;
        this.pusherMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public void update(boolean shooterReady, boolean driverClearance, boolean pusherDown) {
        if (shooterReady && driverClearance) {
            setState(PusherState.FEEDING);
        } else if (pusherDown) {
            setState(PusherState.REVERSE);
        }
        else {
            setState(PusherState.IDLE);
        }
    }

    public boolean isFeeding() {
        return currentState == PusherState.FEEDING;
    }

    public void setState (PusherState state) {
        if (currentState == state) return;
        currentState = state;
        switch (currentState) {
            case IDLE:
                pusherMotor.setPower(0);
                break;

            case FEEDING:
                pusherMotor.setPower(1);
                break;

            case REVERSE:
                pusherMotor.setPower(-1);
                break;
        }
    }

    public PusherState getState() {
        return currentState;
    }
}