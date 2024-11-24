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

import com.slobodanzivanovic.jewel.laf.core.util.UIScale;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.util.Objects;

/**
 * UI delegate for JOptionPane components in the Jewel Look and Feel.
 * Provides custom layout and UI scaling support.
 */
public class JewelOptionPaneUI extends BasicOptionPaneUI {
	private static final String SEPARATOR_NAME = "OptionPane.separator";
	private static final String ICON_MESSAGE_GAP_KEY = "OptionPane.iconMessageGap";
	private static final String MESSAGE_PADDING_KEY = "OptionPane.messagePadding";
	private static final String MAX_CHARS_KEY = "OptionPane.maxCharactersPerLine";
	private static final String FOCUS_WIDTH_KEY = "Component.focusWidth";

	protected int iconMessageGap;
	protected int messagePadding;
	protected int maxCharactersPerLine;
	private int focusWidth;

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelOptionPaneUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		iconMessageGap = UIManager.getInt(ICON_MESSAGE_GAP_KEY);
		messagePadding = UIManager.getInt(MESSAGE_PADDING_KEY);
		maxCharactersPerLine = UIManager.getInt(MAX_CHARS_KEY);
		focusWidth = UIManager.getInt(FOCUS_WIDTH_KEY);
	}

	@Override
	public Dimension getMinimumOptionPaneSize() {
		return UIScale.scale(super.getMinimumOptionPaneSize());
	}

	@Override
	protected int getMaxCharactersPerLineCount() {
		int max = super.getMaxCharactersPerLineCount();
		return (maxCharactersPerLine > 0 && max == Integer.MAX_VALUE)
			? maxCharactersPerLine
			: max;
	}

	@Override
	protected Container createMessageArea() {
		Container messageArea = super.createMessageArea();

		if (iconMessageGap > 0) {
			Component separator = findByName(messageArea, SEPARATOR_NAME);
			if (separator != null) {
				separator.setPreferredSize(new Dimension(UIScale.scale(iconMessageGap), 1));
			}
		}

		return messageArea;
	}

	@Override
	protected Container createButtonArea() {
		Container buttonArea = super.createButtonArea();

		if (buttonArea.getLayout() instanceof ButtonAreaLayout layout) {
			layout.setPadding(UIScale.scale(layout.getPadding() - (focusWidth * 2)));
		}

		return buttonArea;
	}

	@Override
	protected void addMessageComponents(Container container, GridBagConstraints constraints,
										Object message, int maxWidth, boolean internallyCreated) {
		if (messagePadding > 0) {
			constraints.insets.bottom = UIScale.scale(messagePadding);
		}

		super.addMessageComponents(container, constraints, message, maxWidth, internallyCreated);
	}

	/**
	 * Recursively searches for a component with the specified name.
	 *
	 * @param container the container to search in
	 * @param name      the name to search for
	 * @return the found component or null if not found
	 */
	private Component findByName(Container container, String name) {
		Objects.requireNonNull(container, "Container cannot be null");
		Objects.requireNonNull(name, "Name cannot be null");

		for (Component child : container.getComponents()) {
			if (name.equals(child.getName())) {
				return child;
			}

			if (child instanceof Container childContainer) {
				Component found = findByName(childContainer, name);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}
}
