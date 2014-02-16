package BrAltarPro;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.ButtonGroup;

import org.brad.enums.Bones;
import org.parabot.core.ui.components.LogArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UI extends JFrame {

	private JPanel contentPane;
	private JLabel lblTitle;
	private JComboBox boneBox;
	private JRadioButton rdbtnEdgeville;
	private JRadioButton rdbtnPremiumZone;
	private JButton btnStart;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private String[] bones = new String[Bones.values().length];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI frame = new UI();
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
	public UI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 266, 219);
		setTitle("BrAltarPro by Bradsta");

		// List initlization
		setupLists();
		// end

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblTitle = new JLabel("BrAltarPro v1.000");
		lblTitle.setFont(new Font("Candara", Font.PLAIN, 26));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(12, 25, 219, 38);
		contentPane.add(lblTitle);

		boneBox = new JComboBox(bones);
		boneBox.setBounds(22, 71, 209, 22);
		contentPane.add(boneBox);

		rdbtnEdgeville = new JRadioButton("Edgeville");
		buttonGroup.add(rdbtnEdgeville);
		rdbtnEdgeville.setBounds(23, 102, 91, 25);
		contentPane.add(rdbtnEdgeville);

		rdbtnPremiumZone = new JRadioButton("Premium Zone");
		buttonGroup.add(rdbtnPremiumZone);
		rdbtnPremiumZone.setBounds(118, 102, 119, 25);
		contentPane.add(rdbtnPremiumZone);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				BrAltarPro.bones = Bones.values()[boneBox.getSelectedIndex()];
				BrAltarPro.useEdge = rdbtnEdgeville.isSelected();

				LogArea.log("Bone type: " + bones + " Location: "
						+ (BrAltarPro.useEdge ? "Edgeville" : "Premium Zone"));
				
				BrAltarPro.startScript = true;
				dispose();
			}
		});
		btnStart.setBounds(22, 136, 209, 25);
		contentPane.add(btnStart);
	}

	/**
	 * Initalizes lists for GUI.
	 */
	private void setupLists() {
		for (int i = 0; i < bones.length; i++) {
			bones[i] = Bones.values()[i].getName();
		}
	}
}
