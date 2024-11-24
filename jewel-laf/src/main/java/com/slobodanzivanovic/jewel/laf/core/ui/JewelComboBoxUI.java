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
import com.slobodanzivanovic.jewel.laf.core.util.UIScale;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * Jewel Look and Feel UI delegate for JComboBox.
 * Provides custom rendering with support for different states (enabled, disabled, hover)
 * and UI scaling.
 */
public class JewelComboBoxUI extends BasicComboBoxUI {

	private static final String BACKGROUND_PROPERTY = "background";
	private static final String FOREGROUND_PROPERTY = "foreground";
	private static final String ENABLED_PROPERTY = "enabled";
	private static final String RENDERER_PROPERTY = "renderer";

	// UI Configuration
	protected int focusWidth;
	protected int arc;
	protected String arrowType;

	// Colors
	protected Color borderColor;
	protected Color disabledBorderColor;
	protected Color disabledBackground;
	protected Color disabledForeground;
	protected Color buttonBackground;
	protected Color buttonEditableBackground;
	protected Color buttonArrowColor;
	protected Color buttonDisabledArrowColor;
	protected Color buttonHoverArrowColor;

	// State
	private MouseListener hoverListener;
	private boolean hover;

	/**
	 * Creates a new UI delegate for JComboBox.
	 *
	 * @param c the component to create UI for
	 * @return new instance of JewelComboBoxUI
	 */
	public static ComponentUI createUI(JComponent c) {
		return new JewelComboBoxUI();
	}

	@Override
	protected void installListeners() {
		super.installListeners();
		installHoverListener();
	}

	private void installHoverListener() {
		hoverListener = new JewelUIUtils.HoverListener(null, this::handleHover);
		comboBox.addMouseListener(hoverListener);
	}

