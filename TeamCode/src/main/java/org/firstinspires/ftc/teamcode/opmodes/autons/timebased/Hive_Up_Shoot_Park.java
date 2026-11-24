package org.firstinspires.ftc.teamcode.opmodes.autons.timebased;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Pusher;
import org.firstinspires.ftc.teamcode.mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.robot.Robot;

@Autonomous(group = "no pedro")
public class Hive_Up_Shoot_Park extends LinearOpMode {

    @Override
    public void runOpMode() {

        Robot robot = new Robot(hardwareMap);
        Intake intake = robot.intake;
        Shooter pollenShooter = robot.pollenShooter;
        Pusher pusher = robot.pusher;

        intake.setState(Intake.IntakeState.UP);


        waitForStart();

        if (isStopRequested()) {
            return;
        }

        intake.setState(Intake.IntakeState.DOWN_IDLE);
        pollenShooter.setHoodPosition(0.78);
        pollenShooter.setTargetVelocity(2480);
        sleep(900);
        pusher.setPusherPower(0.7);
        intake.setManualIntakePower(0.75);
        sleep(150);
        pusher.setPusherPower(0.7);
        sleep(500);
        pusher.setPusherPower(0.7);
        sleep(5000);
        robot.drivetrain.robotCentricDrive(0, 0, -0.75);
        sleep(200);
        robot.drivetrain.robotCentricDrive(0, 0.75, 0);
        intake.setManualIntakePower(0);
        pusher.setPusherPower(0.7);
        sleep(1500);
        robot.drivetrain.robotCentricDrive(0, 0, 0);
        pollenShooter.setTargetVelocity(0);
        sleep(500);
        robot.drivetrain.robotCentricDrive(0, 0, 0.75);
        sleep(400);
        robot.drivetrain.robotCentricDrive(0, 0, 0);
        sleep(500);
        robot.drivetrain.robotCentricDrive(0, 0.75, 0);
        sleep(500);
        robot.drivetrain.robotCentricDrive(0, 0, 0);





















    }
}