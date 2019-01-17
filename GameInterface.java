import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameInterface extends JPanel implements ActionListener, KeyListener {

	// Creates the member variables
	Timer t = new Timer(5, this);// Creates a new timer, gets called by the method actionPerformed
	private Player player;// Instance of Player class
	private Monster[] monsters;// Instance of Monster class
	private Map map;// Instance of Map class
	private JLabel statusbar;
	private boolean pause;// Game can be paused
	private int amountOfMonsters;// Total amount of monsters on the map
	private int health;// Health out of 100, game over when this gets to 0

	public GameInterface(JLabel statusbar) {
		this.statusbar = statusbar;
		init();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		t.start();
	}

	private void init() {
		amountOfMonsters = 30;
		health = 100;
		map = new Map(this);
		player = new Player(map, this);
		monsters = new Monster[amountOfMonsters];// amountOfMonsters is equal to whatever amount is declared before
		map.setMonster(monsters);
		pause = false;// Game is not paused at the beginning

		for (int i = 0; i < amountOfMonsters; i++) {// Creates all the monsters
			monsters[i] = new Monster(map, i, this);
		}

	}

	public void actionPerformed(ActionEvent e) {// Gets called by timer every 5 milliseconds
		repaint();// Repaints, calling paintComponent
	}
	
	public void paintComponent(Graphics g) {// Called by actionPerformed after it is called by timer
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Color currentColor = g.getColor();
		g.setColor(new Color(204, 255, 255));
		g.fillRect(1, 1, this.getWidth(), this.getHeight());
		g.setColor(currentColor);
		map.draw(g2);
		player.draw(g2);
		int deadMonster = 0;// The game begins with no dead monsters
		for (int i = 0; i < amountOfMonsters; i++) {
			monsters[i].draw(g2);
			if (monsters[i].isDead()) {// Checks if any monsters are dead
				++deadMonster;
			}
		}
		if (health <= 0) {// The game is lost when the player's health gets to 0
			statusbar.setText("GAME OVER! YOU LOSE! PRESS R TO RESTART");
			t.stop();
		} else if (deadMonster == monsters.length) {// The game is won when all the monsters are dead
			statusbar.setText("YOU WIN! PRESS R TO RESTART");
			t.stop();
		} else if (pause == true) {
			statusbar.setText("The game is paused, press p to unpause");
			t.stop();// The timer is stopped and nothing is repainted, stopping all actions until it is pressed again
		} else {// This status bar is shown as long as the game is not over
			statusbar.setText("[HEALTH: " + health + "] [arrow keys to move] [spacebar to fire] [p to pause] [r to restart]");

		}
	}

	public void changeHealth(int damage) {
		health -= damage;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		player.keyMove(key);
		if (key == KeyEvent.VK_P) {
			if (pause == false) {// If pause is already false and the key is pressed, it becomes true
				pause = true;// Pauses game, stopping timer and displayed pause statement as shown in paintComponent method
			} else {// This is to unpause the game
				pause = false;
				t.start();
			}
		}
		if (key == KeyEvent.VK_R) {// Restarts the game, resetting everything
			t.start();
			init();
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		player.keyReleased(key);
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public Dimension getPreferredSize() {
		return new Dimension(1200, 1200);
	}
}
