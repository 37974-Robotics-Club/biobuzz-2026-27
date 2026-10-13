package org.firstinspires.ftc.teamcode.opmodes.autons.tests;

import com.pedropathing.follower.Follower;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous(name = "Test Path")
public class TestPath extends OpMode {

    private Follower follower;

    private final PoseFactory p = PoseFactory.degrees();

    private final Pose start = p.of(56, 8, 90);
    private final Pose point1 = p.of(56.1218, 23.0625, 270);
    private final Pose point2 = p.of(73.7721, 23.4587, -178.7138);
    private final Pose point3 = p.of(32.4823, 23.945, 48.3694);
    private final Pose point3Control1 = p.of(55.8684, 50.4941, 0);
    private final Pose park = p.of(55.4666, 29.8487, 0);

    private Path path1() {
        return line(start, point1).linear(start, point1);
    }

    private Path path2() {
        return line(point1, point2).linear(point1, point2);
    }

    private Path path3() {
        return curve(point2, point3Control1, point3)
                .linear(point2, point3);
    }

    private Path path4() {
        return line(point3, park).linear(point3, park);
    }

    private Command routine() {
        return sequential(
                follow(follower, path1()),
                follow(follower, path2()),
                follow(follower, path3()),
                follow(follower, path4())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();
    }

    @Override
    public void start() {
        schedule(routine());
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.update();
    }
}