package maze_runner.generator;

import maze_runner.Maze;

public class GraphBasedMazeGenerator implements MazeGenerator {
	
	private boolean usePerfectType;
	
	public GraphBasedMazeGenerator(boolean usePerfectType) {
		this.usePerfectType = usePerfectType;
	}

	@Override
	public Maze generate(int width, int height) {
		return null;
	}

}
