package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "limelightTest")
public class limelightTest extends OpMode{

    private Limelight3A limelight;
    private IMU imu;

    @Override
    public void init(){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(8); //11
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot revHubOrientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
        imu.initialize(new IMU.Parameters(revHubOrientationOnRobot));
    }

    @Override
    public void start(){
        limelight.start();
    }

    @Override
    public void loop(){
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight.updateRobotOrientation(orientation.getYaw());
        LLResult  llresult = limelight.getLatestResult();
        if (llresult != null && llresult.isValid()){
            Pose3D botpose = llresult.getBotpose(); //add MT2 to use the better one
            telemetry.addData("tx", llresult.getTx());
            telemetry.addData("ty", llresult.getTx());
            telemetry.addData("ta", llresult.getTa());
        }

    }
}
