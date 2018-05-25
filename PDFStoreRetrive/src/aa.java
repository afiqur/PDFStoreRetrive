import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.event.ActionEvent;

public class aa extends JFrame {

	private JPanel contentPane;
	JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					aa frame = new aa();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public aa() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				File f = chooser.getSelectedFile();
				String filename = f.getAbsolutePath();

				try {

					FileReader reader = new FileReader(filename);
					Reader redaer = null;
					BufferedReader br = new BufferedReader(redaer);
					textArea.read(br, null);
					br.close();
					textArea.requestFocus();
				}

				catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2);

				}
			

			}
		});
		btnNewButton.setBounds(124, 11, 89, 23);
		contentPane.add(btnNewButton);
		
		 textArea = new JTextArea();
		textArea.setBounds(55, 48, 330, 167);
		contentPane.add(textArea);
	}
}
