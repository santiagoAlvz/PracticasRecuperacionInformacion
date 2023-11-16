package practica5;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JInternalFrame;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MainWindow {

	private JFrame frame;
	private JTextField entEpTitle;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel pnlEpFilters = new JPanel();
		pnlEpFilters.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Episode Filters", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		frame.getContentPane().add(pnlEpFilters);
		pnlEpFilters.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_1 = new JPanel();
		pnlEpFilters.add(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] {128, 256, 0};
		gbl_panel_1.rowHeights = new int[]{26, 26, 26, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblEpTitle = new JLabel("Title");
		lblEpTitle.setLabelFor(lblEpTitle);
		GridBagConstraints gbc_lblEpTitle = new GridBagConstraints();
		gbc_lblEpTitle.fill = GridBagConstraints.BOTH;
		gbc_lblEpTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblEpTitle.gridx = 0;
		gbc_lblEpTitle.gridy = 0;
		panel_1.add(lblEpTitle, gbc_lblEpTitle);
		
		entEpTitle = new JTextField();
		GridBagConstraints gbc_entEpTitle = new GridBagConstraints();
		gbc_entEpTitle.fill = GridBagConstraints.BOTH;
		gbc_entEpTitle.insets = new Insets(0, 0, 5, 0);
		gbc_entEpTitle.gridx = 1;
		gbc_entEpTitle.gridy = 0;
		panel_1.add(entEpTitle, gbc_entEpTitle);
		entEpTitle.setColumns(10);
		
		JLabel lblEpSeason = new JLabel("EpisodeSeason");
		GridBagConstraints gbc_lblEpSeason = new GridBagConstraints();
		gbc_lblEpSeason.fill = GridBagConstraints.BOTH;
		gbc_lblEpSeason.insets = new Insets(0, 0, 5, 5);
		gbc_lblEpSeason.gridx = 0;
		gbc_lblEpSeason.gridy = 1;
		panel_1.add(lblEpSeason, gbc_lblEpSeason);
		
		JPanel pnlEpSeason = new JPanel();
		GridBagConstraints gbc_pnlEpSeason = new GridBagConstraints();
		gbc_pnlEpSeason.fill = GridBagConstraints.BOTH;
		gbc_pnlEpSeason.insets = new Insets(0, 0, 5, 0);
		gbc_pnlEpSeason.gridx = 1;
		gbc_pnlEpSeason.gridy = 1;
		panel_1.add(pnlEpSeason, gbc_pnlEpSeason);
		
		JComboBox comEpSeason = new JComboBox();
		comEpSeason.setModel(new DefaultComboBoxModel(new String[] {">", "<", "="}));
		
		JSpinner spnEpSeason = new JSpinner();
		pnlEpSeason.setLayout(new GridLayout(0, 2, 5, 0));
		pnlEpSeason.add(comEpSeason);
		pnlEpSeason.add(spnEpSeason);
		
		JLabel lblEpRating = new JLabel("Episode Rating (IMDB)");
		GridBagConstraints gbc_lblEpRating = new GridBagConstraints();
		gbc_lblEpRating.fill = GridBagConstraints.BOTH;
		gbc_lblEpRating.insets = new Insets(0, 0, 0, 5);
		gbc_lblEpRating.gridx = 0;
		gbc_lblEpRating.gridy = 2;
		panel_1.add(lblEpRating, gbc_lblEpRating);
		
		JPanel pnlEpRating = new JPanel();
		GridBagConstraints gbc_pnlEpRating = new GridBagConstraints();
		gbc_pnlEpRating.fill = GridBagConstraints.BOTH;
		gbc_pnlEpRating.gridx = 1;
		gbc_pnlEpRating.gridy = 2;
		panel_1.add(pnlEpRating, gbc_pnlEpRating);
		pnlEpRating.setLayout(new BoxLayout(pnlEpRating, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel(">");
		pnlEpRating.add(lblNewLabel);
		
		JSpinner spnEpRating = new JSpinner();
		pnlEpRating.add(spnEpRating);
		spnEpRating.setModel(new SpinnerNumberModel(Float.valueOf(0), Float.valueOf(0), Float.valueOf(100), Float.valueOf(0)));
		
		JPanel pnlLineFilters = new JPanel();
		pnlLineFilters.setBorder(new TitledBorder(null, "Line Filters", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(pnlLineFilters);
		
		JPanel panel = new JPanel();
		pnlLineFilters.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {128, 256, 0};
		gbl_panel.rowHeights = new int[] {26, 26, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("New label");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		panel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 0;
		gbc_textField_1.gridy = 3;
		panel.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
	}

}
