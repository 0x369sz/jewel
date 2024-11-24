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
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * UI delegate for JScrollBar components in the Jewel Look and Feel.
 * Provides a modern scrollbar design with hover effects, custom thumb painting,
 * and proper UI scaling support.
 */
public class JewelScrollBarUI extends BasicScrollBarUI {

	private static final String KEY_HOVER_TRACK_COLOR = "ScrollBar.hoverTrackColor";
	private static final String KEY_HOVER_THUMB_COLOR = "ScrollBar.hoverThumbColor";

	protected Color hoverTrackColor;
	protected Color hoverThumbColor;

	private MouseAdapter hoverListener;
	private boolean hoverTrack;
	private boolean hoverThumb;
	private static boolean isPressed;

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelScrollBarUI();
	}

	@Override
	protected void installListeners() {
		super.installListeners();
		hoverListener = new ScrollBarHoverListener();
		scrollbar.addMouseListener(hoverListener);
		scrollbar.addMouseMotionListener(hoverListener);
	}

	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		scrollbar.removeMouseListener(hoverListener);
		scrollbar.removeMouseMotionListener(hoverListener);
		hoverListener = null;
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		hoverTrackColor = UIManager.getColor(KEY_HOVER_TRACK_COLOR);
		hoverThumbColor = UIManager.getColor(KEY_HOVER_THUMB_COLOR);
	}

	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();
		hoverTrackColor = null;
		hoverThumbColor = null;
	}

	@Override
	public Dimension getPreferredSize(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return UIScale.scale(super.getPreferredSize(component));
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		return createInvisibleButton();
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		return createInvisibleButton();
	}

	/**
	 * Creates an invisible button with zero size for use in the scrollbar.
	 * The button is non-focusable and has no preferred dimensions.
	 */
	private JButton createInvisibleButton() {
		JButton button = new JButton();
		Dimension emptyDimension = new Dimension();
		button.setMinimumSize(emptyDimension);
		button.setMaximumSize(emptyDimension);
		button.setPreferredSize(emptyDimension);
		button.setFocusable(false);
		button.setRequestFocusEnabled(false);
		return button;
	}

	@Override
	protected void paintDecreaseHighlight(Graphics graphics) {
		// Intentionally empty to remove default highlight painting
	}

	@Override
	protected void paintIncreaseHighlight(Graphics graphics) {
		// Intentionally empty to remove default highlight painting
	}

	@Override
	protected void paintTrack(Graphics graphics, JComponent component, Rectangle trackBounds) {
		Objects.requireNonNull(graphics, "Graphics cannot be null");
		Objects.requireNonNull(component, "Component cannot be null");
		Objects.requireNonNull(trackBounds, "TrackBounds cannot be null");

		graphics.setColor(hoverTrack ? hoverTrackColor : trackColor);
		graphics.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
	}

	@Override
	protected void paintThumb(Graphics graphics, JComponent component, Rectangle thumbBounds) {
		Objects.requireNonNull(graphics, "Graphics cannot be null");
		Objects.requireNonNull(component, "Component cannot be null");
		Objects.requireNonNull(thumbBounds, "ThumbBounds cannot be null");

		if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
			return;
		}

		graphics.setColor(hoverThumb ? hoverThumbColor : thumbColor);
		graphics.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
	}

	@Override
	protected Dimension getMinimumThumbSize() {
		return UIScale.scale(super.getMinimumThumbSize());
	}

	@Override
	protected Dimension getMaximumThumbSize() {
		return UIScale.scale(super.getMaximumThumbSize());
	}

	/**
	 * Handles mouse events for the scrollbar to implement hover effects
	 * for both the track and thumb components.
	 */
	private class ScrollBarHoverListener extends MouseAdapter {
		@Override
		public void mouseExited(MouseEvent event) {
			if (!isPressed) {
				hoverTrack = hoverThumb = false;
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent event) {
			if (!isPressed) {
				update(event.getX(), event.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent event) {
			isPressed = true;
		}

		@Override
		public void mouseReleased(MouseEvent event) {
			isPressed = false;
			update(event.getX(), event.getY());
		}

		private void update(int x, int y) {
			boolean inTrack = getTrackBounds().contains(x, y);
			boolean inThumb = getThumbBounds().contains(x, y);
			if (inTrack != hoverTrack || inThumb != hoverThumb) {
				hoverTrack = inTrack;
				hoverThumb = inThumb;
				repaint();
			}
		}

		private void repaint() {
			if (scrollbar.isEnabled()) {
				scrollbar.repaint();
			}
		}
	}
}
