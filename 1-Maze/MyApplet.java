import java.awt.*;

public class MyApplet extends BufferedApplet
{
	int w = 0, h = 0;
	boolean isMouseDown = false;
	boolean refresh = false;
	int size = 10;
	MazeClass maze = new MazeClass();
	int n = 30;
	int[] location = {15, 15};
	boolean[] scale = {true, true, true, true, true};
	private final int N = MazeClass.N;

	public boolean mouseDown(Event e, int x, int y) {
		if(y <= 380 && y >= 350) {
			if(x <= 60 && x >= 10) {
				n = 5;
				scale[0] = false;
				refresh = true;
			}
			if(x <= 120 && x >= 70) {
				n = 10;
				scale[1] = false;
				refresh = true;
			}
			if(x <= 180 && x >= 130) {
				n = 15;
				scale[2] = false;
				refresh = true;
			}
			if(x <= 240 && x >= 190) {
				n = 20;
				scale[3] = false;
				refresh = true;
			}
			if(x <= 300 && x >= 250) {
				n = 30;
				scale[4] = false;
				refresh = true;
			}
		}
		return true;
	}

	public boolean mouseUp(Event e, int x, int y) {
		for(int i = 0; i < 5; i++) 
			scale[i] = true;
		return true;
	}

	public void move(char c) {
		switch(c) {
			case 'u': //key up
				if((maze.Maze[(location[1] - 5) / 10][(location[0] - 5) / 10] & 1) == 0)
					location[1] -= 10; 
				break;
			case 'd': //key down
				if((maze.Maze[(location[1] + 5) / 10][(location[0] - 5) / 10] & 1) == 0)
					location[1] += 10; 
				break;
			case 'l': 
				if((maze.Maze[(location[1] - 5) / 10][(location[0] - 15) / 10] & 2) == 0)
					location[0] -= 10;
				break;
			case 'r':
				if((maze.Maze[(location[1] - 5) / 10][(location[0] - 5) / 10] & 2) == 0)
					location[0] += 10;
				break;
		}
	}


	public boolean keyUp(Event e, int key) {
		switch(key) {
			case 'r': refresh = true; break;
			case 1004: move('u'); break;	//key up
			case 1005: move('d'); break;	//key down
			case 1006: move('l'); break;	//key left
			case 1007: move('r'); break;	//key right
		}
		return true;
	}

	public void render(Graphics g) {
		if(w == 0) {
			w = getWidth();
			h = getHeight();
			g.setColor(Color.white);
			g.fillRect(0, 0, w, h);
			maze.init(n, size);
			maze.drawMaze(g);
			g.setColor(Color.black);
			g.drawString("Press r to refresh, and arrow keys to move.", 120, 390);
		}
		if(refresh == true) {
			g.setColor(Color.white);
			g.fillRect(0, 0, (N + 1) * size + 1, (N + 1) * size + 1);
			refresh = false;
			maze.init(n, size);
			maze.drawMaze(g);
			location[0] = 15;
			location[1] = 15;
		}

		g.setColor(Color.white);
		g.fillRect(0, 0, (N + 1) * size + 1, (N + 1) * size + 1);

		g.setColor(Color.black);
		maze.drawMaze(g);
		g.fillRect(location[0] - 1, location[1] - 1, 3, 3);

		if(location[0] == (n + 1) * size + 5) {
			refresh = true;
		}

		g.setColor(Color.red);
		g.fill3DRect(10, 350, 50, 30, scale[0]);

		g.setColor(Color.black);
		g.drawString("5X5", 23, 370);

		g.setColor(Color.red);
		g.fill3DRect(70, 350, 50, 30, scale[1]);

		g.setColor(Color.black);
		g.drawString("10X10", 75, 370);

		g.setColor(Color.red);
		g.fill3DRect(130, 350, 50, 30, scale[2]);

		g.setColor(Color.black);
		g.drawString("15X15", 135, 370);

		g.setColor(Color.red);
		g.fill3DRect(190, 350, 50, 30, scale[3]);

		g.setColor(Color.black);
		g.drawString("20X20", 195, 370);

		g.setColor(Color.red);
		g.fill3DRect(250, 350, 50, 30, scale[4]);

		g.setColor(Color.black);
		g.drawString("30X30", 255, 370);
	}
}
