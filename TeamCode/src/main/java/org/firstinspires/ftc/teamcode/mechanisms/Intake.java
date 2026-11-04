package org.firstinspires.ftc.teamcode.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotorEx;

//state machines are life.
public class Intake {
    private DcMotorEx intakeMotor;

    public enum IntakeState {
        IDLE,
        INTAKE,
        OUTTAKE
    }

    private IntakeState currentState = IntakeState.IDLE;

    public Intake(DcMotorEx intake) {
        this.intakeMotor = intake;
        this.intakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public void setState (IntakeState state) {
        if (currentState == state) return;
        currentState = state;
        switch (currentState) {
            case IDLE:
                intakeMotor.setPower(0);
                break;

            case INTAKE:
                intakeMotor.setPower(1);
                break;

            case OUTTAKE:
                intakeMotor.setPower(-1);
                break;
        }
    }

    public IntakeState getState() {
        return currentState;
    }
}
