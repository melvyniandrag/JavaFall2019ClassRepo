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
		this.add(makeButton2());
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
				JOptionPane.showMessageDialog(b, "Hello World!");
			}
		});
		return b;
	}

	private JButton makeButton2() {
		final JButton b2 = new JButton();
		b2.setText("Click me!");
		b2.setBounds(40, 40, 100, 30);
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(b2, "Hello World!");
			}
		});
		return b2;
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
