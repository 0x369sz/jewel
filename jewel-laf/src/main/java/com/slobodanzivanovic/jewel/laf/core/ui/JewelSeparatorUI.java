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

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for JSeparator components in the Jewel Look and Feel.
 * Provides custom painting with configurable height, stripe width, and indentation,
 * supporting both horizontal and vertical orientations.
 */
public class JewelSeparatorUI extends BasicSeparatorUI {
	private static final String HEIGHT_KEY = ".height";
	private static final String STRIPE_WIDTH_KEY = ".stripeWidth";
	private static final String STRIPE_INDENT_KEY = ".stripeIndent";

	protected int height;
	protected int stripeWidth;
	protected int stripeIndent;

	private boolean defaultsInitialized;
	private static ComponentUI instance;

	/**
	 * Creates or returns the singleton UI delegate for JSeparator components.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		if (instance == null) {
			instance = new JewelSeparatorUI();
		}
		return instance;
	}

	@Override
	protected void installDefaults(JSeparator separator) {
		Objects.requireNonNull(separator, "Separator cannot be null");
		super.installDefaults(separator);

		if (!defaultsInitialized) {
			String prefix = getPropertyPrefix();
			height = UIManager.getInt(prefix + HEIGHT_KEY);
			stripeWidth = UIManager.getInt(prefix + STRIPE_WIDTH_KEY);
			stripeIndent = UIManager.getInt(prefix + STRIPE_INDENT_KEY);
			defaultsInitialized = true;
		}
	}

	@Override
	protected void uninstallDefaults(JSeparator separator) {
		Objects.requireNonNull(separator, "Separator cannot be null");
		super.uninstallDefaults(separator);
		defaultsInitialized = false;
	}

	/**
	 * Returns the prefix used for UI property keys.
	 *
	 * @return the property prefix string
	 */
	protected String getPropertyPrefix() {
		return "Separator";
	}

	@Override
	public void paint(Graphics graphics, JComponent component) {
		Objects.requireNonNull(graphics, "Graphics cannot be null");
		Objects.requireNonNull(component, "Component cannot be null");

		Graphics2D g2d = (Graphics2D) graphics.create();
		try {
			JewelUIUtils.setRenderingHints(g2d);
			g2d.setColor(component.getForeground());

			float scaledWidth = scale((float) stripeWidth);
			float scaledIndent = scale((float) stripeIndent);

			JSeparator separator = (JSeparator) component;
			if (separator.getOrientation() == JSeparator.VERTICAL) {
				g2d.fill(new Rectangle2D.Float(scaledIndent, 0, scaledWidth, component.getHeight()));
			} else {
				g2d.fill(new Rectangle2D.Float(0, scaledIndent, component.getWidth(), scaledWidth));
			}
		} finally {
			g2d.dispose();
		}
	}

	@Override
	public Dimension getPreferredSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		JSeparator separator = (JSeparator) component;
		int scaledHeight = scale(height);

		return separator.getOrientation() == JSeparator.VERTICAL
			? new Dimension(scaledHeight, 0)
			: new Dimension(0, scaledHeight);
	}
}
