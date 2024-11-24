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
import com.slobodanzivanovic.jewel.util.platform.PlatformInfo;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for JPasswordField components in the Jewel Look and Feel.
 * Provides custom painting, focus handling, and platform-specific features.
 */
public class JewelPasswordFieldUI extends BasicPasswordFieldUI {
	private static final String FOCUS_WIDTH_KEY = "Component.focusWidth";
	private static final String MIN_WIDTH_KEY = "Component.minimumWidth";
	//	private static final char MAC_ECHO_CHAR = '\u2022';
	private static final char MAC_ECHO_CHAR = '•';

	protected int focusWidth;
	protected int minimumWidth;
	private Handler handler;

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelPasswordFieldUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		JTextComponent component = getComponent();
		if (PlatformInfo.IS_MAC) {
			LookAndFeel.installProperty(component, "echoChar", MAC_ECHO_CHAR);
		}

		focusWidth = UIManager.getInt(FOCUS_WIDTH_KEY);
		minimumWidth = UIManager.getInt(MIN_WIDTH_KEY);

		MigLayoutVisualPadding.install(component, focusWidth);
	}

	@Override
	protected void uninstallDefaults() {
		MigLayoutVisualPadding.uninstall(getComponent());
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

	protected Handler getHandler() {
		if (handler == null) {
			handler = new Handler();
		}
		return handler;
	}

	@Override
	protected void paintBackground(Graphics g) {
		JTextComponent component = getComponent();
		Objects.requireNonNull(g, "Graphics cannot be null");

		JewelUIUtils.paintParentBackground(g, component);

		Graphics2D g2d = (Graphics2D) g.create();
		try {
			JewelUIUtils.setRenderingHints(g2d);

			float scaledFocusWidth = (component.getBorder() instanceof JewelBorder)
				? scale((float) focusWidth)
				: 0;

			g2d.setColor(component.getBackground());
			JewelUIUtils.fillRoundRectangle(g2d, 0, 0, component.getWidth(),
				component.getHeight(), scaledFocusWidth, 0);
		} finally {
			g2d.dispose();
		}
	}

	@Override
	public Dimension getPreferredSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return applyMinimumWidth(super.getPreferredSize(component));
	}

	@Override
	public Dimension getMinimumSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return applyMinimumWidth(super.getMinimumSize(component));
	}

	private Dimension applyMinimumWidth(Dimension size) {
		Objects.requireNonNull(size, "Size cannot be null");
		size.width = Math.max(size.width, scale(minimumWidth + (focusWidth * 2)));
		return size;
	}

	/**
	 * Handler for focus events that triggers repainting.
	 */
	public class Handler implements FocusListener {
		@Override
		public void focusGained(FocusEvent event) {
			getComponent().repaint();
		}

		@Override
		public void focusLost(FocusEvent event) {
			getComponent().repaint();
		}
	}
}
