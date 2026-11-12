package org.firstinspires.ftc.teamcode.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

//state machines are life.
public class Intake {
    private DcMotorEx intakeMotor;
    private Servo intakeDrop;

    public double intakeDownPosition = 0;
    public double intakeUpPosition = 1;

    public enum IntakeState {
        UP,
        DOWN_IDLE,
        INTAKE,
        OUTTAKE
    }

    private IntakeState currentState = IntakeState.DOWN_IDLE;

    public Intake(DcMotorEx intake, Servo intakeDrop) {
        this.intakeMotor = intake;
        this.intakeDrop = intakeDrop;
        this.intakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public void setState (IntakeState state) {
        if (currentState == state) return;
        currentState = state;
        switch (currentState) {
            case UP:
                intakeMotor.setPower(0);
                intakeDrop.setPosition(intakeUpPosition);
                break;
            case DOWN_IDLE:
                intakeDrop.setPosition(intakeDownPosition);
                if (intakeDrop.getPosition() <= 0.25) {
                    intakeMotor.setPower(0);
                }
                break;

            case INTAKE:
                intakeDrop.setPosition(intakeDownPosition);
                if (intakeDrop.getPosition() <= 0.25) {
                    intakeMotor.setPower(1);
                }
                break;

            case OUTTAKE:
                intakeDrop.setPosition(intakeDownPosition);
                if (intakeDrop.getPosition() <= 0.25) {
                    intakeMotor.setPower(-1);
                }
                break;
        }
    }

    public void setIntakeDrop() {

    }

    public IntakeState getState() {
        return currentState;
    }
}
