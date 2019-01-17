import java.awt.*;
import java.awt.geom.*;

public class Bullet {

	private int x, y, velX, velY;
	private Map map;

	public Bullet(int x, int y, int direction, Map map) {
		this.x = x;
		this.y = y;
		this.velX = 0;
		this.velY = 0;
		this.map = map;
		setVelosity(direction);
	}

	public void draw(Graphics2D g2) {
		if (canMove()) {
			move();
			Color currentColor = g2.getColor();
			g2.setColor(new Color(0, 255, 0));
			g2.fill(new Ellipse2D.Double(x, y, 8, 8));
			g2.setColor(currentColor);
		}
	}

	private void setVelosity(int direction) {
		if (direction == 1) {// North
			this.velY = -5;
		} 
		else if (direction == 2) {// East
			this.velX = 5;
		} 
		else if (direction == 3) {// South
			this.velY = 5;
		} 
		else if (direction == 4) {// West
			this.velX = -5;
		}
	}

	private void move() {
		x += velX;
		y += velY;
	}

	public boolean canMove() {
		return map.canMove(x, y, x + 8, y + 8);
	}
}
