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

package com.slobodanzivanovic.jewel.laf.core.ui;

import com.slobodanzivanovic.jewel.laf.core.util.JewelUIUtils;
import com.slobodanzivanovic.jewel.laf.core.util.MigLayoutVisualPadding;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for JSpinner components in the Jewel Look and Feel.
 * Provides custom painting with arrow buttons and focus support.
 */
public class JewelSpinnerUI extends BasicSpinnerUI {
	private static final String KEY_FOCUS_WIDTH = "Component.focusWidth";
	private static final String KEY_ARC = "Component.arc";
	private static final String KEY_MIN_WIDTH = "Component.minimumWidth";
	private static final String KEY_ARROW_TYPE = "Component.arrowType";
	private static final String KEY_BORDER_COLOR = "Component.borderColor";
	private static final String KEY_DISABLED_BORDER = "Component.disabledBorderColor";
	private static final String KEY_DISABLED_BG = "Spinner.disabledBackground";
	private static final String KEY_DISABLED_FG = "Spinner.disabledForeground";
	private static final String KEY_BUTTON_BG = "Spinner.buttonBackground";
	private static final String KEY_BUTTON_ARROW = "Spinner.buttonArrowColor";
	private static final String KEY_BUTTON_DISABLED = "Spinner.buttonDisabledArrowColor";
	private static final String KEY_BUTTON_HOVER = "Spinner.buttonHoverArrowColor";
	private static final String KEY_PADDING = "Spinner.padding";

	private Handler handler;

	protected int focusWidth;
	protected int arc;
	protected int minimumWidth;
	protected String arrowType;
	protected Color borderColor;
	protected Color disabledBorderColor;
	protected Color disabledBackground;
	protected Color disabledForeground;
	protected Color buttonBackground;
	protected Color buttonArrowColor;
	protected Color buttonDisabledArrowColor;
	protected Color buttonHoverArrowColor;
	protected Insets padding;

	/**
	 * Creates a UI delegate for the specified component.
	 *
	 * @param component the component to create the UI delegate for
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelSpinnerUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		focusWidth = UIManager.getInt(KEY_FOCUS_WIDTH);
		arc = UIManager.getInt(KEY_ARC);
		minimumWidth = UIManager.getInt(KEY_MIN_WIDTH);
		arrowType = UIManager.getString(KEY_ARROW_TYPE);
		borderColor = UIManager.getColor(KEY_BORDER_COLOR);
		disabledBorderColor = UIManager.getColor(KEY_DISABLED_BORDER);
		disabledBackground = UIManager.getColor(KEY_DISABLED_BG);
		disabledForeground = UIManager.getColor(KEY_DISABLED_FG);
		buttonBackground = UIManager.getColor(KEY_BUTTON_BG);
		buttonArrowColor = UIManager.getColor(KEY_BUTTON_ARROW);
		buttonDisabledArrowColor = UIManager.getColor(KEY_BUTTON_DISABLED);
		buttonHoverArrowColor = UIManager.getColor(KEY_BUTTON_HOVER);
		padding = UIManager.getInsets(KEY_PADDING);

		padding = scale(padding);

		MigLayoutVisualPadding.install(spinner, focusWidth);
	}

	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();

		borderColor = null;
		disabledBorderColor = null;
		disabledBackground = null;
		disabledForeground = null;
		buttonBackground = null;
		buttonArrowColor = null;
		buttonDisabledArrowColor = null;
		buttonHoverArrowColor = null;
		padding = null;

		MigLayoutVisualPadding.uninstall(spinner);
	}

	@Override
	protected void installListeners() {
		super.installListeners();
		addEditorFocusListener(spinner.getEditor());
		spinner.addPropertyChangeListener(getHandler());
	}

	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		removeEditorFocusListener(spinner.getEditor());
		spinner.removePropertyChangeListener(getHandler());
		handler = null;
	}

	Handler getHandler() {
		if (handler == null) {
			handler = new Handler();
		}
		return handler;
	}

	@Override
	protected JComponent createEditor() {
		JComponent editor = super.createEditor();
		updateEditorColors();
		return editor;
	}

	@Override
	protected void replaceEditor(JComponent oldEditor, JComponent newEditor) {
		Objects.requireNonNull(oldEditor, "Old editor cannot be null");
		Objects.requireNonNull(newEditor, "New editor cannot be null");

		super.replaceEditor(oldEditor, newEditor);
		removeEditorFocusListener(oldEditor);
		addEditorFocusListener(newEditor);
		updateEditorColors();
	}

	private void addEditorFocusListener(JComponent editor) {
		if (editor == null) return;

		JTextField textField = getEditorTextField(editor);
		if (textField != null) {
			textField.addFocusListener(getHandler());
		}
	}

	private void removeEditorFocusListener(JComponent editor) {
		if (editor == null) return;

		JTextField textField = getEditorTextField(editor);
		if (textField != null) {
			textField.removeFocusListener(getHandler());
		}
	}

	private void updateEditorColors() {
		JTextField textField = getEditorTextField(spinner.getEditor());
		if (textField != null) {
			textField.setBackground(JewelUIUtils.nonUIResource(
				spinner.isEnabled() ? spinner.getBackground() : disabledBackground));
			textField.setForeground(JewelUIUtils.nonUIResource(spinner.getForeground()));
			textField.setDisabledTextColor(JewelUIUtils.nonUIResource(disabledForeground));
		}
	}

	private JTextField getEditorTextField(JComponent editor) {
		if (editor instanceof JSpinner.DefaultEditor defaultEditor) {
			return defaultEditor.getTextField();
		}
		return null;
	}

	@Override
	protected LayoutManager createLayout() {
		return getHandler();
	}

	@Override
	protected Component createNextButton() {
		return createArrowButton(SwingConstants.NORTH, "Spinner.nextButton");
	}

	@Override
	protected Component createPreviousButton() {
		return createArrowButton(SwingConstants.SOUTH, "Spinner.previousButton");
	}

	private Component createArrowButton(int direction, String name) {
		JewelArrowButton button = new JewelArrowButton(
			direction,
			arrowType,
			buttonArrowColor,
			buttonDisabledArrowColor,
			buttonHoverArrowColor,
			null
		);
		button.setName(name);
		button.setYOffset(direction == SwingConstants.NORTH ? 1 : -1);

		if (direction == SwingConstants.NORTH) {
			installNextButtonListeners(button);
		} else {
			installPreviousButtonListeners(button);
		}
		return button;
	}

	@Override
	public void update(Graphics g, JComponent component) {
		if (!component.isOpaque()) {
			return;
		}

		JewelUIUtils.paintParentBackground(g, component);
		Graphics2D g2 = (Graphics2D) g.create();

		try {
			JewelUIUtils.setRenderingHints(g2);

			int width = component.getWidth();
			int height = component.getHeight();
			float focusWidth = component.getBorder() instanceof JewelBorder ? scale((float) this.focusWidth) : 0;
			float arc = component.getBorder() instanceof JewelRoundBorder ? scale((float) this.arc) : 0;
			Component nextButton = getHandler().nextButton;
			int arrowX = nextButton.getX();
			int arrowWidth = nextButton.getWidth();
			boolean enabled = spinner.isEnabled();
			boolean isLeftToRight = spinner.getComponentOrientation().isLeftToRight();

			g2.setColor(enabled ? component.getBackground() : disabledBackground);
			JewelUIUtils.fillRoundRectangle(g2, 0, 0, width, height, focusWidth, arc);

			if (enabled) {
				g2.setColor(buttonBackground);
				Shape oldClip = g2.getClip();
				if (isLeftToRight) {
					g2.clipRect(arrowX, 0, width - arrowX, height);
				} else {
					g2.clipRect(0, 0, arrowX + arrowWidth, height);
				}
				JewelUIUtils.fillRoundRectangle(g2, 0, 0, width, height, focusWidth, arc);
				g2.setClip(oldClip);
			}

			g2.setColor(enabled ? borderColor : disabledBorderColor);
			float lineWidth = scale(1f);
			float lineX = isLeftToRight ? arrowX : arrowX + arrowWidth - lineWidth;
			g2.fill(new Rectangle2D.Float(lineX, focusWidth, lineWidth, height - (focusWidth * 2)));
		} finally {
			g2.dispose();
		}

		paint(g, component);
	}

	/**
	 * Handler class that manages layout and event handling for the spinner.
	 */
	private class Handler implements LayoutManager, FocusListener, PropertyChangeListener {
		private Component editor;
		private Component nextButton;
		private Component previousButton;

