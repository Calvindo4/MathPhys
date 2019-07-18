import javax.swing.JFrame;

public class Main{
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		GamePlay gameplay = new GamePlay();
		frame.setBounds(10, 10, 709, 625);
		frame.setTitle("BrickBreaker");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(gameplay);
	}
}
