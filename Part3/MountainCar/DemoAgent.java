import javax.swing.*;
import java.util.Arrays;

public class DemoAgent {

    private static MountainCarEnv game;
    private static double[] gamestate;

    public static void main(String[] args) {
        game = new MountainCarEnv(MountainCarEnv.RENDER);
        // Initialize V and policy arrays
        game.initialize_V_and_policy();

        // Running 100 episodes
        for (int i = 0; i < 10; i++) {
            gamestate = game.randomReset();
            System.out.println("The initial gamestate is: " + Arrays.toString(gamestate));
            while (gamestate[0] == 0) { // Game is not over yet
                int action = game.selectActionBasedOnPolicy(); // Use the policy to decide the action
                gamestate = game.step(action);

                System.out.println("The car's position is " + gamestate[2]);
                System.out.println("The car's velocity is " + gamestate[3]);
                System.out.println("Action taken: " + (action == MountainCarEnv.FORWARD ? "FORWARD"
                        : (action == MountainCarEnv.REVERSE ? "REVERSE" : "NOTHING")));
                System.out.println("The gamestate passed back to me was: " + Arrays.toString(gamestate));
                System.out.println("I received a reward of " + gamestate[1]);
            }
            System.out.println();
        }
        // Visualization part remains the same
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
