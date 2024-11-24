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
import javax.swing.plaf.basic.BasicViewportUI;
import java.awt.*;
import java.util.Objects;

/**
 * UI delegate for JViewport components in the Jewel Look and Feel.
 * Provides custom background painting for table viewports while maintaining
 * standard behavior for other viewport types.
 */
public class JewelViewportUI extends BasicViewportUI {
	private static ComponentUI instance;

	/**
	 * Creates or returns the singleton UI delegate for JViewport components.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		if (instance == null) {
			instance = new JewelViewportUI();
		}
		return instance;
	}

	@Override
	public void update(Graphics graphics, JComponent component) {
		Objects.requireNonNull(graphics, "Graphics cannot be null");
		Objects.requireNonNull(component, "Component cannot be null");

		JViewport viewport = (JViewport) component;
		Component view = viewport.getView();

		// Special handling for table viewports
		if (component.isOpaque() && view instanceof JTable) {
			Graphics2D g2d = (Graphics2D) graphics.create();
			try {
				JewelUIUtils.setRenderingHints(g2d);
				g2d.setColor(view.getBackground());
				g2d.fillRect(0, 0, component.getWidth(), component.getHeight());
				paint(g2d, component);
			} finally {
				g2d.dispose();
			}
		} else {
			super.update(graphics, component);
		}
	}
}
