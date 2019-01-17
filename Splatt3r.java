import javax.swing.*;
import java.awt.*;

public class Splatt3r {

	static JFrame frame = new JFrame("SPLATT3R!");
	private JLabel statusbar;

	public Splatt3r() {
		initUI();
	}

	private void initUI() {
		frame.getContentPane().setLayout(new BorderLayout());
		statusbar = new JLabel();// Creates status bar where health, tips and game status will be displayed
		GameInterface gameInterface = new GameInterface(statusbar);

		frame.getContentPane().add(BorderLayout.CENTER, gameInterface);
		gameInterface.setBackground(new Color(138, 182, 252));
		JPanel jpanel = new JPanel();
		jpanel.setBackground(new Color(204, 255, 153));
		jpanel.add(statusbar);
		statusbar.setFont(statusbar.getFont().deriveFont((float) 20.0));
		frame.getContentPane().add(BorderLayout.SOUTH, jpanel);
		frame.setSize(1200, 1200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Splatt3r();

			}
		});

	}

}
