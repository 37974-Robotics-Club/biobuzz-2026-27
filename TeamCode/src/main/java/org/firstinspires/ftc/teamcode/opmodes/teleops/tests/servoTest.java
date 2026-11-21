
package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.mechanisms.Shooter;

@TeleOp(group = "tests")
public class servoTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {


        CRServo turret = hardwareMap.get(CRServo.class, "pollenTurret");



        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.circle) {
                turret.setPower(1);
            } else {
                turret.setPower(0);
            }


            telemetry.update();
        }
    }
}
