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

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import java.awt.*;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * Jewel Look and Feel UI delegate for JEditorPane.
 * Provides scaled minimum width support for editor panes.
 */
public class JewelEditorPaneUI extends BasicEditorPaneUI {

	private static final String MINIMUM_WIDTH_KEY = "Component.minimumWidth";
	private static final int BORDER_COMPENSATION = 2;  // Accounts for border width

	protected int minimumWidth;

	/**
	 * Creates a new UI delegate for JEditorPane.
	 *
	 * @param c the component to create UI for
	 * @return new instance of JewelEditorPaneUI
	 */
	public static ComponentUI createUI(JComponent c) {
		return new JewelEditorPaneUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		initializeMinimumWidth();
	}

	private void initializeMinimumWidth() {
		minimumWidth = UIManager.getInt(MINIMUM_WIDTH_KEY);
	}

	@Override
	public Dimension getPreferredSize(JComponent c) {
		return applyMinimumWidth(super.getPreferredSize(c));
	}

	@Override
	public Dimension getMinimumSize(JComponent c) {
		return applyMinimumWidth(super.getMinimumSize(c));
	}

	private Dimension applyMinimumWidth(Dimension size) {
		int scaledMinWidth = scale(minimumWidth);
		int borderCompensation = scale(BORDER_COMPENSATION);
		size.width = Math.max(size.width, scaledMinWidth - borderCompensation);
		return size;
	}
}
