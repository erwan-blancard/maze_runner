package maze_runner.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import maze_runner.Maze;
import maze_runner.MazeTile;

public class GraphBasedMazeGenerator implements MazeGenerator {
	
	private boolean usePerfectType;
	
	public GraphBasedMazeGenerator(boolean usePerfectType) {
		this.usePerfectType = usePerfectType;
	}

	@Override
	public Maze generate(int width, int height) {
		Maze maze = new Maze(width, height);
		
		List<GraphNode> graphNodes = new ArrayList<GraphNode>((width+height)/2);
		List<Pos> availableNodes = new ArrayList<Pos>();
		// adds all possible tiles as Pos to availableNodes
		for(int y = 0; y < height; y++) { for (int x = 0; x < width; x++) { availableNodes.add(new Pos(x, y)); } }
		
		Random r = new Random();
		
		for(int i = 0; i < (width+height)/2; i++) {
			int randIndex = r.nextInt(availableNodes.size());
			graphNodes.add(new GraphNode(availableNodes.get(randIndex)));
			availableNodes.remove(randIndex);
		}
		final int firstNode = r.nextInt(graphNodes.size());
		int currentNode = firstNode;
		int targetNode = r.nextInt(graphNodes.stream()
				.filter((gn) -> !gn.equals(graphNodes.get(firstNode)))
				.map((gn) -> gn)
				.collect(Collectors.toList()).size());
		
		byte prevDirection = 0;
		Pos pos = new Pos(graphNodes.get(currentNode).getPos().getX(), graphNodes.get(currentNode).getPos().getY());
		
		// connect all graph nodes
		while (allNodesLinked(graphNodes)) {
			GraphNode target = graphNodes.get(targetNode);
			
			byte targetDirection = MazeTile.getValueOfTileType(target.getPos().getX() < pos.getX(),
					target.getPos().getX() > pos.getX(),
					target.getPos().getY() < pos.getY(),
					target.getPos().getY() > pos.getY());
			
			// if on target tile somehow
			if (targetDirection == 0) {
				graphNodes.get(currentNode).setLinked();
				List<GraphNode> remainingNodes = getRemainingGraphNodes(graphNodes);
				if (remainingNodes.size() == 0) {
					break;
				}
				final int compareNode = targetNode;
				pos = new Pos(graphNodes.get(compareNode).getPos().getX(), graphNodes.get(compareNode).getPos().getY());
				currentNode = targetNode;
				targetNode = r.nextInt(remainingNodes.stream()
						.filter((gn) -> !gn.equals(remainingNodes.get(compareNode)))
						.map((gn) -> gn)
						.collect(Collectors.toList()).size());
				continue;
			}
			
			final double LOW_CHANCE = 0.5;
			
			double leftChance = 0.0;
			double rightChance = 0.0;
			double upChance = 0.0;
			double downChance = 0.0;
			
			if (prevDirection != MazeTile.getValueOfTileType(true, false, false, false) && maze.isPosValid(pos.getX()-1, pos.getY()) && MazeTile.hasLeftMask(targetDirection)) {
				leftChance = target.getPos().getX() < pos.getX() ? 1.0 : LOW_CHANCE;
			}
			if (prevDirection != MazeTile.getValueOfTileType(false, true, false, false) && maze.isPosValid(pos.getX()+1, pos.getY()) && MazeTile.hasRightMask(targetDirection)) {
				rightChance = target.getPos().getX() > pos.getX() ? 1.0 : LOW_CHANCE;
			}
			if (prevDirection != MazeTile.getValueOfTileType(false, false, true, false) && maze.isPosValid(pos.getX(), pos.getY()-1) && MazeTile.hasUpMask(targetDirection)) {
				upChance = target.getPos().getY() < pos.getY() ? 1.0 : LOW_CHANCE;
			}
			if (prevDirection != MazeTile.getValueOfTileType(false, false, false, true) && maze.isPosValid(pos.getX(), pos.getY()+1) && MazeTile.hasDownMask(targetDirection)) {
				downChance = target.getPos().getY() > pos.getY() ? 1.0 : LOW_CHANCE;
			}
			
			// choose a random direction
			double leftRandValue = r.nextDouble() * leftChance;
			double rightRandValue = r.nextDouble() * rightChance;
			double upRandValue = r.nextDouble() * upChance;
			double downRandValue = r.nextDouble() * downChance;
			
			double[] chanceList = {leftChance, rightChance, upChance, downChance};
			
			double chanceValue = Arrays.stream(chanceList).max().getAsDouble();
			
			if (chanceValue == leftChance) {
				
			} else if (chanceValue == rightChance) {
				
			} else if (chanceValue == upChance) {
				
			} else if (chanceValue == downChance) {
				
			} else {
				System.out.println("Invalid direction");
				return maze;
			}
		}
		
		// fill the rest of the maze
		while (true) {
			break;
		}
		
		return maze;
	}
	
	private List<GraphNode> getRemainingGraphNodes(List<GraphNode> graphNodes) {
		return graphNodes.stream()
			.filter((gn) -> !gn.isLinked())
			.map((gn) -> gn)
			.collect(Collectors.toList());
	}
	
	private boolean allNodesLinked(List<GraphNode> graphNodes) {
		return getRemainingGraphNodes(graphNodes).size() != 0;
	}
	
	private boolean isGraphNodeTile(Pos pos, List<GraphNode> graphNodes) {
		for (int i = 0; i < graphNodes.size(); i++) {
			if (graphNodes.get(i).getPos().equals(pos)) {
				return true;
			}
		}
		return false;
	}
	
	private class GraphNode {
		private Pos pos;
		// if linked to another GraphNode
		private boolean linked;
		
		public GraphNode(Pos pos) {
			this.pos = pos;
		}
		
		public Pos getPos() { return pos; }
		public boolean isLinked() { return linked; }
		public void setLinked() { linked = true; }
	}
	
	private class Pos {
		
		private int x;
		private int y;
		
		public Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() { return x; }
		public int getY() { return y; }
		
		@Override
		public boolean equals(Object obj) {
			return obj instanceof Pos && x == ((Pos) obj).getX() && y == ((Pos) obj).getY();
		}
	}

}
