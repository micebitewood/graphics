import java.awt.*;
public class MazeClass{
	public static final int N = 30;
	int size = 10;
	int n = 5;
	public int Maze[][] = new int[N + 2][N + 2];

	
		public void init(int n, int size) {
			this.n = n;
			this.size = size;
			for (int i = 0; i <= n + 1; i++) {
				for (int j = 0; j <= n + 1; j++) {
					if (i == 0 || i == n + 1 || j == 0 || j == n + 1) {
						Maze[i][j] = -1;
					} else {
						Maze[i][j] = 3;
					}
				}
			}
			genMaze(n / 2, n / 2);
			Maze[n][n] &= 0xd;
			findPath(1, 1);
			//print();
		}

	public void print() {
		for(int i = 0; i <= n + 1; i++) {
			for(int j = 0; j <= n + 1; j++) {
				System.out.print(Maze[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void genMaze(int i, int j) {
		int dir;
		Maze[i][j] |= 0x4;
		while (Maze[i][j + 1] == 3 || Maze[i + 1][j] == 3 || Maze[i][j - 1] == 3 || Maze[i - 1][j] == 3) {
			dir = (int) (4 * Math.random() + 1);
			if (dir == 1 && Maze[i][j + 1] == 3) {
				Maze[i][j] &= 0xd;
				genMaze(i, j + 1);
			} else if (dir == 2 && Maze[i - 1][j] == 3) {
				Maze[i][j] &= 0xe;
				genMaze(i - 1, j);
			} else if (dir == 3 && Maze[i][j - 1] == 3) {
				Maze[i][j - 1] &= 0xd;
				genMaze(i, j - 1);
			} else if (dir == 4 && Maze[i + 1][j] == 3) {
				Maze[i + 1][j] &= 0xe;
				genMaze(i + 1, j);
			}
		}
	}

	public void drawMaze(Graphics g) {
		g.setColor(Color.black);
		g.drawLine(size, 2 * size, size, (n + 1) * size);
		g.drawLine(size, (n + 1) * size, (n + 1) * size, (n + 1) * size);
		for(int i = 1; i <= n; i++) {
			for(int j = 1; j <= n; j++) {
				if((Maze[i][j] & 1) == 1) {
					g.drawLine(j * size, i * size, (j + 1) * size, i * size);
				}
				if((Maze[i][j] & 2) == 2) {
					g.drawLine((j + 1) * size, i * size, (j + 1) * size, (i + 1) * size);
				}
			}
		}
	}
/*
	void drawPath(Graphics g) {
		g.setColor(Color.red);
		int i = 1, j = 1;
		while(i != n || j != n) {
			if ((Maze[i][j] & 0x80) == 0x80) {
				g.drawLine(j * size + size / 2, i * size + size / 2, j * size + size / 2, i * size - size / 2);
				i--;
			} 
			else if ((Maze[i][j] & 0x40) == 0x40) {
				g.drawLine(j * size + size / 2, i * size + size / 2, j * size + size / 2, i * size + 3 * size / 2);
				i++;
			} 
			else if ((Maze[i][j] & 0x20) == 0x20) {
				g.drawLine(j * size + size / 2, i * size + size / 2, j * size - size / 2, i * size + size / 2);
				j--;
			} 
			else if ((Maze[i][j] & 0x10) == 0x10) {
				g.drawLine(j * size + size / 2, i * size + size / 2, j * size + 3 * size / 2, i * size + size / 2);
				j++;
			}
		}
		g.drawLine(size, size + size / 2, size + size / 2, size + size / 2);
		g.drawLine(n * size + size / 2, n * size + size / 2, n * size + size, n * size + size / 2);
	}
*/
	void findPath(int x, int y) {
		if (x == n && y == n) {
		} else {
			Maze[x][y] |= 0x8;
			//up
			if (Maze[x - 1][y] != -1 && (Maze[x - 1][y] & 8) == 0 && (Maze[x][y] & 1) == 0) {
				Maze[x][y] |= 0x80;
				Maze[x][y] &= 0x8f;
				findPath(x - 1, y);
			}
			//down
			if (Maze[x + 1][y] != -1 && (Maze[x + 1][y] & 8) == 0 && (Maze[x + 1][y] & 1) == 0) {
				Maze[x][y] |= 0x40;
				Maze[x][y] &= 0x4f;
				findPath(x + 1, y);
			}
			//left
			if (Maze[x][y - 1] != -1 && (Maze[x][y - 1] & 8) == 0 && (Maze[x][y - 1] & 2) == 0) {
				Maze[x][y] |= 0x20;
				Maze[x][y] &= 0x2f;
				findPath(x, y - 1);
			}
			//right
			if (Maze[x][y + 1] != -1 && (Maze[x][y + 1] & 8) == 0 && (Maze[x][y] & 2) == 0) {
				Maze[x][y] |= 0x10;
				Maze[x][y] &= 0x1f;
				findPath(x, y + 1);
			}
		}
	}
}
