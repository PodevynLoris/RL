import javax.swing.*;
import java.util.Arrays;

public class DemoAgent_BACKUP {

    private static MountainCarEnv game;
    private static double[] gamestate;

    public static void main(String[] args) {
        game = new MountainCarEnv(MountainCarEnv.RENDER);
        // Running 100 episodes
        for (int i = 0; i < 10; i++) {
            gamestate = game.randomReset();
            System.out.println("The initial gamestate is: " + Arrays.toString(gamestate));
            while (gamestate[0] == 0) { // Game is not over yet
                double continuousPos = game.getPositionFromIndex((int) (gamestate[2])); // RE-TRANSCRIPTION
                double continuousVel = game.getVelocityFromIndex((int) (gamestate[3]));
                System.out.println("The car's position is " + game.getPosition());
                System.out.println("The car's velocity is " + game.getVelocity());
                System.out.println("AFTER REVERSE");
                System.out.println("The car's position is " + continuousPos);
                System.out.println("The car's velocity is " + continuousVel);
                if (continuousVel >= 0.0) {
                    System.out.println("I will try to go further forward");
                    gamestate = game.step(MountainCarEnv.FORWARD);
                } else if (continuousVel < 0.0) {
                    System.out.println("I will try to continue going backwards");
                    gamestate = game.step(MountainCarEnv.REVERSE);
                }
                System.out.println("The gamestate passed back to me was: " + Arrays.toString(gamestate));
                System.out.println("I received a reward of " + gamestate[1]);
                System.out.println();
            }
            System.out.println();
        }
        try {
            double[][] valuesToShow = new double[1000][1000];
            for (int i = 0; i < 1000; i++)
                for (int j = 0; j < 1000; j++)
                    valuesToShow[i][j] = Math.sin(0.00002 * i * j);
            HeatMapWindow hm = new HeatMapWindow(valuesToShow);
            hm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            hm.setSize(600, 600);
            hm.setVisible(true);
            hm.update(valuesToShow);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
