package BrHerblorePro;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import org.brad.enums.Potions;
import org.parabot.core.ui.components.LogArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UI extends JFrame {

	private JPanel contentPane;
	private JLabel lblBrherblorepro;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JButton btnStart;
	private JComboBox potionBox;
	private JComboBox processBox;
	private JLabel lblSelectPotionTypeprocess;

	private String[] potions = new String[Potions.values().length];

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
		setBounds(100, 100, 292, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Setup any lists
		setupLists();

		lblBrherblorepro = new JLabel("BrHerblorePro v" + BrHerblorePro.VERSION);
		lblBrherblorepro.setFont(new Font("Candara", Font.PLAIN, 26));
		lblBrherblorepro.setHorizontalAlignment(SwingConstants.CENTER);
		lblBrherblorepro.setBounds(12, 13, 258, 54);
		contentPane.add(lblBrherblorepro);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 75, 250, 131);
		contentPane.add(tabbedPane);

		panel = new JPanel();
		tabbedPane.addTab("Main", null, panel, null);
		panel.setLayout(null);

		potionBox = new JComboBox(potions);
		potionBox.setBounds(12, 13, 221, 22);
		panel.add(potionBox);

		processBox = new JComboBox();
		processBox
				.setModel(new DefaultComboBoxModel(new String[] {
						"Full process", "Make unfinished only",
						"Make finished only" }));
		processBox.setBounds(12, 48, 221, 22);
		panel.add(processBox);

		lblSelectPotionTypeprocess = new JLabel("Select potion type/process");
		lblSelectPotionTypeprocess
				.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectPotionTypeprocess.setBounds(12, 83, 221, 16);
		panel.add(lblSelectPotionTypeprocess);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				switch (processBox.getSelectedIndex()) {
				case 0:
					BrHerblorePro.fullProcess = true;
					break;
				case 1:
					BrHerblorePro.makeUnfinished = true;
					break;
				case 2:
					BrHerblorePro.makeFinished = true;
					break;
				}

				BrHerblorePro.potion = Potions.values()[potionBox
						.getSelectedIndex()];

				LogArea.log("Potion: " + BrHerblorePro.potion.getName(false));
				LogArea.log("Process: "
						+ (BrHerblorePro.fullProcess ? "Full process"
								: BrHerblorePro.makeUnfinished ? "Unfinished only"
										: BrHerblorePro.makeFinished ? "Finished only"
												: "Error"));

				BrHerblorePro.startScript = true;
				dispose();
			}
		});
		btnStart.setBounds(12, 219, 250, 25);
		contentPane.add(btnStart);
	}

	/**
	 * Sets up any lists needed for boxes
	 */
	private void setupLists() {
		for (int i = 0; i < potions.length; i++) {
			potions[i] = Potions.values()[i].getName(true);
		}
	}
}
