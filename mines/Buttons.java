package mines;

import javafx.scene.control.Button;

//class that extents button and uses get x and gety
public class Buttons extends Button {
	private int x, y;

	public Buttons(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
