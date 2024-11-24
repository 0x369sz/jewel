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
import com.slobodanzivanovic.jewel.laf.core.util.UIScale;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

/**
 * UI delegate for progress bars in the Jewel Look and Feel.
 * Provides custom painting with rounded corners and scaled dimensions.
 */
public class JewelProgressBarUI extends BasicProgressBarUI {

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param component the component to create the UI delegate for
	 * @return the UI delegate for the component
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component must not be null");
		return new JewelProgressBarUI();
	}

	@Override
	protected Dimension getPreferredInnerHorizontal() {
		return UIScale.scale(super.getPreferredInnerHorizontal());
	}

	@Override
	protected Dimension getPreferredInnerVertical() {
		return UIScale.scale(super.getPreferredInnerVertical());
	}

	@Override
	public void update(Graphics graphics, JComponent component) {
		Objects.requireNonNull(graphics, "Graphics must not be null");
		Objects.requireNonNull(component, "Component must not be null");

		if (component.isOpaque()) {
			JewelUIUtils.paintParentBackground(graphics, component);
		}
		paint(graphics, component);
	}

	@Override
	public void paint(Graphics graphics, JComponent component) {
		Objects.requireNonNull(graphics, "Graphics must not be null");
		Objects.requireNonNull(component, "Component must not be null");

		Insets insets = progressBar.getInsets();
		int x = insets.left;
		int y = insets.top;
		int width = progressBar.getWidth() - (insets.right + insets.left);
		int height = progressBar.getHeight() - (insets.top + insets.bottom);

		if (width <= 0 || height <= 0) {
			return;
		}

		boolean isHorizontal = progressBar.getOrientation() == JProgressBar.HORIZONTAL;
		int arc = isHorizontal ? height : width;
		Graphics2D graphics2D = (Graphics2D) graphics;

		JewelUIUtils.setRenderingHints(graphics2D);

		graphics.setColor(progressBar.getBackground());
		graphics2D.fill(new RoundRectangle2D.Float(x, y, width, height, arc, arc));

		if (progressBar.isIndeterminate()) {
			paintIndeterminate(graphics2D, x, y, width, height, arc, insets);
		} else {
			paintDeterminate(graphics2D, component, x, y, width, height, arc, insets);
		}
	}

	private void paintIndeterminate(Graphics2D graphics, int x, int y, int width,
									int height, int arc, Insets insets) {
		boxRect = getBox(boxRect);
		if (boxRect != null) {
			graphics.setColor(progressBar.getForeground());
			graphics.fill(new RoundRectangle2D.Float(boxRect.x, boxRect.y,
				boxRect.width, boxRect.height, arc, arc));
		}

		if (progressBar.isStringPainted()) {
			paintString(graphics, x, y, width, height, 0, insets);
		}
	}

	private void paintDeterminate(Graphics2D graphics, JComponent component, int x, int y,
								  int width, int height, int arc, Insets insets) {
		int amountFull = getAmountFull(insets, width, height);
		boolean isHorizontal = progressBar.getOrientation() == JProgressBar.HORIZONTAL;

		graphics.setColor(progressBar.getForeground());
		if (isHorizontal) {
			int startX = component.getComponentOrientation().isLeftToRight() ?
				x : x + (width - amountFull);
			graphics.fill(new RoundRectangle2D.Float(startX, y, amountFull, height, arc, arc));
		} else {
			graphics.fill(new RoundRectangle2D.Float(x, y + (height - amountFull),
				width, amountFull, arc, arc));
		}

		if (progressBar.isStringPainted()) {
			paintString(graphics, x, y, width, height, amountFull, insets);
		}
	}
}
