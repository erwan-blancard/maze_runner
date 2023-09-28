package maze_runner;

public class Maze {
	
	/*
	 * masks:
	 * 1 : LEFT		0b0001
	 * 2 : RIGHT	0b0010
	 * 4 : UP		0b0100
	 * 8 : DOWN		0b1000
	 */
	public static final String[] MAZE_TILE_PATTERNS = {
			"#########",	// 0 NONE
			
			"###..####",	// 1 LEFT
			"####..###",	// 2 RIGHT
			"###...###",
			"#.##.####",	// 4 UP
			"#.#..####",
			"#.##..###",
			"#.#...###",
			"####.##.#",	// 8 DOWN
			"###..##.#",
			"####..#.#",
			"###...#.#",
			"#.##.##.#",
			"#.#..##.#",
			"#.##..#.#",
			"#.#...#.#",
			
			"####.####"		// 16 NO_CONNECTIONS
	};

	private int[] startTilePos;
	private int[] endTilePos;
	private boolean[][] tiles;
	
	public Maze(int width, int height) {
		tiles = new boolean[width][height];
		
		startTilePos = new int[2];
		endTilePos = new int[2];
		
		startTilePos[0] = 0;
		startTilePos[1] = 0;
		
		endTilePos[0] = width-1;
		endTilePos[1] = height-1;
	}
	
	public void setTile(int x, int y, boolean b) {
		tiles[x][y] = b;
	}
	
	public boolean getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public int getWidth() {
		return tiles.length;
	}
	
	public int getHeight() {
		return tiles[0].length;
	}
	
	private boolean isPosValid(int x, int y) {
		return (x >= 0 && x < getWidth()) && (y >= 0 && y < getHeight());
	}
	
	public boolean isStartTile(int x, int y) {
		return x == startTilePos[0] && y == startTilePos[1];
	}
	
	public boolean isEndTile(int x, int y) {
		return x == endTilePos[0] && y == endTilePos[1];
	}
	
	public boolean isConnectedLeft(int x, int y) {
		return (isPosValid(x-1, y)) && (x-1 >= 0 && tiles[x-1][y]);
	}
	
	public boolean isConnectedRight(int x, int y) {
		return (isPosValid(x+1, y)) && (x+1 < getWidth() && tiles[x+1][y]);
	}
	
	public boolean isConnectedUp(int x, int y) {
		return isStartTile(x, y) || ((isPosValid(x, y-1)) && (y-1 >= 0 && tiles[x][y-1]));
	}
	
	public boolean isConnectedDown(int x, int y) {
		return isEndTile(x, y) || ((isPosValid(x, y+1)) && (y+1 < getHeight() && tiles[x][y+1]));
	}
	
	/*
	 * returns a value between 0 and 16, indicating the type of the tile.
	 * 
	 * masks:
	 * 1 : LEFT		0b0001
	 * 2 : RIGHT	0b0010
	 * 4 : UP		0b0100
	 * 8 : DOWN		0b1000
	 * 
	 * Examples:
	 * - LEFT+RIGHT = 0b0011
	 * - RIGHT+DOWN = 0b1010
	 */
	public byte getTileType(int x, int y) {
		final byte LEFT = 0b0001;
		final byte RIGHT = 0b0010;
		final byte UP = 0b0100;
		final byte DOWN = 0b1000;
		
		byte type = 0;
		
		if (isPosValid(x, y)) {
			if (isConnectedLeft(x, y)) { type += LEFT; }
			if (isConnectedRight(x, y)) { type += RIGHT; }
			if (isConnectedUp(x, y)) { type += UP; }
			if (isConnectedDown(x, y)) { type += DOWN; }
			
			// if the tile has no connections
			if (type == 0 && tiles[x][y]) { return (byte) (MAZE_TILE_PATTERNS.length-1); }
		}
		
		return type;
	}

}
