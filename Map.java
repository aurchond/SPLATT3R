import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Map {

	private Monster[] monsters;
	private Point[] monsterPositions;
	private Point playPos;
	private int monsterCount;
	private Rect[] rects;

	JPanel panel;

	public Map(JPanel panel) {
		this.panel = panel;
		monsters = null;
		rects = new Rect[5];//Creates six walls out of the rect class
		rects[0] = new Rect(100, 200, 500, 220);//Top right
		rects[1] = new Rect(700, 200, 1100, 220);//Top left
		
		rects[2] = new Rect(200, 1000, 1000, 1020);//Bottom
		
		rects[3] = new Rect(200, 300, 220, 920);//Left
		rects[4] = new Rect(980, 300, 1000, 920);//Right
		
		
		
	}

	public void draw(Graphics2D g) {
		Color currentColor = g.getColor();
		g.setColor(new Color(100, 55, 252));
		for (int i = 0; i < rects.length; i++) {
			g.fillRect(rects[i].getX1(), rects[i].getY1(), rects[i].getX2() - rects[i].getX1(),
					rects[i].getY2() - rects[i].getY1());
		}
		g.setColor(currentColor);
	}

	public void setMonster(Monster[] monsters) {
		this.monsters = monsters;
		this.monsterCount = monsters.length;
		monsterPositions = new Point[monsterCount];
	}

	public void setMonsterPosition(int monsterId, Point point) {
		if (monsterId < monsterCount) {
			monsterPositions[monsterId] = point;
		}
	}

	public void setPlayerPosition(Point point) {
		playPos = point;
	}

	public boolean canMove(int x1, int y1, int x2, int y2) {
		int height = panel.getHeight() - 10; // Checks if player/monster is outside the panel height
		int width = panel.getWidth() - 10;// Checks if player/monster is outside the panel height
		if (x1 < 0 || y1 < 0 || y2 >= height || x2 >= width) {
			return false;
		} 
		else {
			for (int i = 0; i < rects.length; i++) {
				if (rects[i].within(x1, y1, x2, y2)) {
					return false;
				}
			}
		}
		return true;
	}

	// Checks if there is a monster in 16 x 200 rectangle towards "direction"
	// (eg. north) every time the player shoots
	public int hitmonster(Point point, int direction) {
		int hit = 0;
		int height = panel.getHeight();
		int width = panel.getWidth();
		int x1 = point.getX();
		int y1 = point.getY();
		int x2 = point.getX();
		int y2 = point.getY();
		if (direction == 1) {// North
			x1 = x1 - 16;
			x2 = x1 + 16;
			y1 = y2 - height;
		} 
		else if (direction == 2) {// East
			x2 = x1 + width;
			y1 = y1 - 16;
			y2 = y2 + 16;
		} 
		else if (direction == 3) {// South
			x1 = x1 - 16;
			x2 = x1 + 16;
			y2 = y1 + height;
		} 
		else if (direction == 4) {// West
			x1 = x2 - width;
			y1 = y1 - 16;
			y2 = y2 + 16;
		}

		for (int i = 0; i < monsterCount; i++) {
			if (!monsters[i].isDead() && monsterPositions[i].getX() >= x1 && monsterPositions[i].getY() >= y1
					&& monsterPositions[i].getX() <= x2 && monsterPositions[i].getY() <= y2) {
				monsters[i].setDead(true);
				++hit;

			}
		}
		return hit;
	}

	// Checks if player's position within a certain rectangle range from the monster
	public boolean playerInBulletRange(Point point, int direction) {
		int x1 = point.getX();
		int y1 = point.getY();
		int x2 = point.getX();
		int y2 = point.getY();
		if (direction == 3) {// South
			x1 = x1 - 4;
			x2 = x1 + 8;
			y2 = y2 + 100;
		} 
		else if (direction == 2) {// East
			x1 = x1 - 100;
			y1 = y1 - 4;
			y2 = y2 + 8;
		} 
		else if (direction == 1) {// North
			x1 = x1 - 4;
			x2 = x1 + 8;
			y1 = y1 - 100;
		} 
		else if (direction == 4) {// West
			x2 = x1 + 100;
			y1 = y1 - 4;
			y2 = y2 + 8;
		}
		if (playPos.getX() >= x1 && playPos.getY() >= y1 && playPos.getX() <= x2 && playPos.getY() <= y2) {
			return true;
		}
		return false;

	}

	// Checks if player direction is within a 400 x 400 square around the monster
	// Also the square is a different size then the shooting range so that they
	// will only shoot at the player if they get in that exact range
	public int scanPlayer(Point monsterPoint) {
		int monsterDirection = 0;
		int length = monsterPoint.getX() - playPos.getX();
		int height = monsterPoint.getY() - playPos.getY();
		int absLength = Math.abs(length);// Finds absolute value to because the north and west directions will be negative,
											// but wants to find the longest length regardless of if negative or not
		int absHeigth = Math.abs(height);
		if (absLength < 200 && absHeigth < 200) {// If the absolute value amount is within the square
			if (absHeigth > absLength) { // Finds if the player is vertically closer to the monster (within the square)
				if (height > 0) {// If the monster is below the player, then they move north
					monsterDirection = 1;
				} 
				else { // If the monster is above the player, then they move south
					monsterDirection = 3;
				}
			} 
			else { // Finds if the player is horizontally closer to the monster (within the square)
				if (length > 0) { // If the monster is on the right of the player, then they move west
					monsterDirection = 4;
				} 
				else { // If the monster is on the left of the player, then they move east
					monsterDirection = 2;
				}
			}
		}
		return monsterDirection;

	}
}

class Rect {
	private int x1, y1, x2, y2;

	public Rect(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public int getX1() {
		return this.x1;
	}

	public int getY1() {
		return this.y1;
	}

	public int getX2() {
		return this.x2;
	}

	public int getY2() {
		return this.y2;
	}

	public boolean within(int x1, int y1, int x2, int y2) {
		if (this.x1 <= x2 && this.x2 >= x1 && this.y1 < y2 && this.y2 > y1) {
			return true;
		}
		return false;
	}
}