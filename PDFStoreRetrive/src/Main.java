
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Main extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	private JTextField nameTF;
	String filename = null;
	byte[] image_detail = null;
	private JButton btnSend;
	private JTextField searchTF;
	private JButton btnDownload, insertBtn;
	private JLabel imgLabel;
	InputStream in;
	InputStream input;
	OutputStream output;
	private ImageIcon format = null;

	/**
	 * Create the frame.
	 */
	public Main() {
		setTitle("PDF and Image");
		conn = MySQLConnection.ConnecrDb();
		load();
	}

	private void load() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		insertBtn = new JButton("Insert");
		insertBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg", "gif", "png", "pdf");
				fileChooser.addChoosableFileFilter(filter);
				int result = fileChooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					try {
						@SuppressWarnings("resource")
						FileInputStream fis = new FileInputStream(selectedFile);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();

						byte[] buf = new byte[1024];
						for (int readNum; (readNum = fis.read(buf)) != -1;) {
							bos.write(buf, 0, readNum);
						}

						image_detail = bos.toByteArray();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else if (result == JFileChooser.CANCEL_OPTION) {
					System.out.println("No Data");
				}

				insertBtn.setText(filename);

			}

		});
		insertBtn.setBounds(169, 6, 174, 33);
		contentPane.add(insertBtn);

		nameTF = new JTextField();
		nameTF.setBounds(12, 6, 147, 33);
		contentPane.add(nameTF);
		nameTF.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					String query = " insert into pdfs (Name,PDF) values (?,?)";
					pst = conn.prepareStatement(query);
					pst.setString(1, nameTF.getText());
					pst.setBytes(2, image_detail);
					pst.execute();

					JOptionPane.showMessageDialog(null, "PDF Saved Successfully");

					pst.close();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSend.setBounds(353, 6, 76, 33);
		contentPane.add(btnSend);

		searchTF = new JTextField();
		searchTF.setBounds(288, 101, 141, 20);
		contentPane.add(searchTF);
		searchTF.setColumns(10);

		btnDownload = new JButton("PDF Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String query = "Select * from pdfs where serial ='" + searchTF.getText() + "'";
					pst = conn.prepareStatement(query);
					rs = pst.executeQuery();
					int i = 1;
					in = null;
					while (rs.next()) {
						in = rs.getBinaryStream("PDF");
					}

					int available1 = in.available();
					byte[] bt = new byte[available1];
					in.read(bt);

					FileOutputStream fout = new FileOutputStream("C:/Users/PIASH/Documents/piash.pdf");
					DataOutputStream dout = new DataOutputStream(fout);
					dout.write(bt, 0, bt.length);
					fout.close();

					pst.close();
					rs.close();
				} catch (Exception e1) {
					e1.printStackTrace();

				}

			}
		});
		btnDownload.setBounds(288, 167, 141, 23);
		contentPane.add(btnDownload);

		imgLabel = new JLabel("");
		imgLabel.setBounds(12, 101, 266, 118);
		contentPane.add(imgLabel);

		JButton btnShow = new JButton("Image Download");
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String query = "Select * from pdfs where serial ='" + searchTF.getText() + "'";
					pst = conn.prepareStatement(query);
					rs = pst.executeQuery();
					int i = 1;
					in = null;
					while (rs.next()) {
						in = rs.getBinaryStream("PDF");
						byte[] imagedata = rs.getBytes("PDF");
						format = new ImageIcon(imagedata);
						imgLabel.setIcon(format);
					}
					int available1 = in.available();
					byte[] bt = new byte[available1];
					in.read(bt);

					FileOutputStream fout = new FileOutputStream("C:/Users/PIASH/Documents/piash.jpg");
					DataOutputStream dout = new DataOutputStream(fout);
					dout.write(bt, 0, bt.length);
					fout.close();
					pst.close();
					rs.close();
				} catch (Exception e1) {
					e1.printStackTrace();

				}

			}
		});
		btnShow.setBounds(288, 133, 141, 23);
		contentPane.add(btnShow);
	}
}
