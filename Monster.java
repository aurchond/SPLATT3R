import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Monster {

	private double x, y, velX, velY;
	private int direction;
	private Map map;
	private int id;
	private boolean dead;
	private Bullet bullets[];
	private int bulletCount;
	private ThreadLocalRandom randomGenerator;
	private GameInterface gameInterface;
	int moveCounter = 50;
	int playerDirection = 0;
	private Image monsterPicture = null;
	private Image monsterDead = null;
	int monsterBulletCounter;//Counter to check if the monster can fire (they fire periodically)

	public Monster(Map map, int id, GameInterface gameInterface) {
		this.gameInterface = gameInterface;
		this.id = id;
		this.map = map;
		this.dead = false;
		randomGenerator = ThreadLocalRandom.current();
		this.x = randomGenerator.nextInt(300, 850 + 1);// X-coordinate of monster spawn location
		this.y = randomGenerator.nextInt(300, 850 + 1);// Y-coordinate of monster spawn location
		map.setMonsterPosition(id, new Point((int) x, (int) y));
		this.velX = 0;
		this.velY = 0;
		monsterBulletCounter = 0;
		this.bullets = new Bullet[16];
		bulletCount = 0;
		this.direction = ThreadLocalRandom.current().nextInt(1, 4 + 1);
		setVelocity(direction);
		monsterPicture = Toolkit.getDefaultToolkit().getImage("images/monster.png");
		monsterDead = Toolkit.getDefaultToolkit().getImage("images/slime.jpg");
	}

	public void draw(Graphics2D g2) {
		if (!dead) {
			g2.drawImage(monsterPicture, (int) x, (int) y, null);
			playerDirection = map.scanPlayer(new Point((int) x, (int) y));
			if (playerDirection != 0) {
				direction = playerDirection;
				setVelocity(direction);
			}
			move();
			int damage = 0;
			if (map.playerInBulletRange(new Point((int) x, (int) y), direction)) {
				if (monsterBulletCounter == 0) {//The monster can fire when the monsterBulletCounter is 0
					fireBullet();
					damage += 20;//Monsters deal this much damage per shot
				}
				++monsterBulletCounter;//Adds to the monsterBulletCounter whenever it moves
				if (monsterBulletCounter > 100) {//Resets the monster's bullets
					monsterBulletCounter = 0;
				}
			}
			gameInterface.changeHealth(damage);

			for (int i = 0; i < bulletCount; i++) {
				if (bullets[i].canMove()) {
					bullets[i].draw(g2);
				}
			}

		} 
		else {
			g2.drawImage(monsterDead, (int) x, (int) y, null);
		}
	}

	private void fireBullet() {
		if (bulletCount > 15) {
			bulletCount = 0; //Resets it
			bullets = new Bullet[16];
		}
		Bullet bullet = new Bullet((int) x, (int) y, direction, map);
		bullets[bulletCount++] = bullet;
	}

	private void move() {
		++moveCounter;
		if (moveCounter > 5000) { // After 5000 moves it changes direction randomly
			moveCounter = 0;
			if (playerDirection == 0)// Changes random direction only if player is not nearby
				setRandomdirection();
		}
		if (!map.canMove((int) (x + velX), (int) (y + velY), (int) (x + velX + 24), (int) (y + velY) + 24)) {
			setRandomdirection();
		} 
		else {
			x += velX;
			y += velY;
			map.setMonsterPosition(id, new Point((int) x, (int) y));
		}
	}

	private void setRandomdirection() {
		int newDir = randomGenerator.nextInt(1, 4 + 1);
		if (newDir == direction) {
			++direction;
			if (direction > 4) {
				direction = 1;
			}
		} else
			direction = newDir;
		this.velX = 0;
		this.velY = 0;
		setVelocity(direction);
	}

	private void setVelocity(int direction) {
		if (direction == 1) {// North
			this.velY = -.2;
		} 
		else if (direction == 2) {// East
			this.velX = .2;
		} 
		else if (direction == 3) {// South
			this.velY = .2;
		} 
		else if (direction == 4) {// West
			this.velX = -.2;
		}
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isDead() {
		return dead;
	}
}
