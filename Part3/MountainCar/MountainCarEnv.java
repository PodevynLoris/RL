import java.util.ArrayList;

public class MountainCarEnv {

	public static final double MIN_POS = -1.2;
	public static final double MAX_POS = 0.6;
	public static final double MAX_SPEED = 0.07;
	public static final double GOAL_POS = 0.5;
	public static final int NOTHING = 0;
	public static final int FORWARD = 1;
	public static final int REVERSE = -1;
	private static final double FORCE_INFLUENCE = 0.0005;
	private static final double GRAVITY = -0.0025;

	private double position;
	private double velocity;
	public static final int TEXT = 1;
	public static final int RENDER = 2;
	public static final int NONE = 0;
	private static int vizType = NONE;
	private static int vizDelay = 10;
	// NEWW
	public static final int numberOfPositionBins = 6;
	public static final int numberOfVelocityBins = 100;
	public static final int numberOfActions = 3;
	// NEWW
	// public static final double positionBinSize = (MAX_POS - MIN_POS) /
	// numberOfPositionBins;
	// public static final double velocityBinSize = (MAX_SPEED * 2) /
	// numberOfVelocityBins;
	private double[][] V;
	private int[][] policy;

	public MountainCarEnv() {
		randomReset();
	}

	public MountainCarEnv(int vizType) {
		MountainCarEnv.vizType = vizType;
	}

	public MountainCarEnv(int vizType, int vizDelay) {
		MountainCarEnv.vizType = vizType;
		MountainCarEnv.vizDelay = vizDelay;
	}

	public void initialize_V_and_policy() {
	}

	// public void initialize_V_and_policy() {
	// int goalPositionIndex = getGoalPositionIndex();
	// V = new double[numberOfPositionBins][numberOfVelocityBins];
	// policy = new int[numberOfPositionBins][numberOfVelocityBins];

	// for (int i = 0; i < numberOfPositionBins; i++) {
	// for (int j = 0; j < numberOfVelocityBins; j++) {
	// if (i >= goalPositionIndex) {
	// V[i][j] = 0; // Set value to 0 for terminal states
	// policy[i][j] = NOTHING; // No action needed for terminal states
	// } else {
	// V[i][j] = Math.random(); // Initialize with a random value for non-terminal
	// states
	// policy[i][j] = FORWARD; // Default policy, can be randomized or set to a
	// specific strategy
	// }
	// }
	// }
	// }

	// public int selectActionBasedOnPolicy() {
	// int posIndex = getDiscretePositionIndex();
	// int velIndex = getDiscreteVelocityIndex();
	// // Ensure indices are within bounds before accessing the array
	// if (posIndex < 0 || posIndex >= numberOfPositionBins || velIndex < 0 ||
	// velIndex >= numberOfVelocityBins) {
	// return NOTHING; // Default action if out of bounds
	// }
	// return policy[posIndex][velIndex];
	// }

	// NEWW
	// public int getDiscretePositionIndex() {
	// return (int) ((position - MIN_POS) / positionBinSize);
	// }

	// // NEWW
	// public int getDiscreteVelocityIndex() {
	// return (int) ((velocity + MAX_SPEED) / velocityBinSize);
	// }

	// // NEWW
	// public int getGoalPositionIndex() {
	// return (int) ((GOAL_POS - MIN_POS) / positionBinSize);
	// }

	// public int[] getDiscretizedState() {
	// double[] continuousState = getState(); // Retrieve the continuous state
	// int posIndex = (int)((continuousState[2] - MIN_POS) / positionBinSize);
	// int velIndex = (int)((continuousState[3] + MAX_SPEED) / velocityBinSize);

	// // Ensure the indices are within the bounds of the discretized grid
	// posIndex = Math.max(0, Math.min(numberOfPositionBins - 1, posIndex));
	// velIndex = Math.max(0, Math.min(numberOfVelocityBins - 1, velIndex));

	// return new int[] {gameState[0],gameState[1],posIndex, velIndex};
	// }

