import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Sample extends JFrame {
	public Sample() {
		super();
		this.setTitle("HelloApp");
		this.getContentPane().setLayout(null);
		this.setBounds(100, 100, 180, 140);
		this.add(makeButton());
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private JButton makeButton() {
		final JButton b = new JButton();
		b.setText("Click me!");
		b.setBounds(40, 80, 200, 30);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(b, "0xd8 0x3d 0xde 0x00");
				JOptionPane.showMessageDialog(null, "0xd8 0x3d 0xde 0x00", "my title", 0);
			}
		});
		return b;
	}
	
	public static void main(String[] args) {
		// Swing calls must be run by the event dispatching thread.
		try{
			SwingUtilities.invokeAndWait(() -> new Sample());
		}
		catch( Exception e ){
			System.out.println("error");
		}
	}
}
