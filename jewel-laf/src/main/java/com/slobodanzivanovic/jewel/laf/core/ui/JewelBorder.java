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
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.text.JTextComponent;
import java.awt.*;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * A custom border implementation for the Jewel Look and Feel that provides
 * focus indication and border styling for various Swing components.
 */
public class JewelBorder extends BasicBorders.MarginBorder {

	protected final int focusWidth = UIManager.getInt("Component.focusWidth");
	protected final int innerFocusWidth = UIManager.getInt("Component.innerFocusWidth");
	protected final Color focusColor = UIManager.getColor("Component.focusColor");
	protected final Color borderColor = UIManager.getColor("Component.borderColor");
	protected final Color disabledBorderColor = UIManager.getColor("Component.disabledBorderColor");
	protected final Color focusedBorderColor = UIManager.getColor("Component.focusedBorderColor");

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2 = (Graphics2D) g.create();
		try {
			JewelUIUtils.setRenderingHints(g2);

			float focusWidth = getFocusWidth();
			float borderWidth = getBorderWidth(c);
			float arc = getArc();

			if (isFocused(c)) {
				g2.setColor(getFocusColor(c));
				JewelUIUtils.paintOutlineBorder(g2, x, y, width, height, focusWidth,
					getLineWidth() + scale((float) innerFocusWidth), arc);
			}

			g2.setPaint(getBorderColor(c));
			JewelUIUtils.drawRoundRectangle(g2, x, y, width, height, focusWidth, borderWidth, arc);
		} finally {
			g2.dispose();
		}
	}

	protected Color getFocusColor(Component c) {
		return focusColor;
	}

	protected Paint getBorderColor(Component c) {
		boolean enabled = c.isEnabled() && (!(c instanceof JTextComponent) || ((JTextComponent) c).isEditable());
		return enabled
			? (isFocused(c) ? focusedBorderColor : borderColor)
			: disabledBorderColor;
	}

	protected boolean isFocused(Component c) {
		if (c instanceof JScrollPane) {
			JViewport viewport = ((JScrollPane) c).getViewport();
			Component view = (viewport != null) ? viewport.getView() : null;
			if (view != null) {
				if (view.hasFocus())
					return true;

				if ((view instanceof JTable && ((JTable) view).isEditing()) ||
					(view instanceof JTree && ((JTree) view).isEditing())) {
					Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
					if (focusOwner != null)
						return SwingUtilities.isDescendingFrom(focusOwner, view);
				}
			}
			return false;
		} else if (c instanceof JComboBox && ((JComboBox<?>) c).isEditable()) {
			Component editorComponent = ((JComboBox<?>) c).getEditor().getEditorComponent();
			return editorComponent != null && editorComponent.hasFocus();
		} else if (c instanceof JSpinner) {
			JComponent editor = ((JSpinner) c).getEditor();
			if (editor instanceof JSpinner.DefaultEditor) {
				JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
				if (textField != null)
					return textField.hasFocus();
			}
			return false;
		} else
			return c.hasFocus();
	}

	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		float ow = getFocusWidth() + getLineWidth();

		insets = super.getBorderInsets(c, insets);
		insets.top = Math.round(scale((float) insets.top) + ow);
		insets.left = Math.round(scale((float) insets.left) + ow);
		insets.bottom = Math.round(scale((float) insets.bottom) + ow);
		insets.right = Math.round(scale((float) insets.right) + ow);
		return insets;
	}

	protected float getFocusWidth() {
		return scale((float) focusWidth);
	}

	protected float getLineWidth() {
		return scale(1f);
	}

	protected float getBorderWidth(Component c) {
		return getLineWidth();
	}

	protected float getArc() {
		return 0;
	}
}