	public double[] step(int force) {
		if (!(force == REVERSE || force == NOTHING || force == FORWARD))
			System.out.println("Please pick only -1 (Reverse), 0 (Nothing) or 1 (Forward) as actions.");
		velocity += force * FORCE_INFLUENCE + Math.cos(3 * position) * (GRAVITY);
		velocity = Math.min(MAX_SPEED, Math.max(-MAX_SPEED, velocity));
		position += velocity;
		position = Math.min(MAX_POS, Math.max(MIN_POS, position));
		if (position == MIN_POS && velocity < 0)
			velocity = 0;
		double[] state = getDiscreteState();
		if (vizType == TEXT)
			printState(state);
		else if (vizType == RENDER)
			renderState(state);
		return state;
	}

	public double[] undo(int force) {
		if (!(force == REVERSE || force == NOTHING || force == FORWARD))
			System.out.println("Please pick only -1 (Reverse), 0 (Nothing) or 1 (Forward) as actions.");
		velocity -= force * FORCE_INFLUENCE + Math.cos(3 * position) * (GRAVITY);
		velocity = Math.min(MAX_SPEED, Math.max(-MAX_SPEED, velocity));
		position -= velocity;
		position = Math.min(MAX_POS, Math.max(MIN_POS, position));
		if (position == MIN_POS && velocity < 0)
			velocity = 0;
		double[] state = getDiscreteState();
		if (vizType == TEXT)
			printState(state);
		else if (vizType == RENDER)
			renderState(state);
		return state;
	}

	public double[] setState(double position, double velocity) {
		this.position = Math.min(MAX_POS, Math.max(MIN_POS, position));
		this.velocity = Math.min(MAX_SPEED, Math.max(-MAX_SPEED, velocity));
		return getDiscreteState();
	}

	// public double[] getState() {
	// double[] state = new double[4];
	// if (position > GOAL_POS)
	// state[0] = 1;
	// else
	// state[0] = 0;
	// state[1] = getReward();
	// state[2] = position;
	// state[3] = velocity;
	// return state;
	// }
	int positionIndex;
	int velocityIndex;

	public double[] getDiscreteState() {
		double[] state = new double[4];

		double position_intervals_sizes = (MAX_POS - MIN_POS) / numberOfPositionBins;
		this.positionIndex = (int) ((position - MIN_POS) / position_intervals_sizes);

		double velocity_intervals_sizes = (MAX_SPEED * 2) / numberOfVelocityBins;
		this.velocityIndex = (int) ((velocity + MAX_SPEED) / velocity_intervals_sizes);

		if (getPositionFromIndex(this.positionIndex) > GOAL_POS)
			state[0] = 1;
		else
			state[0] = 0;
		state[1] = getReward();
		state[2] = positionIndex;
		state[3] = velocityIndex;
		return state;
	}

	public double getPositionFromIndex(int positionIndex) {
		double positionStep = (MAX_POS - MIN_POS) / numberOfPositionBins;
		// Getting the midpoint of the interval for the given index
		return MIN_POS + positionStep * positionIndex + positionStep / 2;
	}

	public double getVelocityFromIndex(int velocityIndex) {
		double velocityStep = (2 * MAX_SPEED) / numberOfVelocityBins;
		// Getting the midpoint of the interval for the given index
		return -MAX_SPEED + velocityStep * velocityIndex + velocityStep / 2;
	}

	public double getReward() {
		if (getPositionFromIndex(this.positionIndex) > GOAL_POS)
			return 0.0;
		// Possible negative reward for hitting back wall
		// else if (position==MIN_POS)
		// return -50;
		else
			return -1.0;
	}

	public double getPosition() {
		return position;
	}

	public double getVelocity() {
		return velocity;
	}

	public double[] randomReset() {
		this.position = -0.6 + Math.random() * 0.2;
		this.velocity = 0.0;
		return getDiscreteState();
	}

	public double[] reset() {
		this.position = -0.6;
		this.velocity = 0.0;
		return getDiscreteState();
	}

	public static void printState(double[] state) {
		if (state[0] == 1)
			System.out.println("The car has escaped");
		else
			System.out.println("The episode is still going");
		System.out.println("The reward earned this step was " + state[1]);
		System.out.println("Car Position: " + state[2] +
				" Car Velocity: " + state[3]);
	}

	public static void renderState(double[] state) {
		if (!visibleState) {
			panel = new MountainCarPanel();
			visibleState = true;
		}
		panel.render(state);
		try {
			Thread.sleep(vizDelay);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private static boolean visibleState = false;
	private static MountainCarPanel panel;

}