	private void handleHover(boolean isHover) {
		if (!comboBox.isEditable()) {
			hover = isHover;
			if (arrowButton != null) {
				arrowButton.repaint();
			}
		}
	}

	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		uninstallHoverListener();
	}

	private void uninstallHoverListener() {
		if (hoverListener != null) {
			comboBox.removeMouseListener(hoverListener);
			hoverListener = null;
		}
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		loadUIDefaults();
		setupPadding();
	}

	private void loadUIDefaults() {
		focusWidth = UIManager.getInt("Component.focusWidth");
		arc = UIManager.getInt("Component.arc");
		arrowType = UIManager.getString("Component.arrowType");

		// Load colors
		borderColor = UIManager.getColor("Component.borderColor");
		disabledBorderColor = UIManager.getColor("Component.disabledBorderColor");
		disabledBackground = UIManager.getColor("ComboBox.disabledBackground");
		disabledForeground = UIManager.getColor("ComboBox.disabledForeground");
		buttonBackground = UIManager.getColor("ComboBox.buttonBackground");
		buttonEditableBackground = UIManager.getColor("ComboBox.buttonEditableBackground");
		buttonArrowColor = UIManager.getColor("ComboBox.buttonArrowColor");
		buttonDisabledArrowColor = UIManager.getColor("ComboBox.buttonDisabledArrowColor");
		buttonHoverArrowColor = UIManager.getColor("ComboBox.buttonHoverArrowColor");
	}

	private void setupPadding() {
		if (padding != null) {
			padding = UIScale.scale(padding);
		}
		MigLayoutVisualPadding.install(comboBox, focusWidth);
	}

	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();
		clearColors();
		MigLayoutVisualPadding.uninstall(comboBox);
	}

	private void clearColors() {
		borderColor = null;
		disabledBorderColor = null;
		disabledBackground = null;
		disabledForeground = null;
		buttonBackground = null;
		buttonEditableBackground = null;
		buttonArrowColor = null;
		buttonDisabledArrowColor = null;
		buttonHoverArrowColor = null;
	}

	@Override
	protected LayoutManager createLayoutManager() {
		return new BasicComboBoxUI.ComboBoxLayoutManager() {
			@Override
			public void layoutContainer(Container parent) {
				super.layoutContainer(parent);
				applyEditorPadding();
			}

			private void applyEditorPadding() {
				if (editor != null && padding != null) {
					editor.setBounds(JewelUIUtils.subtractInsets(editor.getBounds(), padding));
				}
			}
		};
	}

	@Override
	protected FocusListener createFocusListener() {
		return new BasicComboBoxUI.FocusHandler() {
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				handleFocusChange();
			}

			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				handleFocusChange();
			}

			private void handleFocusChange() {
				if (comboBox != null && comboBox.isEditable()) {
					comboBox.repaint();
				}
			}
		};
	}

	@Override
	protected PropertyChangeListener createPropertyChangeListener() {
		return new BasicComboBoxUI.PropertyChangeHandler() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				super.propertyChange(e);
				handlePropertyChange(e);
			}
		};
	}

	private void handlePropertyChange(PropertyChangeEvent e) {
		if (editor == null) return;

		Object source = e.getSource();
		String propertyName = e.getPropertyName();

		boolean isComboBoxColorChange = source == comboBox &&
			(Objects.equals(propertyName, BACKGROUND_PROPERTY) ||
				Objects.equals(propertyName, FOREGROUND_PROPERTY));

		boolean isEditorEnabledChange = source == editor &&
			Objects.equals(propertyName, ENABLED_PROPERTY);

		if (isComboBoxColorChange || isEditorEnabledChange) {
			updateEditorColors();
		}
	}

	@Override
	protected ComboPopup createPopup() {
		return new JewelComboPopup(comboBox);
	}

	@Override
	protected void configureEditor() {
		super.configureEditor();
		setupEditor();
	}

	private void setupEditor() {
		if (editor instanceof JTextComponent) {
			((JTextComponent) editor).setBorder(BorderFactory.createEmptyBorder());
		}
		updateEditorColors();
	}

	private void updateEditorColors() {
		boolean enabled = editor.isEnabled();
		editor.setBackground(JewelUIUtils.nonUIResource(
			enabled ? comboBox.getBackground() : disabledBackground));

		editor.setForeground(JewelUIUtils.nonUIResource(
			getEditorForegroundColor(enabled)));

		updateTextComponentColors();
	}

	private Color getEditorForegroundColor(boolean enabled) {
		return (enabled || editor instanceof JTextComponent)
			? comboBox.getForeground()
			: disabledForeground;
	}

	private void updateTextComponentColors() {
		if (editor instanceof JTextComponent) {
			((JTextComponent) editor).setDisabledTextColor(
				JewelUIUtils.nonUIResource(disabledForeground));
		}
	}

	@Override
	protected JButton createArrowButton() {
		return new JewelArrowButton(
			SwingConstants.SOUTH,
			arrowType,
			buttonArrowColor,
			buttonDisabledArrowColor,
			buttonHoverArrowColor,
			null
		) {
			@Override
			protected boolean isHover() {
				return super.isHover() || (!comboBox.isEditable() && hover);
			}
		};
	}

	@Override
	public void update(Graphics g, JComponent c) {
		if (!c.isOpaque()) {
			paint(g, c);
			return;
		}

		paintComponent(g, c);
	}

	private void paintComponent(Graphics g, JComponent c) {
		JewelUIUtils.paintParentBackground(g, c);
		Graphics2D g2 = (Graphics2D) g.create();

		try {
			setupGraphics(g2);
			paintComboBox(g2, c);
		} finally {
			g2.dispose();
		}

		paint(g, c);
	}

	private void setupGraphics(Graphics2D g2) {
		JewelUIUtils.setRenderingHints(g2);
	}

	private void paintComboBox(Graphics2D g2, JComponent c) {
		int width = c.getWidth();
		int height = c.getHeight();
		float focusWidth = calculateFocusWidth(c);
		float arc = calculateArc(c);
		boolean enabled = comboBox.isEnabled();
		boolean isLeftToRight = comboBox.getComponentOrientation().isLeftToRight();

		paintBackground(g2, c, width, height, focusWidth, arc, enabled);

		if (enabled) {
			paintButtonBackground(g2, c, width, height, focusWidth, arc, isLeftToRight);
		}

		if (comboBox.isEditable()) {
			paintSeparator(g2, height, focusWidth, enabled, isLeftToRight);
		}
	}


	private float calculateFocusWidth(JComponent c) {
		return (c.getBorder() instanceof JewelBorder) ? scale((float) this.focusWidth) : 0;
	}

	private float calculateArc(JComponent c) {
		return (c.getBorder() instanceof JewelRoundBorder) ? scale((float) this.arc) : 0;
	}

	private void paintBackground(Graphics2D g2, JComponent c, int width, int height,
								 float focusWidth, float arc, boolean enabled) {
		g2.setColor(enabled ? c.getBackground() : disabledBackground);
		JewelUIUtils.fillRoundRectangle(g2, 0, 0, width, height, focusWidth, arc);
	}

	private void paintButtonBackground(Graphics2D g2, JComponent c, int width, int height,
									   float focusWidth, float arc, boolean isLeftToRight) {
		g2.setColor(comboBox.isEditable() ? buttonEditableBackground : buttonBackground);

		Shape oldClip = g2.getClip();
		paintButtonClip(g2, width, height, isLeftToRight);
		JewelUIUtils.fillRoundRectangle(g2, 0, 0, width, height, focusWidth, arc);
		g2.setClip(oldClip);
	}

	private void paintButtonClip(Graphics2D g2, int width, int height, boolean isLeftToRight) {
		int arrowX = arrowButton.getX();
		int arrowWidth = arrowButton.getWidth();

		if (isLeftToRight) {
			g2.clipRect(arrowX, 0, width - arrowX, height);
		} else {
			g2.clipRect(0, 0, arrowX + arrowWidth, height);
		}
	}

	private void paintSeparator(Graphics2D g2, int height, float focusWidth,
								boolean enabled, boolean isLeftToRight) {
		g2.setColor(enabled ? borderColor : disabledBorderColor);
		float lineWidth = scale(1f);
		float lineX = calculateSeparatorX(isLeftToRight, lineWidth);

		g2.fill(new Rectangle2D.Float(lineX, focusWidth, lineWidth, height - (focusWidth * 2)));
	}

	private float calculateSeparatorX(boolean isLeftToRight, float lineWidth) {
		return isLeftToRight ? arrowButton.getX() : arrowButton.getX() + arrowButton.getWidth() - lineWidth;
	}

	@Override
	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
		ListCellRenderer<Object> renderer = comboBox.getRenderer();
		CellPaddingBorder.uninstall(renderer);

		Component rendererComponent = createRendererComponent(renderer);
		configureRendererComponent(rendererComponent);
		Rectangle adjustedBounds = adjustBoundsForPadding(bounds, rendererComponent);
		paintRendererComponent(g, rendererComponent, adjustedBounds);
	}

	private Component createRendererComponent(ListCellRenderer<Object> renderer) {
		Component c = renderer.getListCellRendererComponent(
			listBox, comboBox.getSelectedItem(), -1, false, false);
		c.setFont(comboBox.getFont());
		CellPaddingBorder.uninstall(c);
		return c;
	}

	private void configureRendererComponent(Component c) {
		boolean enabled = comboBox.isEnabled();
		c.setForeground(enabled ? comboBox.getForeground() : disabledForeground);
		c.setBackground(enabled ? comboBox.getBackground() : disabledBackground);
	}

	private Rectangle adjustBoundsForPadding(Rectangle bounds, Component c) {
		Rectangle adjustedBounds = new Rectangle(bounds);

		if (padding != null) {
			adjustedBounds = JewelUIUtils.subtractInsets(adjustedBounds, padding);
		}

		Insets rendererInsets = getRendererComponentInsets(c);
		if (rendererInsets != null) {
			adjustedBounds = JewelUIUtils.addInsets(adjustedBounds, rendererInsets);
		}

		return adjustedBounds;
	}

	private void paintRendererComponent(Graphics g, Component rendererComponent, Rectangle bounds) {
		boolean shouldValidate = (rendererComponent instanceof JPanel);
		currentValuePane.paintComponent(g, rendererComponent, comboBox,
			bounds.x, bounds.y, bounds.width, bounds.height, shouldValidate);
	}

	@Override
	public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
		g.setColor(comboBox.isEnabled() ? comboBox.getBackground() : disabledBackground);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	protected Dimension getSizeForComponent(Component comp) {
		Dimension size = super.getSizeForComponent(comp);
		return adjustSizeForRenderer(size, comp);
	}

	private Dimension adjustSizeForRenderer(Dimension size, Component comp) {
		Insets rendererInsets = getRendererComponentInsets(comp);
		if (rendererInsets != null) {
			return new Dimension(
				size.width,
				size.height - rendererInsets.top - rendererInsets.bottom
			);
		}
		return size;
	}

	private Insets getRendererComponentInsets(Component rendererComponent) {
		if (rendererComponent instanceof JComponent) {
			Border rendererBorder = ((JComponent) rendererComponent).getBorder();
			if (rendererBorder != null) {
				return rendererBorder.getBorderInsets(rendererComponent);
			}
		}
		return null;
	}


	/**
	 * Custom popup implementation for the JewelComboBox.
	 * Handles popup sizing, border configuration, and cell rendering.
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private class JewelComboPopup extends BasicComboPopup implements ListCellRenderer {
		private CellPaddingBorder paddingBorder;

		JewelComboPopup(JComboBox combo) {
			super(combo);
		}

		@Override
		protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
			pw = adjustPopupWidth(pw);
			return super.computePopupBounds(px, py, pw, ph);
		}

		private int adjustPopupWidth(int popupWidth) {
			Object prototype = comboBox.getPrototypeDisplayValue();
			if (prototype != null) {
				try {
					comboBox.setPrototypeDisplayValue(null);
					Dimension displaySize = getDisplaySize();
					popupWidth = Math.max(popupWidth, displaySize.width);
				} finally {
					comboBox.setPrototypeDisplayValue(prototype);
				}
			}
			return popupWidth;
		}

		@Override
		protected void configurePopup() {
			super.configurePopup();
			configureBorder();
		}

		private void configureBorder() {
			Border popupBorder = UIManager.getBorder("PopupMenu.border");
			if (popupBorder != null) {
				setBorder(popupBorder);
			}
		}

		@Override
		protected void configureList() {
			super.configureList();
			list.setCellRenderer(this);
		}

		@Override
		protected PropertyChangeListener createPropertyChangeListener() {
			return new BasicComboPopup.PropertyChangeHandler() {
				@Override
				public void propertyChange(PropertyChangeEvent e) {
					super.propertyChange(e);
					handlePropertyChange(e);
				}
			};
		}

		private void handlePropertyChange(PropertyChangeEvent e) {
			if (RENDERER_PROPERTY.equals(e.getPropertyName())) {
				list.setCellRenderer(this);
			}
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
													  int index, boolean isSelected, boolean cellHasFocus) {

			ListCellRenderer renderer = comboBox.getRenderer();
			CellPaddingBorder.uninstall(renderer);

			Component rendererComponent = renderer.getListCellRendererComponent(
				list, value, index, isSelected, cellHasFocus);

			installPaddingBorder(rendererComponent);

			return rendererComponent;
		}

		private void installPaddingBorder(Component c) {
			if (c instanceof JComponent) {
				if (paddingBorder == null) {
					paddingBorder = new CellPaddingBorder(padding);
				}
				paddingBorder.install((JComponent) c);
			}
		}
	}

	/**
	 * Custom border that combines padding with the renderer's original border.
	 */
	private static class CellPaddingBorder extends AbstractBorder {
		private final Insets padding;
		private Border rendererBorder;

		CellPaddingBorder(Insets padding) {
			this.padding = padding;
		}

		void install(JComponent rendererComponent) {
			Border oldBorder = rendererComponent.getBorder();
			if (!(oldBorder instanceof CellPaddingBorder)) {
				rendererBorder = oldBorder;
				rendererComponent.setBorder(this);
			}
		}

		static void uninstall(Object o) {
			if (!(o instanceof JComponent rendererComponent)) {
				return;
			}

			Border border = rendererComponent.getBorder();
			if (border instanceof CellPaddingBorder paddingBorder) {
				rendererComponent.setBorder(paddingBorder.rendererBorder);
				paddingBorder.rendererBorder = null;
			}
		}

		@Override
		public Insets getBorderInsets(Component c, Insets insets) {
			if (rendererBorder != null) {
				return combineInsetsWithRenderer(c, insets);
			}
			return copyPaddingInsets(insets);
		}

		private Insets combineInsetsWithRenderer(Component c, Insets insets) {
			Insets rendererInsets = rendererBorder.getBorderInsets(c);
			insets.top = Math.max(padding.top, rendererInsets.top);
			insets.left = Math.max(padding.left, rendererInsets.left);
			insets.bottom = Math.max(padding.bottom, rendererInsets.bottom);
			insets.right = Math.max(padding.right, rendererInsets.right);
			return insets;
		}

		private Insets copyPaddingInsets(Insets insets) {
			insets.top = padding.top;
			insets.left = padding.left;
			insets.bottom = padding.bottom;
			insets.right = padding.right;
			return insets;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			if (rendererBorder != null) {
				rendererBorder.paintBorder(c, g, x, y, width, height);
			}
		}
	}
}
