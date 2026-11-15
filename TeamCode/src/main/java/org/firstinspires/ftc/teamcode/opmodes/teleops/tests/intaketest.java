package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;

@TeleOp(group = "tests")
public class intaketest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Intake intake = new Intake(hardwareMap.get(DcMotorEx.class, "intake"), hardwareMap.get(Servo.class, "intakeDrop"));

        waitForStart();

        if (isStopRequested()) return;

        intake.setState(Intake.IntakeState.UP);

        while (opModeIsActive()) {
            if (gamepad1.circle) {
                intake.setState(Intake.IntakeState.INTAKE);

            } else if (gamepad1.square) {
                intake.setState(Intake.IntakeState.OUTTAKE);
            } else {
                intake.setState(Intake.IntakeState.DOWN_IDLE);
            }
            telemetry.addData("State", intake.getState());
            telemetry.addData("Gamepad 1 square", gamepad1.square);
            telemetry.addData("Gamepad 1 circle", gamepad1.circle);
            telemetry.update();

        }
    }
}