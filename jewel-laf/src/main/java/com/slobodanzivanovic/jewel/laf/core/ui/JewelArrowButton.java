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

import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * A customizable arrow button component that supports both arrow and chevron styles.
 *
 * <p>The button supports different directions (NORTH, SOUTH, EAST, WEST) and can be
 * styled with custom colors for various states (normal, disabled, hover).
 */
public class JewelArrowButton extends BasicArrowButton implements UIResource {

	private static final String CHEVRON_TYPE = "chevron";
	private static final float STROKE_WIDTH = 1f;
	private static final int DEFAULT_CHEVRON_WIDTH = 8;
	private static final int DEFAULT_CHEVRON_HEIGHT = 4;
	private static final int DEFAULT_ARROW_WIDTH = 9;
	private static final int DEFAULT_ARROW_HEIGHT = 5;

	private final boolean chevron;
	private final Color foreground;
	private final Color disabledForeground;
	private final Color hoverForeground;
	private final Color hoverBackground;

	private int xOffset = 0;
	private int yOffset = 0;
	private volatile boolean hover;

	/**
	 * Creates a new JewelArrowButton with the specified properties.
	 *
	 * @param direction          The direction of the arrow (NORTH, SOUTH, EAST, WEST)
	 * @param type               The type of the button ("chevron" for chevron style, any other value for arrow style)
	 * @param foreground         The normal state color of the arrow
	 * @param disabledForeground The color of the arrow when the button is disabled
	 * @param hoverForeground    The color of the arrow when hovered (can be null for no hover effect)
	 * @param hoverBackground    The background color when hovered (can be null for no hover effect)
	 * @throws IllegalArgumentException if foreground or disabledForeground is null
	 */
	public JewelArrowButton(int direction, String type, Color foreground, Color disabledForeground,
							Color hoverForeground, Color hoverBackground) {
		super(direction, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);

		// Validate required colors
		if (foreground == null) {
			throw new IllegalArgumentException("Foreground color cannot be null");
		}
		if (disabledForeground == null) {
			throw new IllegalArgumentException("Disabled foreground color cannot be null");
		}

		this.chevron = CHEVRON_TYPE.equals(type);
		this.foreground = foreground;
		this.disabledForeground = disabledForeground;
		this.hoverForeground = hoverForeground;
		this.hoverBackground = hoverBackground;

		initializeButton();
	}

	private void initializeButton() {
		setOpaque(false);
		setBorder(null);

		if (hoverForeground != null || hoverBackground != null) {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					hover = true;
					repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					hover = false;
					repaint();
				}
			});
		}
	}

	protected boolean isHover() {
		return hover;
	}

	public int getXOffset() {
		return xOffset;
	}

	public void setXOffset(int xOffset) {
		if (this.xOffset != xOffset) {
			this.xOffset = xOffset;
			repaint();
		}
	}

	public int getYOffset() {
		return yOffset;
	}

	public void setYOffset(int yOffset) {
		if (this.yOffset != yOffset) {
			this.yOffset = yOffset;
			repaint();
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return scale(super.getPreferredSize());
	}

	@Override
	public Dimension getMinimumSize() {
		return scale(super.getMinimumSize());
	}

	@Override
	public boolean isFocusable() {
		return false;
	}

	@Override
	public void updateUI() {
		super.updateUI();
		setOpaque(false);
		setBorder(null);
	}

	@Override
	public void paint(Graphics g) {
		if (g == null) return;

		Graphics2D g2 = (Graphics2D) g.create();
		try {
			paintComponent(g2);
		} finally {
			g2.dispose();
		}
	}

	private void paintComponent(Graphics2D g2) {
		JewelUIUtils.setRenderingHints(g2);

		paintBackground(g2);
		paintArrow(g2);
	}

	private void paintBackground(Graphics2D g2) {
		if (isEnabled() && isHover() && hoverBackground != null) {
			g2.setColor(hoverBackground);
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	private void paintArrow(Graphics2D g2) {
		int width = getWidth();
		int height = getHeight();
		int direction = getDirection();
		boolean vert = (direction == NORTH || direction == SOUTH);

		int w = scale(chevron ? DEFAULT_CHEVRON_WIDTH : DEFAULT_ARROW_WIDTH);
		int h = scale(chevron ? DEFAULT_CHEVRON_HEIGHT : DEFAULT_ARROW_HEIGHT);
		int rw = vert ? w : h;
		int rh = vert ? h : w;

		int x = Math.round((width - rw) / 2f + scale((float) xOffset));
		int y = Math.round((height - rh) / 2f + scale((float) yOffset));

		if (x + rw >= width && x > 0) x--;
		if (y + rh >= height && y > 0) y--;

		g2.setColor(determineArrowColor());

		g2.translate(x, y);
		Shape arrowShape = createArrowShape(direction, chevron, w, h);

		if (chevron) {
			g2.setStroke(new BasicStroke(scale(STROKE_WIDTH)));
			g2.draw(arrowShape);
		} else {
			g2.fill(arrowShape);
		}
		g2.translate(-x, -y);
	}

	private Color determineArrowColor() {
		if (!isEnabled()) {
			return disabledForeground;
		}
		if (isHover() && hoverForeground != null) {
			return hoverForeground;
		}
		return foreground;
	}

	/**
	 * Creates an arrow or chevron shape based on the specified parameters.
	 *
	 * @param direction The direction of the arrow (NORTH, SOUTH, EAST, WEST)
	 * @param chevron   Whether to create a chevron instead of a solid arrow
	 * @param w         The width of the shape
	 * @param h         The height of the shape
	 * @return The created shape
	 * @throws IllegalArgumentException if direction is invalid or dimensions are not positive
	 */
	public static Shape createArrowShape(int direction, boolean chevron, int w, int h) {
		if (w <= 0 || h <= 0) {
			throw new IllegalArgumentException("Width and height must be positive");
		}

		return switch (direction) {
			case NORTH -> createNorthShape(chevron, w, h);
			case SOUTH -> createSouthShape(chevron, w, h);
			case WEST -> createWestShape(chevron, w, h);
			case EAST -> createEastShape(chevron, w, h);
			default -> throw new IllegalArgumentException("Invalid direction: " + direction);
		};
	}

	private static Shape createNorthShape(boolean chevron, int w, int h) {
		return JewelUIUtils.createPath(!chevron, 0, h, (w / 2f), 0, w, h);
	}

	private static Shape createSouthShape(boolean chevron, int w, int h) {
		return JewelUIUtils.createPath(!chevron, 0, 0, (w / 2f), h, w, 0);
	}

	private static Shape createWestShape(boolean chevron, int w, int h) {
		return JewelUIUtils.createPath(!chevron, h, 0, 0, (w / 2f), h, w);
	}

	private static Shape createEastShape(boolean chevron, int w, int h) {
		return JewelUIUtils.createPath(!chevron, 0, 0, h, (w / 2f), 0, w);
	}
}
