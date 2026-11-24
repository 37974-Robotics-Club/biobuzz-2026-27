package org.firstinspires.ftc.teamcode.opmodes.autons.timebased;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.robot.Robot;

@Autonomous(group = "no pedro")
public class Hive_Up_Park extends LinearOpMode {

    @Override
    public void runOpMode() {

        Robot robot = new Robot(hardwareMap);
        Intake intake = robot.intake;

        intake.setState(Intake.IntakeState.UP);


        waitForStart();

        if (isStopRequested()) {
            return;
        }

        intake.setState(Intake.IntakeState.DOWN_IDLE);
        robot.drivetrain.robotCentricDrive(0, 0.75, 0);
        sleep(1500);
        robot.drivetrain.robotCentricDrive(0, 0, 0);
        sleep(500);
        robot.drivetrain.robotCentricDrive(0, 0, 0.75);
        sleep(350);
        robot.drivetrain.robotCentricDrive(0, 0, 0);
        sleep(500);
        robot.drivetrain.robotCentricDrive(0, 0.75, 0);
        sleep(300);
        robot.drivetrain.robotCentricDrive(0, 0, 0);
    }
}