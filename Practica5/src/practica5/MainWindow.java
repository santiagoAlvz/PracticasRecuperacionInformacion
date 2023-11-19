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
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.SpringLayout;
import javax.swing.JTree;
import javax.swing.JSplitPane;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.Box;
import java.awt.CardLayout;
import javax.swing.border.EmptyBorder;

public class MainWindow {

	private JFrame frame;
	private JTextField entEpTitle;
	private JTextField txtLineCharacter;
	private JTextField txtLineWords;

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
		frame.setBounds(100, 100, 478, 363);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel topPanel = new JPanel();
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.getContentPane().add(topPanel);
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		JPanel pnlEpFilters = new JPanel();
		pnlEpFilters.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Episode Filters", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		
		JPanel panel_1 = new JPanel();
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] {160, 256, 0};
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
		pnlEpRating.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblNewLabel = new JLabel(">");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pnlEpRating.add(lblNewLabel);
		
		JSpinner spnEpRating = new JSpinner();
		pnlEpRating.add(spnEpRating);
		spnEpRating.setModel(new SpinnerNumberModel(Float.valueOf(0), Float.valueOf(0), Float.valueOf(100), Float.valueOf(0)));
		GroupLayout gl_pnlEpFilters = new GroupLayout(pnlEpFilters);
		gl_pnlEpFilters.setHorizontalGroup(
			gl_pnlEpFilters.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlEpFilters.createSequentialGroup()
					.addGap(18)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		gl_pnlEpFilters.setVerticalGroup(
			gl_pnlEpFilters.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlEpFilters.createSequentialGroup()
					.addGap(5)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		pnlEpFilters.setLayout(gl_pnlEpFilters);
		topPanel.add(pnlEpFilters);
		
		JPanel pnlLineFilters = new JPanel();
		pnlLineFilters.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Line Filters", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		
		JPanel panel = new JPanel();
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {160, 256, 0};
		gbl_panel.rowHeights = new int[] {26, 26, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblLineCharacter = new JLabel("Character");
		GridBagConstraints gbc_lblLineCharacter = new GridBagConstraints();
		gbc_lblLineCharacter.fill = GridBagConstraints.BOTH;
		gbc_lblLineCharacter.insets = new Insets(0, 0, 5, 5);
		gbc_lblLineCharacter.gridx = 0;
		gbc_lblLineCharacter.gridy = 0;
		panel.add(lblLineCharacter, gbc_lblLineCharacter);
		
		txtLineCharacter = new JTextField();
		GridBagConstraints gbc_txtLineCharacter = new GridBagConstraints();
		gbc_txtLineCharacter.fill = GridBagConstraints.BOTH;
		gbc_txtLineCharacter.insets = new Insets(0, 0, 5, 0);
		gbc_txtLineCharacter.gridx = 1;
		gbc_txtLineCharacter.gridy = 0;
		panel.add(txtLineCharacter, gbc_txtLineCharacter);
		txtLineCharacter.setColumns(10);
		
		JLabel lblLineWords = new JLabel("New label");
		GridBagConstraints gbc_lblLineWords = new GridBagConstraints();
		gbc_lblLineWords.fill = GridBagConstraints.BOTH;
		gbc_lblLineWords.insets = new Insets(0, 0, 0, 5);
		gbc_lblLineWords.gridx = 0;
		gbc_lblLineWords.gridy = 1;
		panel.add(lblLineWords, gbc_lblLineWords);
		
		txtLineWords = new JTextField();
		GridBagConstraints gbc_txtLineWords = new GridBagConstraints();
		gbc_txtLineWords.fill = GridBagConstraints.BOTH;
		gbc_txtLineWords.gridx = 1;
		gbc_txtLineWords.gridy = 1;
		panel.add(txtLineWords, gbc_txtLineWords);
		txtLineWords.setColumns(10);
		GroupLayout gl_pnlLineFilters = new GroupLayout(pnlLineFilters);
		gl_pnlLineFilters.setHorizontalGroup(
			gl_pnlLineFilters.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLineFilters.createSequentialGroup()
					.addGap(23)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		gl_pnlLineFilters.setVerticalGroup(
			gl_pnlLineFilters.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLineFilters.createSequentialGroup()
					.addGap(5)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		pnlLineFilters.setLayout(gl_pnlLineFilters);
		topPanel.add(pnlLineFilters);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setAlignmentX(Component.CENTER_ALIGNMENT);
		topPanel.add(btnSearch);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		topPanel.add(verticalStrut);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JTree treeResults = new JTree();
		treeResults.setRootVisible(false);
		scrollPane.setViewportView(treeResults);
		topPanel.add(scrollPane);
	}
}
