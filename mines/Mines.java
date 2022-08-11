package mines;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

//This is a class designed to run the game itself.
public class Mines {
	private int numMines, height, width;
	private int board[][];
	private Random rand = new Random();
	private boolean showAll;
	private Set<Location> flag = new TreeSet<>();
	private Set<Location> discover = new TreeSet<>();
	private int[][] offsetXY = { { -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 }, { -1, -1 }, { 1, 1 }, { -1, 1 }, { 1, -1 } };

	private class Location implements Comparable<Location> {
		private int i;
		private int j;

		public Location(int i, int j) {
			this.i = i;
			this.j = j;
		}

		@Override
		public String toString() {
			return "(" + i + ", " + j + ")";
		}

		@Override
		public int compareTo(Location other) {
			return this.toString().compareTo(other.toString());
		}

	}

	// constructor to boot a game in the width and height of the data, in which
	// exactly numMines mines are placed at random.
	public Mines(int height, int width, int numMines) {
		int i, j;
		this.height = height;
		this.width = width;
		this.numMines = numMines;
		board = new int[height][width];

		for (int n = 0; n < numMines;) {
			// get random indexes
			i = rand.nextInt(height);
			j = rand.nextInt(width);
			// add mines to the board
			if (addMine(i, j))
				n++;
		}

	}

	// returns true if (i,j) is in bounds of the field
	public boolean isInBounds(int x, int y) {
		return 0 <= x && x < height && 0 <= y && y < width;
	}

	// Inserts a mine in place at the given location.
	public boolean addMine(int i, int j) {
		int x, y;
		if (board[i][j] == -1)
			return false;
		for (int k = 0; k < 8; k++) {
			x = i + offsetXY[k][0];
			y = j + offsetXY[k][1];
			// check bounds and its open to put mine
			if ((0 <= x && x < height && 0 <= y && y < width) && board[x][y] != -1)
				board[x][y]++;
		}

		// indicate that there is a mine
		board[i][j] = -1;
		numMines++;
		return true;

	}

//	indicates that the user is opening this location.
//	Returns true if it is not a mine. 
//	In addition, if there is no mine near this location, then open all the neighboring locations - and so on recursively.
	public boolean open(int i, int j) {
		int x, y;
		Location checkPlace = new Location(i, j);
		// return false if its a mine
		if (board[i][j] == -1)
			return false;

		// return true if its flag
		if (flag.contains(checkPlace))
			return true;

		discover.add(checkPlace);// add to discover set

		// return true if the is mines nearby
		if (board[i][j] != 0)
			return true;
		// empty
		else
			for (int k = 0; k < 8; k++) {
				x = i + offsetXY[k][0];
				y = j + offsetXY[k][1];

				if ((0 <= x && x < height && 0 <= y && y < width) && board[i][j] != -1
						&& !discover.contains(new Location(x, y)))
					open(x, y);
			}
		return true;
	}

//	Name a flag in the given location, 
//	or remove it if there is already a flag.
	public void toggleFlag(int x, int y) {

		Location addFlag = new Location(x, y);
		if (flag.contains(addFlag))
			flag.remove(addFlag);// if flag exist,remove it
		else
			flag.add(addFlag);// else,add it.
	}

	// Returns true if all non-mine locations are open.
	public boolean isDone() {
		return (width * height - discover.size() == numMines);
	}

//	Returns representation as a string of the position:
//		If the position is closed 
//		it will return "F" if it has a flag on it, and another "."
	public String get(int i, int j) {
		Location p = new Location(i, j);

		// if not discovered
		if (!discover.contains(p) && !showAll)
			return (flag.contains(p)) ? "F" : ".";

		// is mine
		if (board[i][j] == -1)
			return "X";
		// is empty
		if (board[i][j] == 0)
			return " ";
		// nearby mines
		return board[i][j] + "";
	}

//	If the position is open,
//	it returns "X" if it is a mine, 
//	otherwise the number of mines next to it and if that number is 0 then "".
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

//	Returns a description of the board as a get by get to each location.
	// returns the string
	public String toString() {
		StringBuilder f = new StringBuilder();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				f.append(get(i, j));
			f.append('\n');
		}

		return f.toString();
	}

}
