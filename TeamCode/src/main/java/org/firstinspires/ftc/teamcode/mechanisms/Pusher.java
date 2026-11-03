package org.firstinspires.ftc.teamcode.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

//state machines are life.
public class Pusher {
    private DcMotorEx pusherMotor;

    public enum PusherState {
        IDLE,
        LAUNCH,
        PULL
    }

    private PusherState currentState = PusherState.IDLE;

    public Pusher(DcMotorEx pusher) {
        this.pusherMotor = pusher;
        this.pusherMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setState (PusherState state) {
        currentState = state;
        switch (currentState) {
            case IDLE:
                pusherMotor.setPower(0);
                break;

            case LAUNCH:
                pusherMotor.setPower(1);
                break;

            case PULL:
                pusherMotor.setPower(-1);
                break;
        }
    }

    public PusherState getState() {
        return currentState;
    }
}