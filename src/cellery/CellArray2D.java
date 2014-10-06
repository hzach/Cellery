package cellery;


/**
 * A CellArray is a 2-d array of {@link Cell} objects which can kill and revive Cells and retrieve
 * neighborhoods about individual cells.
 * @author Zach Tidwell
 * 
 */
public class CellArray2D {
	public final int length;
	public final int width;
	private final Cell[][] cells;

	/**
	 * Instantiates a {@link CellArray2D} from an array of integers.
	 * @param intArray the generating array.
	 */
	public CellArray2D(int[][] intArray) {
		this(initializeCells(intArray));
	}

	/**
	 * Instantiates a {@link CellArray2D} from a {@link Cell}[][].
	 * @param cells a 2D array of Cells.
	 */
	protected CellArray2D(Cell[][] cells) {
		this.length = cells.length;
		this.width = cells[0].length;
		this.cells = cells;
	}

	/**
	 * Initializes a {@link Cell}[][] from an integer array. A living Cell is placed in the index corresponding 
	 * to a value of 1 in the input integer array and dead cells are placed everywhere else. 
	 * @param intArray the input array.
	 * @return the array of Cells generated by input array.
	 */
	protected static Cell[][] initializeCells(int[][] intArray) {
		Cell[][] cells = new Cell[intArray.length][intArray[0].length];
		for (int i = 0; i < intArray.length; i++) {
			for (int j = 0; j < intArray[0].length; j++)
				cells[i][j] = new Cell(intArray[i][j]);
		}
		return cells;
	}

	protected static boolean boundariesOK(int ii, int i, int min, int max) {
		return (!(ii < min) && !(ii >= max) && (ii != i));
	}

	/**
	 * Retrieves the number of living {@link Cell}s in the vertical neighborhood about the
	 * ij-th cell of radius r.
	 * @param i row index containing the central cell.
	 * @param j column index containing the central cell.
	 * @param r the radius of the neighborhood.
	 * @return the number of living cells.
	 */
	public int getVertical(int i, int j, int r) {
		int imax = this.length;
		int sum = 0;
		for (int n = 0; n <= 2 * r; n++) {
			int ii = i - r + n;
			if (boundariesOK(ii, i, 0, imax)) {
				sum += this.getCell(ii, j).toBit();
			}
		}
		return sum;
	}

	/**
	 * Retrieves the number of living {@link Cell}s in the horizontal neighborhood about the ij-th
	 * cell of radius r.
	 * @param i row index containing the central cell.
	 * @param j column index containing the central cell.
	 * @param r radius if the neighborhood.
	 * @return the number of living cells.
	 */
	public int getHorizontal(int i, int j, int r) {
		int jmax = this.width;
		int sum = 0;
		for (int n = 0; n <= 2 * r; n++) {
			int jj = j - r + n;
			if (boundariesOK(jj, j, 0, jmax)) {
				sum += this.getCell(i,jj).toBit();
			}
		}
		return sum;
	}

	/**
	 * Retrieves the {@link Cell}s in the right-hand diagonal neighborhood
	 * about the ij-th cell of radius r.
	 * @param i row index containing the central cell.
	 * @param j column index containing the central cell.
	 * @param r radius if the neighborhood.
	 * @return the number of living cells.
	 */
	public int getRightDiag(int i, int j, int r) {
		int imax = this.length;
		int jmax = this.width;
		int sum = 0;
		for (int n = 0; n <= 2 * r; n++) {
			int ii = i + r - n;
			int jj = j - r + n;
			if (boundariesOK(ii, i, 0, imax) && boundariesOK(jj, j, 0, jmax)) {
				sum += this.getCell(ii, jj).toBit();
			}
		}
		return sum;
	}

	/**
	 * Retrieves the number of living {@link Cell}s in the left-hand diagonal neighborhood
	 * about the ij-th cell of radius r.
	 * @param i row index containing the central cell.
	 * @param j column index containing the central cell.
	 * @param r radius if the neighborhood.
	 * @return the number of living cells.
	 */
	public int getLeftDiag(int i, int j, int r) {
		int imax = this.length;
		int jmax = this.width;
		int sum = 0;
		for (int n = 0; n <= 2 * r; n++) {
			int ii = i - r + n;
			int jj = j - r + n;
			if (boundariesOK(ii, i, 0, imax) && boundariesOK(jj, j, 0, jmax)) {
				sum += this.getCell(ii,jj).toBit();
			}
		}
		return sum;
	}
	
	/**
	 * Returns the number of living {@link Cell}s in the Moore neighborhood about the ij-th cell. 
	 * @param i row index containing the central cell.
	 * @param j column index containing the central cell.
	 * @return the number of living cells.
	 */
	public int moore(int i, int j) {
		int sum = 0;
		sum += this.getVertical(i, j, 1);
		sum += this.getHorizontal(i, j, 1);
		sum += this.getRightDiag(i, j, 1);
		sum += this.getLeftDiag(i, j, 1);
		return sum;
	}
	
	/**
	 * Returns the number of living {@link Cell}s in the Von Neumann neighborhood of radius r about the ij-th cell.
	 * @param i row index containing the central cell.
	 * @param j column index containing the central cell.
	 * @param r radius if the neighborhood.
	 * @return the number of living cells.
	 */
	public int vonNeumann(int i, int j, int r) {
		int sum = 0;
		sum += this.getVertical(i, j, r);
		sum += this.getHorizontal(i, j, r);
		sum += this.getRightDiag(i, j, r-1);
		sum += this.getLeftDiag(i, j, r-1);
		return sum;
	}

	/**
	 * Gives an array of binary Values corresponding to the {@link Cell}s in this.
	 * 1 indicates that the Cell is alive and 0 indicates that it is dead.
	 * @return the binary array represented the states of Cells in this
	 */
	public int[][] toBinaryArray() {
		int[][] bin = new int[this.length][this.width];
		for (int i = 0; i < this.length; i++) {
			for (int j = 0; j < this.width; j++) {
				bin[i][j] = this.getCell(i, j).toBit();
			}
		}
		return bin;
	}


	/**
	 * Counts the number of living {@link Cell}s in this
	 * @return the number of Cells in this.
	 */
	public int livingCells() {
		int sum = 0;
		for (int i = 0; i < this.length; i++) {
			for (int j = 0; j < this.width; j++) {
				sum += this.cells[i][j].toBit();
			}
		}
		return sum;
	}

	/**
	 * Retrieves the {@link Cell} in the ij-th position of this.
	 * @param i row position
	 * @param j column position
	 * @return the Cell 
	 */
	public Cell getCell(int i, int j) {
		return this.cells[i][j];
	}

	/**
	 * Kills the {@link Cell} at the ij-th position of this
	 * @param i row position 
	 * @param j column position
	 */
	public void killCell(int i, int j) {
		this.cells[i][j].kill();
	}

	/**
	 * Resurrects the {@link Cell} at the ij-th position in this
	 * @param i row position
	 * @param j column position
	 */
	public void resurrectCell(int i, int j) {
		this.cells[i][j].revive();
	}

	/**
	 * Translates this into a String containing binary integers representing the {@link Cell} states of this.
	 * @return a string representing this.
	 */
	@Override
	public String toString() {
		StringBuilder theString = new StringBuilder();
		theString.append("[");
		for (int i = 0; i < this.length; i++) {
			theString.append("[");
			for (int j = 0; j < this.width; j++) {
				theString.append(this.cells[i][j].toBit());
				if (j < this.width - 1)
					theString.append(", ");
			}
			theString.append("]");
			if (i < this.length)
				theString.append(", ");
		}
		theString.append("]");
		return theString.toString();
	}
}