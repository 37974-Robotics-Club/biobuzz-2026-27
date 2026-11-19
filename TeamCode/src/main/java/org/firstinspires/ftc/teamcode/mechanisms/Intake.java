package org.firstinspires.ftc.teamcode.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

//state machines are life.
public class Intake {

    private DcMotorEx intakeMotor;
    private Servo intakeDrop;

    public double intakeDownPosition = 1;
    public double intakeUpPosition = 0;

    public double intakeTargetPower = 0;

    public enum IntakeState {
        UP,
        DOWN_IDLE,
        INTAKE,
        OUTTAKE
    }

    private IntakeState currentState = IntakeState.UP;

    public Intake(DcMotorEx intake, Servo intakeDrop) {
        this.intakeMotor = intake;
        this.intakeDrop = intakeDrop;
        this.intakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        currentState = IntakeState.UP;
        intakeDrop.setPosition(intakeUpPosition);
    }

    public void setState (IntakeState state) {
        if (currentState == state) return;
        currentState = state;
        switch (currentState) {
            case UP:
                intakeTargetPower = 0;
                intakeDrop.setPosition(intakeUpPosition);
                break;

            case DOWN_IDLE:
                intakeDrop.setPosition(intakeDownPosition);
                if (intakeDrop.getPosition() == intakeDownPosition) {
                    intakeTargetPower = 0;
                }
                break;

            case INTAKE:
                intakeDrop.setPosition(intakeDownPosition);
                if (intakeDrop.getPosition() == intakeDownPosition) {
                    intakeTargetPower = 0.7;
                }
                break;

            case OUTTAKE:
                intakeDrop.setPosition(intakeDownPosition);
                if (intakeDrop.getPosition() == intakeDownPosition) {
                    intakeTargetPower = -0.75;
                }
                break;
        }
    }

    public void update() {
        if (currentState == IntakeState.UP) {
            intakeMotor.setPower(0);
            return;
        }

        intakeMotor.setPower(intakeTargetPower);
    }


    public IntakeState getState() {
        return currentState;
    }
}
