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

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.JTextComponent;

public class JewelTextAreaUI extends BasicTextAreaUI {

	protected Color disabledBackground;
	protected Color inactiveBackground;

	public static ComponentUI createUI(JComponent c) {
		return new JewelTextAreaUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		disabledBackground = UIManager.getColor("TextArea.disabledBackground");
		inactiveBackground = UIManager.getColor("TextArea.inactiveBackground");
	}

	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();
		disabledBackground = null;
		inactiveBackground = null;
	}

	@Override
	protected void paintBackground(Graphics g) {
		JTextComponent textComponent = getComponent();

		Color background = textComponent.getBackground();
		g.setColor(!(background instanceof UIResource) ? background : (!textComponent.isEnabled() ? disabledBackground : (!textComponent.isEditable() ? inactiveBackground : background)));
		g.fillRect(0, 0, textComponent.getWidth(), textComponent.getHeight());
	}

	private java.awt.Color getBackgroundColor(JTextComponent c) {
		if (!c.isEnabled()) {
			return UIManager.getColor("TextArea.disabledBackground");
		} else if (!c.isEditable()) {
			return UIManager.getColor("TextArea.inactiveBackground");
		} else {
			return c.getBackground();
		}
	}
}
