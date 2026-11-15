package org.firstinspires.ftc.teamcode.mechanisms;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.SlewRateLimiter;

//state machines are life.
public class Intake {
    private  final SlewRateLimiter intakeLimiter = new SlewRateLimiter(1.5);

    private DcMotorEx intakeMotor;
    private Servo intakeDrop;

    public double intakeDownPosition = 0;
    public double intakeUpPosition = 1;

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
                if (intakeDrop.getPosition() <= 0.25) {
                    intakeTargetPower = 0;
                }
                break;

            case INTAKE:
                intakeDrop.setPosition(intakeDownPosition);
                if (intakeDrop.getPosition() <= 0.25) {
                    intakeTargetPower = 0.9;
                }
                break;

            case OUTTAKE:
                intakeDrop.setPosition(intakeDownPosition);
                if (intakeDrop.getPosition() <= 0.25) {
                    intakeTargetPower = -0.9;
                }
                break;
        }
    }

    public void update() {
        double smoothedPower = intakeLimiter.calculate(intakeTargetPower);

        if (currentState == IntakeState.UP) {
            smoothedPower = 0;
            intakeLimiter.reset();
        }
        intakeMotor.setPower(smoothedPower);
    }

    public void setIntakeDrop() {

    }

    public IntakeState getState() {
        return currentState;
    }
}
