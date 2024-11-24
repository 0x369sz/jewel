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
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

/**
 * UI delegate for JSlider components in the Jewel Look and Feel.
 * Provides custom painting with hover effects and scaling support.
 */
public class JewelSliderUI extends BasicSliderUI {
	private int trackWidth;
	private int thumbWidth;

	private Color trackColor;
	private Color thumbColor;
	private Color focusColor;
	private Color hoverColor;
	private Color disabledForeground;

	private MouseListener hoverListener;
	private boolean hover;

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param c the component to create the UI delegate for
	 * @return the UI delegate instance
	 * @throws NullPointerException if c is null
	 */
	public static ComponentUI createUI(JComponent c) {
		Objects.requireNonNull(c, "Component cannot be null");
		return new JewelSliderUI();
	}

	public JewelSliderUI() {
		super(null);
	}

	@Override
	protected void installListeners(JSlider slider) {
		Objects.requireNonNull(slider, "Slider cannot be null");
		super.installListeners(slider);

		hoverListener = new JewelUIUtils.HoverListener(slider, h -> hover = h);
		slider.addMouseListener(hoverListener);
	}

	@Override
	protected void uninstallListeners(JSlider slider) {
		super.uninstallListeners(slider);

		slider.removeMouseListener(hoverListener);
		hoverListener = null;
	}

	@Override
	protected void installDefaults(JSlider slider) {
		super.installDefaults(slider);

		trackWidth = UIManager.getInt("Slider.trackWidth");
		thumbWidth = UIManager.getInt("Slider.thumbWidth");

		trackColor = UIManager.getColor("Slider.trackColor");
		thumbColor = UIManager.getColor("Slider.thumbColor");
		focusColor = UIManager.getColor("Slider.focusedColor");
		hoverColor = JewelUIUtils.getUIColor("Slider.hoverColor", focusColor);
		disabledForeground = UIManager.getColor("Slider.disabledForeground");
	}

	@Override
	protected void uninstallDefaults(JSlider slider) {
		super.uninstallDefaults(slider);

		trackColor = null;
		thumbColor = null;
		focusColor = null;
		hoverColor = null;
		disabledForeground = null;
	}

	@Override
	public Dimension getPreferredHorizontalSize() {
		return UIScale.scale(super.getPreferredHorizontalSize());
	}

	@Override
	public Dimension getPreferredVerticalSize() {
		return UIScale.scale(super.getPreferredVerticalSize());
	}

	@Override
	public Dimension getMinimumHorizontalSize() {
		return UIScale.scale(super.getMinimumHorizontalSize());
	}

	@Override
	public Dimension getMinimumVerticalSize() {
		return UIScale.scale(super.getMinimumVerticalSize());
	}

	@Override
	protected int getTickLength() {
		return UIScale.scale(super.getTickLength());
	}

	@Override
	protected Dimension getThumbSize() {
		return new Dimension(UIScale.scale(thumbWidth), UIScale.scale(thumbWidth));
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		JewelUIUtils.setRenderingHints((Graphics2D) g);

		super.paint(g, c);
	}

	@Override
	public void paintFocus(Graphics g) {
	}

	@Override
	public void paintTrack(Graphics g) {
		boolean enabled = slider.isEnabled();
		float tw = UIScale.scale((float) trackWidth);

		RoundRectangle2D coloredTrack = null;
		RoundRectangle2D track;
		if (slider.getOrientation() == JSlider.HORIZONTAL) {
			float y = trackRect.y + (trackRect.height - tw) / 2f;
			if (enabled && isRoundThumb()) {
				int cw = thumbRect.x + (thumbRect.width / 2) - trackRect.x;
				coloredTrack = new RoundRectangle2D.Float(trackRect.x, y, cw, tw, tw, tw);
				track = new RoundRectangle2D.Float(trackRect.x + cw, y, trackRect.width - cw, tw, tw, tw);
			} else track = new RoundRectangle2D.Float(trackRect.x, y, trackRect.width, tw, tw, tw);
		} else {
			float x = trackRect.x + (trackRect.width - tw) / 2f;
			if (enabled && isRoundThumb()) {
				int ch = thumbRect.y + (thumbRect.height / 2) - trackRect.y;
				track = new RoundRectangle2D.Float(x, trackRect.y, tw, ch, tw, tw);
				coloredTrack = new RoundRectangle2D.Float(x, trackRect.y + ch, tw, trackRect.height - ch, tw, tw);
			} else track = new RoundRectangle2D.Float(x, trackRect.y, tw, trackRect.height, tw, tw);
		}

		if (coloredTrack != null) {
			g.setColor(slider.hasFocus() ? focusColor : (hover ? hoverColor : thumbColor));
			((Graphics2D) g).fill(coloredTrack);
		}

		g.setColor(enabled ? trackColor : disabledForeground);
		((Graphics2D) g).fill(track);
	}

	@Override
	public void paintThumb(Graphics g) {
		g.setColor(slider.isEnabled() ? (slider.hasFocus() ? focusColor : (hover ? hoverColor : thumbColor)) : disabledForeground);

		if (isRoundThumb()) g.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
		else {
			double w = thumbRect.width;
			double h = thumbRect.height;
			double wh = w / 2;

			Path2D thumb = JewelUIUtils.createPath(0, 0, w, 0, w, (h - wh), wh, h, 0, (h - wh));

			Graphics2D g2 = (Graphics2D) g.create();
			try {
				g2.translate(thumbRect.x, thumbRect.y);
				if (slider.getOrientation() == JSlider.VERTICAL) {
					if (slider.getComponentOrientation().isLeftToRight()) {
						g2.translate(0, thumbRect.height);
						g2.rotate(Math.toRadians(270));
					} else {
						g2.translate(thumbRect.width, 0);
						g2.rotate(Math.toRadians(90));
					}
				}
				g2.fill(thumb);
			} finally {
				g2.dispose();
			}
		}
	}

	private boolean isRoundThumb() {
		return !slider.getPaintTicks() && !slider.getPaintLabels();
	}
}
