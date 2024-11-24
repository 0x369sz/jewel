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
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for text field components in the Jewel Look and Feel.
 * Provides custom painting with focus indicators and minimum width support,
 * optimized for performance.
 */
public class JewelTextFieldUI extends BasicTextFieldUI {

	private static final String KEY_FOCUS_WIDTH = "Component.focusWidth";
	private static final String KEY_MINIMUM_WIDTH = "Component.minimumWidth";

	protected final int focusWidth;
	protected final int minimumWidth;

	private BufferedImage backgroundBuffer;
	private Rectangle lastPaintedBounds;
	private boolean isBackgroundValid;

	private Handler handler;

	public JewelTextFieldUI() {
		this.focusWidth = UIManager.getInt(KEY_FOCUS_WIDTH);
		this.minimumWidth = UIManager.getInt(KEY_MINIMUM_WIDTH);
	}

	/**
	 * Creates or returns the UI delegate for text field components.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelTextFieldUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		JTextComponent component = getComponent();
		MigLayoutVisualPadding.install(component, focusWidth);

		component.putClientProperty(RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		component.putClientProperty(RenderingHints.KEY_FRACTIONALMETRICS,
			RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	}

	@Override
	protected void uninstallDefaults() {
		MigLayoutVisualPadding.uninstall(getComponent());
		disposeBackgroundBuffer();
		super.uninstallDefaults();
	}

	@Override
	protected void installListeners() {
		super.installListeners();
		getComponent().addFocusListener(getHandler());
	}

	@Override
	protected void uninstallListeners() {
		getComponent().removeFocusListener(getHandler());
		handler = null;
		super.uninstallListeners();
	}

	Handler getHandler() {
		if (handler == null) {
			handler = new Handler();
		}
		return handler;
	}

	@Override
	protected void paintBackground(Graphics g) {
		JTextComponent component = getComponent();
		Rectangle bounds = component.getBounds();
		bounds.x = 0;
		bounds.y = 0;

		if (!isBackgroundValid(bounds)) {
			updateBackgroundBuffer(component, bounds);
		}

		if (backgroundBuffer != null) {
			Graphics2D g2d = (Graphics2D) g.create();
			try {
				JewelUIUtils.setRenderingHints(g2d);
				g2d.drawImage(backgroundBuffer, 0, 0, null);
			} finally {
				g2d.dispose();
			}
		}
	}

	private boolean isBackgroundValid(Rectangle bounds) {
		return isBackgroundValid &&
			backgroundBuffer != null &&
			Objects.equals(lastPaintedBounds, bounds);
	}

	private void updateBackgroundBuffer(JTextComponent component, Rectangle bounds) {
		if (backgroundBuffer == null ||
			backgroundBuffer.getWidth() != bounds.width ||
			backgroundBuffer.getHeight() != bounds.height) {
			disposeBackgroundBuffer();
			backgroundBuffer = new BufferedImage(
				bounds.width,
				bounds.height,
				BufferedImage.TYPE_INT_RGB
			);
		}

		Graphics2D g2d = backgroundBuffer.createGraphics();
		try {
			JewelUIUtils.paintParentBackground(g2d, component);

			JewelUIUtils.setRenderingHints(g2d);
			float scaledFocusWidth = (component.getBorder() instanceof JewelBorder) ?
				scale((float) focusWidth) : 0;

			g2d.setColor(component.getBackground());
			JewelUIUtils.fillRoundRectangle(g2d, 0, 0, bounds.width, bounds.height,
				scaledFocusWidth, 0);
		} finally {
			g2d.dispose();
		}

		lastPaintedBounds = bounds;
		isBackgroundValid = true;
	}

	private void disposeBackgroundBuffer() {
		if (backgroundBuffer != null) {
			backgroundBuffer.flush();
			backgroundBuffer = null;
		}
		isBackgroundValid = false;
		lastPaintedBounds = null;
	}

	@Override
	public Dimension getPreferredSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return applyMinimumWidth(super.getPreferredSize(component), component);
	}

	@Override
	public Dimension getMinimumSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return applyMinimumWidth(super.getMinimumSize(component), component);
	}

	private Dimension applyMinimumWidth(Dimension size, JComponent component) {
		Container parent = component.getParent();
		if (parent instanceof JComboBox ||
			parent instanceof JSpinner ||
			(parent != null && parent.getParent() instanceof JSpinner)) {
			return size;
		}

		size.width = Math.max(size.width, scale(minimumWidth + (focusWidth * 2)));
		return size;
	}

	/**
	 * Handler for focus events that triggers repainting of the text field.
	 */
	private class Handler implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			invalidateBackground();
		}

		@Override
		public void focusLost(FocusEvent e) {
			invalidateBackground();
		}

		private void invalidateBackground() {
			isBackgroundValid = false;
			getComponent().repaint();
		}
	}
}
