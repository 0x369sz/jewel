/*
 * Copyright (C) 2024 Slobodan Zivanovic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.slobodanzivanovic.jewel.laf.demo;

import com.slobodanzivanovic.jewel.laf.core.JewelDarkLaf;
import com.slobodanzivanovic.jewel.laf.core.JewelLightLaf;
import com.slobodanzivanovic.jewel.laf.core.util.UIScale;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

public class JewelLafDemo extends JFrame {
	private static final String PREFS_ROOT_PATH = "/demo";
	private static final String KEY_LAF = "laf";

	private final String title;
	private JewelInspector inspector;
	private JComboBox<LafInfo> lookAndFeelComboBox;
	private JCheckBox inspectCheckBox;
	private JButton closeButton;
	private JRootPane contentPanel;

	private JToolBar toolBar;
	private JPanel searchPanel;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JewelLafDemo demo = JewelLafDemo.create(args, "Jewel LaF Demo");
			demo.showFrame(demo.createContent());
		});
	}

	public static JewelLafDemo create(String[] args, String title) {
		Preferences prefs = Preferences.userRoot().node(PREFS_ROOT_PATH);

		try {
			if (args.length > 0) {
				UIManager.setLookAndFeel(args[0]);
			} else {
				String lafClassName = prefs.get(KEY_LAF, JewelLightLaf.class.getName());
				UIManager.setLookAndFeel(lafClassName);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JewelLightLaf.install();
		}

		return new JewelLafDemo(title);
	}

	private JewelLafDemo(String title) {
		this.title = title;
		initComponents();
		setupLookAndFeelComboBox();
		createMenuBar();
		createToolBar();

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				updateTitle();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				updateTitle();
			}
		});

		registerSwitchToLookAndFeel(KeyEvent.VK_F1, JewelLightLaf.class.getName());
		registerSwitchToLookAndFeel(KeyEvent.VK_F2, JewelDarkLaf.class.getName());
	}

	private void createToolBar() {
		toolBar = new JToolBar();
		toolBar.setFloatable(false);

		addToolBarButton("New", "FileChooser.newFolderIcon");
		addToolBarButton("Open", "Tree.openIcon");
		addToolBarButton("Save", "Tree.leafIcon");
		toolBar.addSeparator();
		addToolBarButton("Cut", "Tree.expandedIcon");
		addToolBarButton("Copy", "Tree.collapsedIcon");
		addToolBarButton("Paste", "Tree.computerIcon");
		toolBar.addSeparator();
		addToolBarButton("Undo", "Tree.closedIcon");
		addToolBarButton("Redo", "Tree.leafIcon");
		toolBar.addSeparator();
		addToolBarButton("Find", "Tree.expandedIcon");
		addToolBarButton("Replace", "Tree.collapsedIcon");
	}

	private void addToolBarButton(String text, String iconKey) {
		JButton button = new JButton(UIManager.getIcon(iconKey));
		button.setToolTipText(text);
		toolBar.add(button);
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		addMenuItem(fileMenu, "New File", 'N', KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		addMenuItem(fileMenu, "Open...", 'O', KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		fileMenu.addSeparator();
		addMenuItem(fileMenu, "Save", 'S', KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		addMenuItem(fileMenu, "Save As...", 'A', KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		fileMenu.addSeparator();
		addMenuItem(fileMenu, "Exit", 'x', KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));

		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic('E');
		addMenuItem(editMenu, "Undo", 'U', KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		addMenuItem(editMenu, "Redo", 'R', KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
		editMenu.addSeparator();
		addMenuItem(editMenu, "Cut", 't', KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		addMenuItem(editMenu, "Copy", 'C', KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		addMenuItem(editMenu, "Paste", 'P', KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));

		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic('V');
		addMenuItem(viewMenu, "Toggle Project Explorer", 'P', KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
		addMenuItem(viewMenu, "Toggle Console", 'C', KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_DOWN_MASK));
		viewMenu.addSeparator();
		JMenu themesMenu = new JMenu("Themes");
		addMenuItem(themesMenu, "Light Theme", 'L', KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		addMenuItem(themesMenu, "Dark Theme", 'D', KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		viewMenu.add(themesMenu);

		JMenu toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic('T');
		addMenuItem(toolsMenu, "Find in Files", 'F', KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		addMenuItem(toolsMenu, "Replace in Files", 'R', KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		toolsMenu.addSeparator();
		addMenuItem(toolsMenu, "Settings", 'S', KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, InputEvent.CTRL_DOWN_MASK));

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);
		menuBar.add(toolsMenu);

		setJMenuBar(menuBar);
	}

	private void addMenuItem(JMenu menu, String text, char mnemonic, KeyStroke accelerator) {
		JMenuItem menuItem = new JMenuItem(text);
		menuItem.setMnemonic(mnemonic);
		menuItem.setAccelerator(accelerator);
		menu.add(menuItem);
	}

	private JComponent createContent() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(toolBar, BorderLayout.NORTH);

		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainSplitPane.setDividerLocation(200);

		JPanel leftPanel = new JPanel(new BorderLayout());
		JTree fileTree = createFileTree();
		JPanel explorerHeader = new JPanel(new BorderLayout());
		explorerHeader.add(new JLabel(" Project Explorer", UIManager.getIcon("Tree.computerIcon"), JLabel.LEFT), BorderLayout.CENTER);
		leftPanel.add(explorerHeader, BorderLayout.NORTH);
		leftPanel.add(new JScrollPane(fileTree), BorderLayout.CENTER);
		mainSplitPane.setLeftComponent(leftPanel);

		JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		rightSplitPane.setDividerLocation(400);

		JTabbedPane editorTabs = createEditorTabs();
		rightSplitPane.setTopComponent(editorTabs);

		JTabbedPane bottomTabs = new JTabbedPane();

		JPanel consolePanel = new JPanel(new BorderLayout());
		JTextArea consoleOutput = new JTextArea();
		consoleOutput.setEditable(false);
		consoleOutput.setText("Console initialized...\nReady for output");
		consolePanel.add(new JScrollPane(consoleOutput), BorderLayout.CENTER);
		bottomTabs.addTab("Console", UIManager.getIcon("Tree.expandedIcon"), consolePanel);

		JPanel problemsPanel = new JPanel(new BorderLayout());
		JTable problemsTable = createProblemsTable();
		problemsPanel.add(new JScrollPane(problemsTable), BorderLayout.CENTER);
		bottomTabs.addTab("Problems", UIManager.getIcon("Tree.leafIcon"), problemsPanel);

		createSearchPanel();
		bottomTabs.addTab("Search", UIManager.getIcon("Tree.expandedIcon"), searchPanel);

		rightSplitPane.setBottomComponent(bottomTabs);
		mainSplitPane.setRightComponent(rightSplitPane);

		JSplitPane rightMostSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		rightMostSplit.setDividerLocation(600);
		rightMostSplit.setLeftComponent(mainSplitPane);

		JTabbedPane sidebarTabs = new JTabbedPane();

		JList<String> bookmarksList = new JList<>(new String[]{"Bookmark 1 - Line 15", "Bookmark 2 - Line 47"});
		sidebarTabs.addTab("Bookmarks", UIManager.getIcon("Tree.leafIcon"), new JScrollPane(bookmarksList));

		JList<String> todoList = new JList<>(new String[]{"TODO: Implement feature", "FIXME: Bug in parser"});
		sidebarTabs.addTab("TODOs", UIManager.getIcon("Tree.leafIcon"), new JScrollPane(todoList));

		rightMostSplit.setRightComponent(sidebarTabs);

		mainPanel.add(rightMostSplit, BorderLayout.CENTER);
		return mainPanel;
	}

	private void createSearchPanel() {
		searchPanel = new JPanel(new BorderLayout());
		JPanel searchControls = new JPanel(new MigLayout("insets 5", "[][grow][][]", "[][]"));

		JTextField searchField = new JTextField(20);
		JButton searchButton = new JButton("Search", UIManager.getIcon("Tree.expandedIcon"));
		JButton previousButton = new JButton("Previous", UIManager.getIcon("Tree.collapsedIcon"));
		JButton nextButton = new JButton("Next", UIManager.getIcon("Tree.expandedIcon"));
		JCheckBox matchCaseCheckBox = new JCheckBox("Match Case");
		JCheckBox wholeWordCheckBox = new JCheckBox("Whole Word");

		searchControls.add(new JLabel("Find:"));
		searchControls.add(searchField, "growx");
		searchControls.add(previousButton);
		searchControls.add(nextButton, "wrap");
		searchControls.add(matchCaseCheckBox);
		searchControls.add(wholeWordCheckBox);
		searchControls.add(searchButton);

		searchPanel.add(searchControls, BorderLayout.NORTH);
		searchPanel.add(new JScrollPane(new JTable(new Object[][]{}, new String[]{"File", "Line", "Match"})), BorderLayout.CENTER);
	}

	private JTree createFileTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Project");
		DefaultMutableTreeNode src = new DefaultMutableTreeNode("src");
		DefaultMutableTreeNode main = new DefaultMutableTreeNode("main");
		DefaultMutableTreeNode test = new DefaultMutableTreeNode("test");

		root.add(src);
		src.add(main);
		src.add(test);

		main.add(new DefaultMutableTreeNode("Main.java"));
		main.add(new DefaultMutableTreeNode("Utils.java"));
		test.add(new DefaultMutableTreeNode("TestSuite.java"));

		return new JTree(root);
	}

	private JTabbedPane createEditorTabs() {
		JTabbedPane tabs = new JTabbedPane();
		addEditorTab(tabs, "Main.java", "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}");
		addEditorTab(tabs, "Utils.java", "public class Utils {\n    public static String greeting() {\n        return \"Hello\";\n    }\n}");
		return tabs;
	}

	private void addEditorTab(JTabbedPane tabs, String title, String content) {
		JTextArea editor = new JTextArea();
		editor.setFont(new Font("Monospaced", Font.PLAIN, 12));
		editor.setText(content);
		tabs.addTab(title, UIManager.getIcon("Tree.leafIcon"), new JScrollPane(editor));
	}

	private JTable createProblemsTable() {
		String[] columnNames = {"Type", "Description", "File", "Line"};
		Object[][] data = {
			{"Warning", "Unused variable", "Main.java", "15"},
			{"Error", "Missing semicolon", "Utils.java", "23"}
		};
		return new JTable(data, columnNames);
	}

	private void registerSwitchToLookAndFeel(int keyCode, String lafClassName) {
		((JComponent) getContentPane()).registerKeyboardAction(
			e -> selectLookAndFeel(lafClassName),
			KeyStroke.getKeyStroke(keyCode, 0),
			JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
		);
	}

	private void selectLookAndFeel(String lafClassName) {
		DefaultComboBoxModel<LafInfo> lafModel = (DefaultComboBoxModel<LafInfo>) lookAndFeelComboBox.getModel();
		int sel = lafModel.getIndexOf(new LafInfo(null, lafClassName));
		if (sel >= 0) {
			lookAndFeelComboBox.setSelectedIndex(sel);
		}
	}

	private void setupLookAndFeelComboBox() {
		DefaultComboBoxModel<LafInfo> lafModel = new DefaultComboBoxModel<>();
		lafModel.addElement(new LafInfo("Jewel Light (F1)", JewelLightLaf.class.getName()));
		lafModel.addElement(new LafInfo("Jewel Dark (F2)", JewelDarkLaf.class.getName()));

		LookAndFeel activeLaf = UIManager.getLookAndFeel();
		String activeLafClassName = activeLaf.getClass().getName();
		int sel = lafModel.getIndexOf(new LafInfo(null, activeLafClassName));
		if (sel < 0) {
			lafModel.addElement(new LafInfo(activeLaf.getName(), activeLafClassName));
			sel = lafModel.getSize() - 1;
		}
		lookAndFeelComboBox.setModel(lafModel);
		lookAndFeelComboBox.setSelectedItem(lafModel.getElementAt(sel));
	}

	private void updateTitle() {
		double systemScaleFactor = UIScale.getSystemScaleFactor(getGraphicsConfiguration());
		float userScaleFactor = UIScale.getUserScaleFactor();
		String newTitle = String.format("%s (Java %s%s%s%s)",
			title,
			System.getProperty("java.version"),
			systemScaleFactor != 1 ? String.format(";  system scale factor %.2f", systemScaleFactor) : "",
			userScaleFactor != 1 ? String.format(";  user scale factor %.2f", userScaleFactor) : "",
			systemScaleFactor == 1 && userScaleFactor == 1 ? "; no scaling" : ""
		);

		if (!newTitle.equals(getTitle())) {
			setTitle(newTitle);
		}
	}

	private void initComponents() {
		JPanel dialogPane = new JPanel();
		contentPanel = new JRootPane();
		JPanel buttonBar = new JPanel();
		lookAndFeelComboBox = new JComboBox<>();
		inspectCheckBox = new JCheckBox();
		closeButton = new JButton();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		dialogPane.setLayout(new BorderLayout());

		Container contentPanelContentPane = contentPanel.getContentPane();
		contentPanelContentPane.setLayout(new MigLayout("insets dialog,hidemode 3",
			"[grow,fill]",
			"[grow,fill]"));
		dialogPane.add(contentPanel, BorderLayout.CENTER);

		buttonBar.setLayout(new MigLayout("insets dialog",
			"[fill][fill][grow,fill][button,fill]",
			null));

		lookAndFeelComboBox.addActionListener(e -> lookAndFeelChanged());
		buttonBar.add(lookAndFeelComboBox, "cell 0 0");

		inspectCheckBox.setText("inspect");
		inspectCheckBox.setMnemonic('I');
		inspectCheckBox.addActionListener(e -> inspectChanged());
		buttonBar.add(inspectCheckBox, "cell 1 0");

		closeButton.setText("Close");
		closeButton.addActionListener(e -> dispose());
		buttonBar.add(closeButton, "cell 3 0");

		dialogPane.add(buttonBar, BorderLayout.SOUTH);
		contentPane.add(dialogPane, BorderLayout.CENTER);

		((JComponent) getContentPane()).registerKeyboardAction(
			e -> dispose(),
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
		);
	}

	private void lookAndFeelChanged() {
		LafInfo newLaf = (LafInfo) lookAndFeelComboBox.getSelectedItem();
		if (newLaf == null) return;

		if (newLaf.className.equals(UIManager.getLookAndFeel().getClass().getName())) return;

		lookAndFeelComboBox.setPopupVisible(false);
		Preferences.userRoot().node(PREFS_ROOT_PATH).put(KEY_LAF, newLaf.className);

		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(newLaf.className);
				SwingUtilities.updateComponentTreeUI(this);
				pack();

				if (inspector != null) {
					inspector.update();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	private void inspectChanged() {
		if (inspector == null) {
			inspector = new JewelInspector(contentPanel);
		}
		inspector.setEnabled(inspectCheckBox.isSelected());
	}

	public void showFrame(JComponent content) {
		contentPanel.getContentPane().add(content);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		EventQueue.invokeLater(() -> closeButton.requestFocusInWindow());
	}

	record LafInfo(String name, String className) {
		@Override
		public boolean equals(Object obj) {
			return obj instanceof LafInfo && className.equals(((LafInfo) obj).className);
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
