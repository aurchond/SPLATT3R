import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class Player {
	private int x, y, velX, velY;
	private Bullet bullets[];
	private int bulletCount;
	private int direction; // 1: North ; 2: South; 3: East; 4: West
	private Map map;
	private GameInterface gameInterface;
	private Image[] guns;

	public Player(Map map, GameInterface gameInterface) {
		this.map = map;
		this.gameInterface = gameInterface;
		this.x = 584;//X-coordinate of player spawn location, it is 584 because that is 
					//1/2*(panel length (1200) - gun model size (32)), centering it
		this.y = 100;//Y-coordinate of player spawn location
		this.velX = 0;// Velocity in the X direction
		this.velY = 0;// Velocity in the Y direction
		this.bullets = new Bullet[100];//Creates 100 bullets 
		bulletCount = 0;//Checker to find how many bullets have been fired
		this.direction = 4;// Sets default direction to right (east)
		guns = new Image[4];
		for (int i = 1; i <= 4; i++) {
			guns[i - 1] = Toolkit.getDefaultToolkit().getImage("images/gun" + i + ".png");
		}
	}

	public void draw(Graphics2D g2) {
		g2.drawImage(guns[direction - 1], x, y, null);//Draws a gun in different positions depending on which way its going to shoot
		map.setPlayerPosition(new Point((int) x, (int) y));
		for (int i = 0; i < bulletCount; i++) {
			if (bullets[i].canMove()) {
				bullets[i].draw(g2);
				map.hitmonster(new Point((int)x, (int)y), direction);
			}
		}
	}

	public void up() {// Calls method if "up" key is pressed
		velX = 0;
		velY = -5;
	}

	public void down() {// Calls method if "down" key is pressed
		velX = 0;
		velY = 5;
	}

	public void left() {// Calls method if "left" key is pressed
		velX = -5;
		velY = 0;
	}

	public void right() {// Calls method if "right" key is pressed
		velX = 5;
		velY = 0;
	}

	public void keyMove(int key) {
		if (key == KeyEvent.VK_UP) {
			setDirection(1);
			up();
		}
		if (key == KeyEvent.VK_DOWN) {
			setDirection(3);
			down();
		}
		if (key == KeyEvent.VK_LEFT) {
			setDirection(4);
			left();
		}
		if (key == KeyEvent.VK_RIGHT) {
			setDirection(2);
			right();
		}
		if (key == KeyEvent.VK_SPACE) {
			fireBullet();
		}
		if (map.canMove(x + velX, y + velY, x + velX + 32, y + velY + 32)) {
			x += velX;
			y += velY;
		}
	}

	private void fireBullet() {
		if (bulletCount > 99) {
			bulletCount = 0; // Resets it
			bullets = new Bullet[100];
		}
		Bullet bullet = new Bullet(x, y, direction, map);
		bullets[bulletCount++] = bullet;
	}

	public void keyReleased(int key) {
		if (key == KeyEvent.VK_UP) {
			velY = 0;
		}
		if (key == KeyEvent.VK_DOWN) {
			velY = 0;
		}
		if (key == KeyEvent.VK_LEFT) {
			velX = 0;
		}
		if (key == KeyEvent.VK_RIGHT) {
			velX = 0;
		}
	}

	public int getDirection() {
		return this.direction;
	}

	public void setDirection(int dir) {
		this.direction = dir;
	}

}