		@Override
		public void addLayoutComponent(String name, Component component) {
			switch (name) {
				case "Editor" -> editor = component;
				case "Next" -> nextButton = component;
				case "Previous" -> previousButton = component;
			}
		}

		@Override
		public void removeLayoutComponent(Component component) {
			if (component == editor) {
				editor = null;
			} else if (component == nextButton) {
				nextButton = null;
			} else if (component == previousButton) {
				previousButton = null;
			}
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			Insets insets = parent.getInsets();
			Dimension editorSize = editor != null ? editor.getPreferredSize() : new Dimension(0, 0);

			int innerHeight = editorSize.height + padding.top + padding.bottom;
			return new Dimension(
				Math.max(insets.left + insets.right + editorSize.width + padding.left + padding.right + innerHeight,
					scale(minimumWidth + (focusWidth * 2))),
				insets.top + insets.bottom + innerHeight);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return preferredLayoutSize(parent);
		}

		@Override
		public void layoutContainer(Container parent) {
			Objects.requireNonNull(parent, "Parent container cannot be null");

			Dimension size = parent.getSize();
			Insets insets = parent.getInsets();
			Rectangle bounds = JewelUIUtils.subtractInsets(new Rectangle(size), insets);

			if (nextButton == null && previousButton == null) {
				if (editor != null) {
					editor.setBounds(bounds);
				}
				return;
			}

			Rectangle editorRect = new Rectangle(bounds);
			Rectangle buttonsRect = new Rectangle(bounds);

			int buttonsWidth = bounds.height;
			buttonsRect.width = buttonsWidth;

			if (parent.getComponentOrientation().isLeftToRight()) {
				editorRect.width -= buttonsWidth;
				buttonsRect.x += editorRect.width;
			} else {
				editorRect.x += buttonsWidth;
				editorRect.width -= buttonsWidth;
			}

			if (editor != null) {
				editor.setBounds(JewelUIUtils.subtractInsets(editorRect, padding));
			}

			int nextHeight = Math.round(buttonsRect.height / 2f);
			if (nextButton != null) {
				nextButton.setBounds(buttonsRect.x, buttonsRect.y, buttonsRect.width, nextHeight);
			}
			if (previousButton != null) {
				previousButton.setBounds(buttonsRect.x, buttonsRect.y + nextHeight,
					buttonsRect.width, buttonsRect.height - nextHeight);
			}
		}

		@Override
		public void focusGained(FocusEvent event) {
			spinner.repaint();
		}

		@Override
		public void focusLost(FocusEvent event) {
			spinner.repaint();
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String propertyName = event.getPropertyName();
			if ("background".equals(propertyName) ||
				"foreground".equals(propertyName) ||
				"enabled".equals(propertyName)) {
				updateEditorColors();
			}
		}
	}
}
