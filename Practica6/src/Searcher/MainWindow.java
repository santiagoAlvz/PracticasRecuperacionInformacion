package Searcher;

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
import javax.swing.DefaultListModel;
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
import java.awt.Checkbox;

import javax.swing.border.EmptyBorder;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.Action;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.lucene.facet.LabelAndValue;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class MainWindow {

	private JFrame frmSearcher;
	private JTextField txtEpTitle;
	private JTextField txtLineWords;
	private JSpinner spnEpRating;
	private JTree treeResults;
	private DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("Root");
	private DefaultTreeModel resultsTreeModel = new DefaultTreeModel(treeRoot);
	private IndexSearcher is = new IndexSearcher();
	private final Action action = new searchIndex();
	private JTextField txtEpGeneric;
	private JCheckBox checkBox_episode;
	private JSpinner numberEpisode;
	private JLabel lblResults;
	private DefaultListModel<String> lstEpYearsModel; 
	private JButton btnApplyFilters;
	private JList<String> lstEpYears;
	private LabelAndValue[] episodeYears, lineCharacters;
	private SearchParameters sp = new SearchParameters();
	private SearchTypes searchType;
	private final Action action_1 = new SwingAction();
	private JComboBox cmbLineCharacter;
	private DefaultComboBoxModel<String> cmbLineCharacterModel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmSearcher.setVisible(true);
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
		frmSearcher = new JFrame();
		frmSearcher.setTitle("Searcher");
		frmSearcher.setBounds(100, 100, 902, 618);
		frmSearcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSearcher.getContentPane().setLayout(new BoxLayout(frmSearcher.getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel topPanel = new JPanel();
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		frmSearcher.getContentPane().add(topPanel);
		GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.columnWidths = new int[]{872, 0};
		gbl_topPanel.rowHeights = new int[]{30, 0, 116, 27, 0, 17, 253, 0};
		gbl_topPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_topPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		topPanel.setLayout(gbl_topPanel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_2.anchor = GridBagConstraints.SOUTH;
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		topPanel.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		JLabel lblGenericSearch = new JLabel("Generic Episode Search");
		panel_2.add(lblGenericSearch);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel_2.add(horizontalStrut);
		
		txtEpGeneric = new JTextField();
		panel_2.add(txtEpGeneric);
		txtEpGeneric.setColumns(10);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_1.gridx = 0;
		gbc_verticalStrut_1.gridy = 1;
		topPanel.add(verticalStrut_1, gbc_verticalStrut_1);
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 2;
		topPanel.add(panel_3, gbc_panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		
		JPanel pnlEpFilters = new JPanel();
		panel_3.add(pnlEpFilters);
		pnlEpFilters.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Episode Filters", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		pnlEpFilters.setLayout(new BoxLayout(pnlEpFilters, BoxLayout.X_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] {160, 256, 0};
		gbl_panel_1.rowHeights = new int[] {26, 26, 26, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblEpTitle = new JLabel("Title");
		lblEpTitle.setLabelFor(lblEpTitle);
		GridBagConstraints gbc_lblEpTitle = new GridBagConstraints();
		gbc_lblEpTitle.fill = GridBagConstraints.BOTH;
		gbc_lblEpTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblEpTitle.gridx = 0;
		gbc_lblEpTitle.gridy = 0;
		panel_1.add(lblEpTitle, gbc_lblEpTitle);
		
		txtEpTitle = new JTextField();
		GridBagConstraints gbc_txtEpTitle = new GridBagConstraints();
		gbc_txtEpTitle.fill = GridBagConstraints.BOTH;
		gbc_txtEpTitle.insets = new Insets(0, 0, 5, 0);
		gbc_txtEpTitle.gridx = 1;
		gbc_txtEpTitle.gridy = 0;
		panel_1.add(txtEpTitle, gbc_txtEpTitle);
		txtEpTitle.setColumns(10);
		
		JLabel lblNumberInSeason = new JLabel("Episode");
		GridBagConstraints gbc_lblNumberInSeason = new GridBagConstraints();
		gbc_lblNumberInSeason.anchor = GridBagConstraints.WEST;
		gbc_lblNumberInSeason.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberInSeason.gridx = 0;
		gbc_lblNumberInSeason.gridy = 1;
		panel_1.add(lblNumberInSeason, gbc_lblNumberInSeason);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_4.gridx = 1;
		gbc_panel_4.gridy = 1;
		panel_1.add(panel_4, gbc_panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));
		
		checkBox_episode = new JCheckBox("");
		panel_4.add(checkBox_episode);
		
		numberEpisode = new JSpinner();
		panel_4.add(numberEpisode);
		
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
		
		spnEpRating = new JSpinner();
		pnlEpRating.add(spnEpRating);
		spnEpRating.setModel(new SpinnerNumberModel(Float.valueOf(0), Float.valueOf(0), Float.valueOf(10), Float.valueOf(0.1f)));
		pnlEpFilters.add(panel_1);
		
		JLabel lblEpRating = new JLabel("Episode Rating (IMDB)");
		GridBagConstraints gbc_lblEpRating = new GridBagConstraints();
		gbc_lblEpRating.fill = GridBagConstraints.BOTH;
		gbc_lblEpRating.insets = new Insets(0, 0, 0, 5);
		gbc_lblEpRating.gridx = 0;
		gbc_lblEpRating.gridy = 2;
		panel_1.add(lblEpRating, gbc_lblEpRating);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(10);
		panel_3.add(horizontalStrut_1);
		
		JPanel pnlLineFilters = new JPanel();
		panel_3.add(pnlLineFilters);
		pnlLineFilters.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Line Filters", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		pnlLineFilters.setLayout(new BoxLayout(pnlLineFilters, BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {128, 256, 0};
		gbl_panel.rowHeights = new int[] {26, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblLineWords = new JLabel("Spoken Words");
		GridBagConstraints gbc_lblLineWords = new GridBagConstraints();
		gbc_lblLineWords.fill = GridBagConstraints.BOTH;
		gbc_lblLineWords.insets = new Insets(0, 0, 0, 5);
		gbc_lblLineWords.gridx = 0;
		gbc_lblLineWords.gridy = 0;
		panel.add(lblLineWords, gbc_lblLineWords);
		
		txtLineWords = new JTextField();
		GridBagConstraints gbc_txtLineWords = new GridBagConstraints();
		gbc_txtLineWords.fill = GridBagConstraints.BOTH;
		gbc_txtLineWords.gridx = 1;
		gbc_txtLineWords.gridy = 0;
		panel.add(txtLineWords, gbc_txtLineWords);
		txtLineWords.setColumns(10);
		pnlLineFilters.add(panel);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setAction(action);
		btnSearch.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearch.gridx = 0;
		gbc_btnSearch.gridy = 3;
		topPanel.add(btnSearch, gbc_btnSearch);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut.gridx = 0;
		gbc_verticalStrut.gridy = 4;
		topPanel.add(verticalStrut, gbc_verticalStrut);
		
		lblResults = new JLabel("Results");
		lblResults.setAlignmentY(0.0f);
		lblResults.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblResults = new GridBagConstraints();
		gbc_lblResults.insets = new Insets(0, 0, 5, 0);
		gbc_lblResults.gridx = 0;
		gbc_lblResults.gridy = 5;
		topPanel.add(lblResults, gbc_lblResults);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 6;
		topPanel.add(scrollPane, gbc_scrollPane);
		
		JPanel panel_5 = new JPanel();
		scrollPane.setViewportView(panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{243, -551, 0};
		gbl_panel_5.rowHeights = new int[]{303, 0};
		gbl_panel_5.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		cmbLineCharacterModel = new DefaultComboBoxModel();
		
		lstEpYearsModel = new DefaultListModel<>();
		
		JPanel panel_6 = new JPanel();
		panel_6.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_6.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.fill = GridBagConstraints.VERTICAL;
		gbc_panel_6.insets = new Insets(0, 0, 0, 5);
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 0;
		panel_5.add(panel_6, gbc_panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[]{118, 162, 0};
		gbl_panel_6.rowHeights = new int[] {26, 26, 26, 26, 26, 0};
		gbl_panel_6.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		JLabel lblCharacter = new JLabel("Character");
		GridBagConstraints gbc_lblCharacter = new GridBagConstraints();
		gbc_lblCharacter.fill = GridBagConstraints.BOTH;
		gbc_lblCharacter.insets = new Insets(0, 0, 5, 5);
		gbc_lblCharacter.gridx = 0;
		gbc_lblCharacter.gridy = 0;
		panel_6.add(lblCharacter, gbc_lblCharacter);
		
		cmbLineCharacter = new JComboBox();
		cmbLineCharacter.setModel(cmbLineCharacterModel);
		GridBagConstraints gbc_cmbLineCharacter = new GridBagConstraints();
		gbc_cmbLineCharacter.fill = GridBagConstraints.BOTH;
		gbc_cmbLineCharacter.insets = new Insets(0, 0, 5, 0);
		gbc_cmbLineCharacter.gridx = 1;
		gbc_cmbLineCharacter.gridy = 0;
		panel_6.add(cmbLineCharacter, gbc_cmbLineCharacter);
		
		JLabel lblNewLabel_1 = new JLabel("Min Season");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		panel_6.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		JSpinner spinner = new JSpinner();
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.fill = GridBagConstraints.BOTH;
		gbc_spinner.insets = new Insets(0, 0, 5, 0);
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 1;
		panel_6.add(spinner, gbc_spinner);
		
		JLabel lblNewLabel_2 = new JLabel("Max. Season");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		panel_6.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		JSpinner spinner_1 = new JSpinner();
		GridBagConstraints gbc_spinner_1 = new GridBagConstraints();
		gbc_spinner_1.fill = GridBagConstraints.BOTH;
		gbc_spinner_1.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_1.gridx = 1;
		gbc_spinner_1.gridy = 2;
		panel_6.add(spinner_1, gbc_spinner_1);
		
		JLabel lblYear = new JLabel("Year");
		GridBagConstraints gbc_lblYear = new GridBagConstraints();
		gbc_lblYear.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblYear.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblYear.insets = new Insets(0, 0, 5, 5);
		gbc_lblYear.gridx = 0;
		gbc_lblYear.gridy = 3;
		panel_6.add(lblYear, gbc_lblYear);
		lstEpYears = new JList<String>();
		lstEpYears.setModel(lstEpYearsModel);
		GridBagConstraints gbc_lstEpYears = new GridBagConstraints();
		gbc_lstEpYears.insets = new Insets(0, 0, 5, 0);
		gbc_lstEpYears.fill = GridBagConstraints.BOTH;
		gbc_lstEpYears.gridx = 1;
		gbc_lstEpYears.gridy = 3;
		panel_6.add(lstEpYears, gbc_lstEpYears);
		
		btnApplyFilters = new JButton("Apply Filters");
		btnApplyFilters.setAction(action_1);
		btnApplyFilters.setEnabled(false);
		GridBagConstraints gbc_btnApplyFilters = new GridBagConstraints();
		gbc_btnApplyFilters.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnApplyFilters.gridwidth = 2;
		gbc_btnApplyFilters.insets = new Insets(0, 0, 0, 5);
		gbc_btnApplyFilters.gridx = 0;
		gbc_btnApplyFilters.gridy = 4;
		panel_6.add(btnApplyFilters, gbc_btnApplyFilters);
		
		//JList<? extends E> list = new JList();
		//panel_5.add(list);
		
		treeResults = new JTree();
		treeResults.setAlignmentY(Component.TOP_ALIGNMENT);
		GridBagConstraints gbc_treeResults = new GridBagConstraints();
		gbc_treeResults.fill = GridBagConstraints.BOTH;
		gbc_treeResults.gridx = 1;
		gbc_treeResults.gridy = 0;
		panel_5.add(treeResults, gbc_treeResults);
		treeResults.setModel(resultsTreeModel);
		treeResults.setRootVisible(false);
	}
	private class searchIndex extends AbstractAction {
		private static final long serialVersionUID = 8374267074142991082L;
		
		public searchIndex() {
			putValue(NAME, "Search Index");
			putValue(SHORT_DESCRIPTION, "Search for matches in the indexed information");
		}
		
		/**
		 * Reads all information from UI fields, adds them to a SearchParameters object and 
		 * launches an index search
		 */
		public void actionPerformed(ActionEvent e) {
			sp = new SearchParameters();
			
			sp.addFilter(FilterFields.EPISODE_GENERIC, txtEpGeneric.getText());
			System.out.println(txtEpGeneric.getText());
			
			sp.addFilter(FilterFields.EPISODE_TITLE, txtEpTitle.getText());
			
			sp.addFilter(FilterFields.EPISODE_TITLE, txtEpTitle.getText());
			
			if (checkBox_episode.isSelected()) {
				sp.addFilter(FilterFields.EPISODE, numberEpisode.getValue().toString());
			}
			
			sp.addFilter(FilterFields.EPISODE_RATING_GREATER_THAN, spnEpRating.getValue().toString());
			
			sp.addFilter(FilterFields.LINE_SPOKEN_WORDS, txtLineWords.getText());
			
			if (!txtEpGeneric.getText().isEmpty()) {
//				if we have changes generic query we use episodes and lines
				System.out.println("search Episodes and Lines");
				searchType = SearchTypes.EPISODES_AND_LINES;
			}			
			
			else if (txtLineWords.getText().isEmpty()) {
//				if nothing changed in Line Filter we will search the episodes
				System.out.println("search Episodes");
				searchType = SearchTypes.EPISODES_ONLY;
			}
			
			else if (txtEpTitle.getText().isEmpty() &&
					Float.valueOf(0).equals(spnEpRating.getValue()) &&
					!checkBox_episode.isSelected()) {
//				if nothing changed in episode Filter we will search the lines first
				System.out.println("search Lines");
				searchType = SearchTypes.LINES_ONLY;
			}
			
			else {
//				if we have changes in Episode and Line Filters we will search episodes and lines
				System.out.println("search Episodes and Lines");
				searchType = SearchTypes.EPISODES_AND_LINES;
			}	
			
			searchIndex();					
		}
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Apply Filters");
			putValue(SHORT_DESCRIPTION, "Limit results by categories");
			
			//lst
		}
	public void actionPerformed(ActionEvent e) {
		int selectedYears[] = lstEpYears.getSelectedIndices();
		
		for(int index: selectedYears) {
			sp.addFacetFilter(FacetFilters.EPISODE_YEAR, episodeYears[index]);
		}
		
		if(cmbLineCharacter.getSelectedIndex() != 0) {
			sp.addFacetFilter(FacetFilters.LINE_CHARACTER, lineCharacters[cmbLineCharacter.getSelectedIndex() - 1]);
		}
		
		searchIndex();
		}
	}

	private void searchIndex() {
		LinkedHashMap<String,ArrayList<String>> results;

		switch(searchType) {
		case EPISODES_AND_LINES:
			results = is.search(sp);
			cmbLineCharacter.setEnabled(true);
			break;
		case EPISODES_ONLY:
			results = is.searchEpisodes(sp);
			cmbLineCharacter.setEnabled(false);
			break;
		case LINES_ONLY:
			results = is.searchLines(sp);
			cmbLineCharacter.setEnabled(true);
			break;
		default:
			results = new LinkedHashMap<String,ArrayList<String>>();
			break;	
		}
		
		treeRoot.removeAllChildren();
		
		int episodeCount = 0, lineCount = 0;
		ArrayList<String> lines;
		
		for(String episode: results.keySet()) {
			DefaultMutableTreeNode episodeNode = new DefaultMutableTreeNode(episode);
			treeRoot.add(episodeNode);
			episodeCount++;
							
			lines = results.get(episode);
			if(lines != null) for(String line: lines) {
				episodeNode.add(new DefaultMutableTreeNode(line));
		        lineCount++;
			}
		}
		
		lblResults.setText("Results ("+episodeCount+" episodes, "+lineCount+" lines)");
		
		lstEpYearsModel.clear();
		cmbLineCharacterModel.removeAllElements();
		
		episodeYears = is.getResultYearsFacets();
		for(LabelAndValue f: episodeYears) {
			lstEpYearsModel.addElement("" + f);
		}					
		
		lineCharacters = is.getResultCharactersFacets();
		for(LabelAndValue f: lineCharacters) {
			cmbLineCharacterModel.addElement("" + f);
		}
		
		cmbLineCharacterModel.insertElementAt("All Characters ("+lineCount+")", 0);
		if(lineCharacters.length != 1) {
			cmbLineCharacter.setSelectedIndex(0);
		} else {
			cmbLineCharacter.setSelectedIndex(1);
		}
		
		resultsTreeModel.reload(treeRoot);
		btnApplyFilters.setEnabled(true);
	}
}
