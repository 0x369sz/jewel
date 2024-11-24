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
import javax.swing.plaf.basic.BasicColorChooserUI;
import java.awt.*;

/**
 * Jewel Look and Feel UI delegate for JColorChooser.
 */
public class JewelColorChooserUI extends BasicColorChooserUI {

	private static final String SWATCH_SIZE_KEY = "ColorChooser.swatchesSwatchSize";
	private static final String RECENT_SWATCH_SIZE_KEY = "ColorChooser.swatchesRecentSwatchSize";

	/**
	 * Creates a new UI delegate for JColorChooser.
	 *
	 * @param c the component to create UI for
	 * @return new instance of JewelColorChooserUI
	 */
	public static ComponentUI createUI(JComponent c) {
		return new JewelColorChooserUI();
	}

	@Override
	public void installUI(JComponent c) {
		if (requiresScaling()) {
			installWithScaling(c);
		} else {
			super.installUI(c);
		}
	}

	private boolean requiresScaling() {
		return UIScale.getUserScaleFactor() != 1f;
	}

	private void installWithScaling(JComponent c) {
		Dimension swatchSize = getAndScaleSize(SWATCH_SIZE_KEY);
		Dimension recentSwatchSize = getAndScaleSize(RECENT_SWATCH_SIZE_KEY);

		try {
			setTemporaryUIValue(SWATCH_SIZE_KEY, swatchSize);
			setTemporaryUIValue(RECENT_SWATCH_SIZE_KEY, recentSwatchSize);
			super.installUI(c);
		} finally {
			clearTemporaryUIValue(SWATCH_SIZE_KEY);
			clearTemporaryUIValue(RECENT_SWATCH_SIZE_KEY);
		}
	}

	private Dimension getAndScaleSize(String key) {
		Dimension size = UIManager.getDimension(key);
		return size != null ? UIScale.scale(size) : null;
	}

	private void setTemporaryUIValue(String key, Dimension value) {
		if (value != null) {
			UIManager.put(key, value);
		}
	}

	private void clearTemporaryUIValue(String key) {
		UIManager.put(key, null);
	}
}
