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

package com.slobodanzivanovic.jewel.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

public class CustomFilePicker extends JDialog {
	private JTree fileTree;
	private DefaultTreeModel treeModel;
	private JTextField pathField;
	private JTextField filterField;
	private File selectedFile;
	private JButton openButton;
	private JButton cancelButton;
	private boolean approved = false;
	private JPanel previewPanel;
	private JTextArea previewText;
	private final FileSystemView fileSystemView;
	private boolean folderSelectionMode = false;

	private Point initialClick;
	private final JPanel titlePanel;

	public CustomFilePicker(Frame parent, String title) {
		super(parent, title, true);
		setUndecorated(true);
		this.fileSystemView = FileSystemView.getFileSystemView();

		titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(new Color(60, 63, 65));
		titlePanel.setBorder(new EmptyBorder(8, 10, 8, 10));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 12));

		JButton closeButton = new JButton("×");
		closeButton.setForeground(Color.WHITE);
		closeButton.setBackground(new Color(60, 63, 65));
		closeButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		closeButton.setFocusPainted(false);
		closeButton.setFont(new Font(closeButton.getFont().getName(), Font.BOLD, 16));
		closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		closeButton.addActionListener(e -> {
			approved = false;
			dispose();
		});

		titlePanel.add(titleLabel, BorderLayout.WEST);
		titlePanel.add(closeButton, BorderLayout.EAST);

		titlePanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				initialClick = e.getPoint();
				getComponentAt(initialClick);
			}
		});

		titlePanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int thisX = getLocation().x;
				int thisY = getLocation().y;

				int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
				int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

				int X = thisX + xMoved;
				int Y = thisY + yMoved;
				setLocation(X, Y);
			}
		});

		initComponents();
		setupUI();
		initializeTree();

		getRootPane().setBorder(BorderFactory.createLineBorder(new Color(40, 43, 45), 1));
	}

	public void setFolderSelectionMode(boolean folderMode) {
		this.folderSelectionMode = folderMode;
		openButton.setText(folderMode ? "Open Folder" : "Open File");
	}

	private void initComponents() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		treeModel = new DefaultTreeModel(root);
		fileTree = new JTree(treeModel);
		fileTree.setCellRenderer(new FileTreeCellRenderer());
		fileTree.setRootVisible(false);
		fileTree.setShowsRootHandles(true);

		pathField = new JTextField();
		pathField.setEditable(false);
		filterField = new JTextField();
		filterField.setToolTipText("Filter files (e.g., *.java, *.txt)");

		openButton = new JButton("Open File");
		cancelButton = new JButton("Cancel");

		previewPanel = new JPanel(new BorderLayout());
		previewText = new JTextArea();
		previewText.setEditable(false);
		previewText.setFont(new Font("Monospaced", Font.PLAIN, 12));
	}

	private void setupUI() {
		setLayout(new BorderLayout());

		add(titlePanel, BorderLayout.NORTH);

		JPanel northPanel = new JPanel(new BorderLayout(5, 5));
		northPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JPanel pathPanel = new JPanel(new BorderLayout(5, 0));
		pathPanel.add(new JLabel("Path: "), BorderLayout.WEST);
		pathPanel.add(pathField, BorderLayout.CENTER);

		JPanel filterPanel = new JPanel(new BorderLayout(5, 0));
		filterPanel.add(new JLabel("Filter: "), BorderLayout.WEST);
		filterPanel.add(filterField, BorderLayout.CENTER);

		northPanel.add(pathPanel, BorderLayout.CENTER);
		northPanel.add(filterPanel, BorderLayout.SOUTH);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JScrollPane treeScrollPane = new JScrollPane(fileTree);
		treeScrollPane.setPreferredSize(new Dimension(300, 400));
		splitPane.setLeftComponent(treeScrollPane);

		previewPanel.add(new JScrollPane(previewText), BorderLayout.CENTER);
		splitPane.setRightComponent(previewPanel);
		splitPane.setDividerLocation(300);

		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.add(openButton);
		southPanel.add(cancelButton);

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(northPanel, BorderLayout.NORTH);
		contentPanel.add(splitPane, BorderLayout.CENTER);
		contentPanel.add(southPanel, BorderLayout.SOUTH);

		add(contentPanel, BorderLayout.CENTER);

		setupListeners();

		setPreferredSize(new Dimension(800, 600));
		pack();
		setLocationRelativeTo(null);
	}

	private void setupListeners() {
		fileTree.addTreeSelectionListener(e -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
			if (node != null && node.getUserObject() instanceof FileNode(File file)) {
				selectedFile = file;
				pathField.setText(file.getAbsolutePath());
				updatePreview(file);

				if (folderSelectionMode) {
					openButton.setEnabled(file.isDirectory());
				} else {
					openButton.setEnabled(file.isFile());
				}
			}
		});

		fileTree.addTreeExpansionListener(new TreeExpansionListener() {
			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
				if (node.getChildCount() == 1 && node.getFirstChild().toString().equals("Loading...")) {
					loadChildren(node);
				}
			}

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				// Not needed
			}
		});

		filterField.addActionListener(e -> applyFilter(filterField.getText()));

		openButton.addActionListener(e -> {
			approved = true;
			dispose();
		});

		cancelButton.addActionListener(e -> {
			approved = false;
			dispose();
		});
	}

	private void initializeTree() {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
		File[] roots = fileSystemView.getRoots();
		for (File fileRoot : roots) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(new FileNode(fileRoot));
			root.add(node);
			if (fileRoot.isDirectory()) {
				node.add(new DefaultMutableTreeNode("Loading..."));
			}
		}
		treeModel.reload();
	}

	private void loadChildren(DefaultMutableTreeNode node) {
		node.removeAllChildren();
		FileNode fileNode = (FileNode) node.getUserObject();
		File[] files = fileSystemView.getFiles(fileNode.file(), true);

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
					childNode.add(new DefaultMutableTreeNode("Loading..."));
				}
			}
		}
		treeModel.reload(node);
	}

	private void updatePreview(File file) {
		previewText.setText("");
		if (folderSelectionMode && file.isDirectory()) {
			try {
				File[] files = file.listFiles();
				if (files != null) {
					StringBuilder preview = new StringBuilder();
					preview.append("Folder contents:\n\n");
					Arrays.sort(files, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
					for (File childFile : files) {
						if (!childFile.isHidden()) {
							preview.append(childFile.isDirectory() ? "📁 " : "📄 ").append(childFile.getName()).append("\n");
						}
					}
					previewText.setText(preview.toString());
				}
			} catch (Exception e) {
				previewText.setText("Cannot preview folder contents.");
			}
			// Preview files < 1MB
		} else if (file.isFile() && file.length() < 1024 * 1024) {
			try {
				String content = Files.readString(file.toPath());
				previewText.setText(content);
				previewText.setCaretPosition(0);
			} catch (Exception e) {
				previewText.setText("Cannot preview this file.");
			}
		}
	}

	private void applyFilter(String filterText) {
		String filter = filterText.toLowerCase().trim();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
		filterNode(root, filter);
		treeModel.reload();
		expandFilteredNodes(root);
	}

	private boolean filterNode(DefaultMutableTreeNode node, String filter) {
		if (filter.isEmpty()) return true;

		if (node.getUserObject() instanceof FileNode(File file)) {
			return file.isDirectory();
		}

		return false;
	}

	private void expandFilteredNodes(DefaultMutableTreeNode node) {
		if (node.getChildCount() >= 0) {
			for (int i = 0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
				expandFilteredNodes(child);
			}
		}
		if (node.getUserObject() instanceof FileNode) {
			TreePath path = new TreePath(node.getPath());
			fileTree.expandPath(path);
		}
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public boolean isApproved() {
		return approved;
	}

	private record FileNode(File file) {

		@Override
		public String toString() {
			return file.getName().isEmpty() ? file.getPath() : file.getName();
		}
	}

	private class FileTreeCellRenderer extends DefaultTreeCellRenderer {
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
