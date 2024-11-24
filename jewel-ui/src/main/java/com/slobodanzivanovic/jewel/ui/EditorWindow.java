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

package com.slobodanzivanovic.jewel.ui;

import com.slobodanzivanovic.jewel.ui.components.CustomFilePicker;
import com.slobodanzivanovic.jewel.ui.components.StatusBar;
import com.slobodanzivanovic.jewel.ui.components.TextArea;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditorWindow extends JPanel implements Runnable {
	private static final double SCREEN_WIDTH_RATIO = 0.55;
	private static final double SCREEN_HEIGHT_RATIO = 0.9;
	private static final int STATUS_BAR_HEIGHT = 25;
	private static final int TARGET_FPS = 120;
	private static final long FRAME_TIME = 1000000000 / TARGET_FPS;

	private final int screenWidth;
	private final int screenHeight;
	private JTree fileTree;
	private DefaultTreeModel treeModel;
	private JTabbedPane tabbedPane;
	private StatusBar statusBar;
	private final List<TextArea> textAreas;
	private int tabCounter = 1;

	private Thread editorThread;
	private volatile boolean running = false;
	private volatile boolean needsUpdate = false;
	private static long lastFpsCheck = 0;
	private static int currentFps = 0;
	private static int totalFrames = 0;

	public EditorWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) (screenSize.width * SCREEN_WIDTH_RATIO);
		screenHeight = (int) (screenSize.height * SCREEN_HEIGHT_RATIO);

		textAreas = new ArrayList<>();
		initializeUI();
	}

	private void initializeUI() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setLayout(new BorderLayout());

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		treeModel = new DefaultTreeModel(root);
		fileTree = new JTree(treeModel);
		fileTree.setRootVisible(false);
		fileTree.setShowsRootHandles(true);
		fileTree.setCellRenderer(new FileTreeCellRenderer());

		JScrollPane treeScrollPane = new JScrollPane(fileTree);
		treeScrollPane.setPreferredSize(new Dimension(200, screenHeight));

		tabbedPane = new JTabbedPane();
		statusBar = new StatusBar();

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, tabbedPane);
		mainSplitPane.setDividerLocation(200);

		add(mainSplitPane, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);

		statusBar.setPreferredSize(new Dimension(screenWidth, STATUS_BAR_HEIGHT));

		createNewTab();

		tabbedPane.addChangeListener(this::handleTabChange);

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");

		JMenuItem openFileMenuItem = new JMenuItem("Open File");
		int openFileModifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
		openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, openFileModifier));
		openFileMenuItem.addActionListener(this::openFile);

		JMenuItem openFolderMenuItem = new JMenuItem("Open Folder");
		int openFolderModifier = openFileModifier;
		openFolderModifier |= InputEvent.SHIFT_DOWN_MASK;
		openFolderMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, openFolderModifier));
		openFolderMenuItem.addActionListener(this::openFolder);

		fileMenu.add(openFileMenuItem);
		fileMenu.add(openFolderMenuItem);
		menuBar.add(fileMenu);

		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = getActionMap();

		KeyStroke openFileKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, openFileModifier);
		inputMap.put(openFileKeyStroke, "openFile");
		actionMap.put("openFile", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(() -> openFile(e));
			}
		});

		JMenuItem newTabMenuItem = new JMenuItem("New Tab");
		int newTabModifier = InputEvent.META_DOWN_MASK;
		newTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, newTabModifier));
		newTabMenuItem.addActionListener(e -> createNewTab());
		fileMenu.add(newTabMenuItem);

		KeyStroke newTabKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_T, newTabModifier);
		inputMap.put(newTabKeyStroke, "newTab");
		actionMap.put("newTab", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(() -> createNewTab());
			}
		});

		JMenuItem closeTabMenuItem = new JMenuItem("Close Tab");
		int closeTabModifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
		closeTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, closeTabModifier));
		closeTabMenuItem.addActionListener(e -> closeCurrentTab());
		fileMenu.add(closeTabMenuItem);

		KeyStroke closeTabKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, closeTabModifier);
		inputMap.put(closeTabKeyStroke, "closeTab");
		actionMap.put("closeTab", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(() -> closeCurrentTab());
			}
		});

		KeyStroke openFolderKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, openFolderModifier);
		inputMap.put(openFolderKeyStroke, "openFolder");
		actionMap.put("openFolder", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(() -> openFolder(e));
			}
		});

		SwingUtilities.invokeLater(() -> {
			Container parent = getParent();
			while (parent != null && !(parent instanceof JFrame)) {
				parent = parent.getParent();
			}
			if (parent != null) {
				((JFrame) parent).setJMenuBar(menuBar);
			}
		});

		fileTree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					TreePath path = fileTree.getPathForLocation(e.getX(), e.getY());
					if (path != null) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
						if (node.getUserObject() instanceof FileNode(File file)) {
							if (file.isFile()) {
								openFileFromTree(file);
							}
						}
					}
				}
			}
		});

		SwingUtilities.invokeLater(() -> getCurrentTextArea().getTextArea().requestFocusInWindow());
	}

	private void openFolder(ActionEvent e) {
		SwingUtilities.invokeLater(() -> {
			CustomFilePicker filePicker = new CustomFilePicker((Frame) SwingUtilities.getWindowAncestor(this), "Open Folder");
			filePicker.setFolderSelectionMode(true);
			filePicker.setVisible(true);

			if (filePicker.isApproved()) {
				File selectedFolder = filePicker.getSelectedFile();
				if (selectedFolder != null && selectedFolder.isDirectory()) {
					loadFolderIntoTree(selectedFolder);
				}
			}
		});
	}

	private void loadFolderIntoTree(File folder) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new FileNode(folder));
		treeModel.setRoot(root);
		loadChildren(root);
		fileTree.setRootVisible(true);
	}

	private void loadChildren(DefaultMutableTreeNode node) {
		FileNode fileNode = (FileNode) node.getUserObject();
		File[] files = fileNode.file().listFiles();
		if (files != null) {
			Arrays.sort(files, (f1, f2) -> {
				if (f1.isDirectory() && !f2.isDirectory()) return -1;
				if (!f1.isDirectory() && f2.isDirectory()) return 1;
				return f1.getName().compareToIgnoreCase(f2.getName());
			});

			for (File file : files) {
				if (!file.isHidden()) {
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileNode(file));
					node.add(childNode);
					if (file.isDirectory()) {
						loadChildren(childNode);
					}
				}
			}
		}
	}

	private void openFileFromTree(File file) {
		openFileFromPath(file);
	}

	private void openFile(ActionEvent e) {
		CustomFilePicker filePicker = new CustomFilePicker((Frame) SwingUtilities.getWindowAncestor(this), "Open File");
		filePicker.setFolderSelectionMode(false);
		filePicker.setVisible(true);

		if (filePicker.isApproved()) {
			File selectedFile = filePicker.getSelectedFile();
			if (selectedFile != null && selectedFile.isFile()) {
				openFileFromPath(selectedFile);
			}
		}
	}

	private void openFileFromPath(File file) {
		try {
			String content = Files.readString(file.toPath());
			TextArea textArea = getCurrentTextArea();
			if (textArea == null || !textArea.getTextArea().getText().isEmpty()) {
				createNewTab();
				textArea = getCurrentTextArea();
			}
			textArea.getTextArea().setText(content);
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), file.getName());
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Error reading file: "
				+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void createNewTab() {
		TextArea textArea = new TextArea();
		textAreas.add(textArea);

		String title = "Untitled-" + tabCounter++;
		tabbedPane.addTab(title, textArea);

		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

		setupTextAreaListeners(textArea);

		SwingUtilities.invokeLater(() -> textArea.getTextArea().requestFocusInWindow());
	}

	private void closeCurrentTab() {
		int selectedIndex = tabbedPane.getSelectedIndex();
		if (selectedIndex != -1) {
			tabbedPane.removeTabAt(selectedIndex);
			textAreas.remove(selectedIndex);

			if (tabbedPane.getTabCount() == 0) {
				createNewTab();
			}
		}
	}

	private void setupTextAreaListeners(TextArea textArea) {
		textArea.getTextArea().addCaretListener(this::updateStatusBar);
		textArea.getTextArea().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				needsUpdate = true;
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				needsUpdate = true;
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				needsUpdate = true;
			}
		});
	}

	private void handleTabChange(ChangeEvent e) {
		if (tabbedPane.getSelectedIndex() != -1) {
			TextArea currentTextArea = getCurrentTextArea();
			if (currentTextArea != null) {
				currentTextArea.getTextArea().requestFocusInWindow();
				updateStatusBarForCurrentTab();
			}
		}
	}

	private void updateStatusBarForCurrentTab() {
		TextArea currentTextArea = getCurrentTextArea();
		if (currentTextArea != null) {
			try {
				JTextArea textArea = currentTextArea.getTextArea();
				int dot = textArea.getCaretPosition();
				int line = dot == 0 ? 1 : textArea.getLineOfOffset(dot) + 1;
				int col = dot - textArea.getLineStartOffset(line - 1) + 1;
				statusBar.updateStatus(line, col);
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void updateStatusBar(CaretEvent e) {
		SwingUtilities.invokeLater(() -> {
			try {
				int dot = e.getDot();
				TextArea currentTextArea = getCurrentTextArea();
				if (currentTextArea != null) {
					JTextArea textArea = currentTextArea.getTextArea();
					int line = dot == 0 ? 1 : textArea.getLineOfOffset(dot) + 1;
					int col = dot - textArea.getLineStartOffset(line - 1) + 1;
					statusBar.updateStatus(line, col);
				}
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		});
	}

	public TextArea getCurrentTextArea() {
		int selectedIndex = tabbedPane.getSelectedIndex();
		return selectedIndex != -1 ? textAreas.get(selectedIndex) : null;
	}

	public void startEditorThread() {
		if (editorThread == null || !editorThread.isAlive()) {
			editorThread = new Thread(this);
			running = true;
			editorThread.start();
		}
	}

	public void stopEditorThread() {
		running = false;
	}

	@Override
	public void run() {
		long lastFrame = System.nanoTime();

		while (running) {
			long now = System.nanoTime();
			long elapsed = now - lastFrame;

			if (elapsed >= FRAME_TIME) {
				totalFrames++;
				if (now > lastFpsCheck + 1000000000) {
					lastFpsCheck = now;
					currentFps = totalFrames;
					totalFrames = 0;

					update();
					repaint();
					System.out.println("FPS: " + currentFps);
					SwingUtilities.invokeLater(() -> statusBar.updateFps(currentFps));
				}

				lastFrame = now - (elapsed % FRAME_TIME);
			} else {
				long sleepTime = (FRAME_TIME - elapsed) / 1000000;
				try {
					Thread.sleep(sleepTime > 0 ? sleepTime : 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void update() {
		if (needsUpdate) {
			needsUpdate = false;
		}
	}

	private record FileNode(File file) {

		@Override
		public String toString() {
			return file.getName().isEmpty() ? file.getPath() : file.getName();
		}
	}

	private static class FileTreeCellRenderer extends DefaultTreeCellRenderer {
		private final FileSystemView fileSystemView = FileSystemView.getFileSystemView();

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

			if (value instanceof DefaultMutableTreeNode node) {
				Object userObject = node.getUserObject();
				if (userObject instanceof FileNode(File file)) {
					setIcon(fileSystemView.getSystemIcon(file));
					setText(fileSystemView.getSystemDisplayName(file));
				}
			}
			return this;
		}
	}
}
