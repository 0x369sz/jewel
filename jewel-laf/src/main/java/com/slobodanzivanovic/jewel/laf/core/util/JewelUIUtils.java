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

package com.slobodanzivanovic.jewel.laf.core.util;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Utility class providing common UI operations for the Jewel Look and Feel.
 * Handles graphics operations, UI resource management, and component styling.
 */
public final class JewelUIUtils {
	private static final boolean MAC_USE_QUARTZ = Boolean.getBoolean("apple.awt.graphics.UseQuartz");
	private static final ConcurrentHashMap<String, Object> uiCache = new ConcurrentHashMap<>();

	private JewelUIUtils() {
		throw new AssertionError("No JewelUIUtils instances for you!");
	}

	/**
	 * Expands a rectangle by the specified insets.
	 *
	 * @param r      The original rectangle
	 * @param insets The insets to add
	 * @return A new rectangle with added insets
	 */
	public static Rectangle addInsets(Rectangle r, Insets insets) {
		if (r == null || insets == null) {
			throw new IllegalArgumentException("Rectangle and insets cannot be null");
		}
		return new Rectangle(
			r.x - insets.left,
			r.y - insets.top,
			r.width + insets.left + insets.right,
			r.height + insets.top + insets.bottom
		);
	}

	/**
	 * Shrinks a rectangle by the specified insets.
	 *
	 * @param r      The original rectangle
	 * @param insets The insets to subtract
	 * @return A new rectangle with subtracted insets
	 */
	public static Rectangle subtractInsets(Rectangle r, Insets insets) {
		if (r == null || insets == null) {
			throw new IllegalArgumentException("Rectangle and insets cannot be null");
		}
		return new Rectangle(
			r.x + insets.left,
			r.y + insets.top,
			r.width - insets.left - insets.right,
			r.height - insets.top - insets.bottom
		);
	}

	/**
	 * Retrieves a UI color, with caching for better performance.
	 *
	 * @param key             The UI manager key
	 * @param defaultColorRGB The default RGB value if the key is not found
	 * @return The color from UI manager or default
	 */
	public static Color getUIColor(String key, int defaultColorRGB) {
		return (Color) uiCache.computeIfAbsent(key, k ->
			Optional.ofNullable(UIManager.getColor(k))
				.orElse(new Color(defaultColorRGB)));
	}

	/**
	 * Retrieves a UI color with a Color object as default.
	 *
	 * @param key          The UI manager key
	 * @param defaultColor The default color if the key is not found
	 * @return The color from UI manager or default
	 */
	public static Color getUIColor(String key, Color defaultColor) {
		return (Color) uiCache.computeIfAbsent(key, k ->
			Optional.ofNullable(UIManager.getColor(k))
				.orElse(defaultColor));
	}

	/**
	 * Retrieves an integer value from UI manager.
	 *
	 * @param key          The UI manager key
	 * @param defaultValue The default value if the key is not found
	 * @return The integer value from UI manager or default
	 */
	public static int getUIInt(String key, int defaultValue) {
		return (int) uiCache.computeIfAbsent(key, k -> {
			Object value = UIManager.get(k);
			return (value instanceof Integer) ? value : defaultValue;
		});
	}

	/**
	 * Converts a ColorUIResource to a regular Color.
	 *
	 * @param c The color to convert
	 * @return A non-UIResource version of the color
	 */
	public static Color nonUIResource(Color c) {
		return (c instanceof ColorUIResource) ? new Color(c.getRGB(), true) : c;
	}

	/**
	 * Configures rendering hints for high-quality graphics.
	 *
	 * @param g The Graphics2D context
	 */
	public static void setRenderingHints(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
			MAC_USE_QUARTZ ? RenderingHints.VALUE_STROKE_PURE : RenderingHints.VALUE_STROKE_NORMALIZE);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}

	/**
	 * Draws a rounded rectangle with border.
	 *
	 * @param g          Graphics context
	 * @param x          X coordinate
	 * @param y          Y coordinate
	 * @param width      Width of rectangle
	 * @param height     Height of rectangle
	 * @param focusWidth Focus border width
	 * @param lineWidth  Line width
	 * @param arc        Arc radius
	 */
	public static void drawRoundRectangle(Graphics2D g, int x, int y, int width, int height,
										  float focusWidth, float lineWidth, float arc) {
		float arc2 = Math.max(0f, arc - lineWidth);

		RoundRectangle2D.Float r1 = new RoundRectangle2D.Float(
			x + focusWidth, y + focusWidth,
			width - focusWidth * 2, height - focusWidth * 2,
			arc, arc
		);
		RoundRectangle2D.Float r2 = new RoundRectangle2D.Float(
			r1.x + lineWidth, r1.y + lineWidth,
			r1.width - lineWidth * 2, r1.height - lineWidth * 2,
			arc2, arc2
		);

		Path2D border = new Path2D.Float(Path2D.WIND_EVEN_ODD);
		border.append(r1, false);
		border.append(r2, false);
		g.fill(border);
	}

	/**
	 * Fills a rounded rectangle.
	 *
	 * @param g          Graphics context
	 * @param x          X coordinate
	 * @param y          Y coordinate
	 * @param width      Width of rectangle
	 * @param height     Height of rectangle
	 * @param focusWidth Focus border width
	 * @param arc        Arc radius
	 */
	public static void fillRoundRectangle(Graphics2D g, int x, int y, int width, int height,
										  float focusWidth, float arc) {
		g.fill(new RoundRectangle2D.Float(
			x + focusWidth, y + focusWidth,
			width - focusWidth * 2, height - focusWidth * 2,
			arc, arc
		));
	}

	/**
	 * Paints the parent background of a component.
	 *
	 * @param g Graphics context
	 * @param c Component
	 */
	public static void paintParentBackground(Graphics g, JComponent c) {
		findOpaqueParent(c).ifPresent(parent -> {
			g.setColor(parent.getBackground());
			g.fillRect(0, 0, c.getWidth(), c.getHeight());
		});
	}

	/**
	 * Finds the first opaque parent container.
	 *
	 * @param c Starting container
	 * @return Optional containing the first opaque parent, or empty if none found
	 */
	private static Optional<Container> findOpaqueParent(Container c) {
		Container parent = c;
		while ((parent = parent.getParent()) != null) {
			if (parent.isOpaque()) return Optional.of(parent);
		}
		return Optional.empty();
	}

	/**
	 * Paints an outline border with rounded corners.
	 *
	 * @param g          Graphics context
	 * @param x          X coordinate
	 * @param y          Y coordinate
	 * @param width      Width of border
	 * @param height     Height of border
	 * @param focusWidth Focus border width
	 * @param lineWidth  Line width
	 * @param arc        Arc radius
	 */
	public static void paintOutlineBorder(Graphics2D g, int x, int y, int width, int height,
										  float focusWidth, float lineWidth, float arc) {
		float outerArc = (arc > 0) ? arc + focusWidth - UIScale.scale(2f) : focusWidth;
		float ow = focusWidth + lineWidth;

		Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
		path.append(createOutlinePath(x, y, width, height, outerArc), false);
		path.append(createOutlinePath(x + ow, y + ow, width - (ow * 2), height - (ow * 2),
			outerArc - ow), false);
		g.fill(path);
	}

	/**
	 * Creates an outline path for borders.
	 *
	 * @param x      X coordinate
	 * @param y      Y coordinate
	 * @param width  Width of path
	 * @param height Height of path
	 * @param arc    Arc radius
	 * @return Shape representing the outline path
	 */
	private static Shape createOutlinePath(float x, float y, float width, float height, float arc) {
		if (arc <= 0) return new Rectangle2D.Float(x, y, width, height);

		float x2 = x + width;
		float y2 = y + height;

		Path2D rect = new Path2D.Float();
		rect.moveTo(x2 - arc, y);
		rect.quadTo(x2, y, x2, y + arc);
		rect.lineTo(x2, y2 - arc);
		rect.quadTo(x2, y2, x2 - arc, y2);
		rect.lineTo(x + arc, y2);
		rect.quadTo(x, y2, x, y2 - arc);
		rect.lineTo(x, y + arc);
		rect.quadTo(x, y, x + arc, y);
		rect.closePath();

		return rect;
	}

	/**
	 * Creates a path from a series of points.
	 *
	 * @param points Series of x,y coordinates
	 * @return The created path
	 */
	public static Path2D createPath(double... points) {
		return createPath(true, points);
	}

	/**
	 * Creates a path from a series of points with optional closure.
	 *
	 * @param close  Whether to close the path
	 * @param points Series of x,y coordinates
	 * @return The created path
	 */
	public static Path2D createPath(boolean close, double... points) {
		if (points.length < 4 || points.length % 2 != 0) {
			throw new IllegalArgumentException("Points array must have even length >= 4");
		}

		Path2D path = new Path2D.Float();
		path.moveTo(points[0], points[1]);
		for (int i = 2; i < points.length; i += 2)
			path.lineTo(points[i], points[i + 1]);
		if (close) path.closePath();
		return path;
	}

	/**
	 * Draws a string with an optional underline for a specified character.
	 *
	 * @param c               Component
	 * @param g               Graphics context
	 * @param text            Text to draw
	 * @param underlinedIndex Index of character to underline (-1 for none)
	 * @param x               X coordinate
	 * @param y               Y coordinate
	 */
	public static void drawStringUnderlineCharAt(JComponent c, Graphics g,
												 String text, int underlinedIndex, int x, int y) {
		if (!(g instanceof Graphics2D g2d)) {
			throw new IllegalArgumentException("Graphics object must be instance of Graphics2D");
		}
		BasicGraphicsUtils.drawStringUnderlineCharAt(c, g2d, text, underlinedIndex, x, y);
	}

	/**
	 * MouseAdapter for handling hover states with automatic repainting.
	 */
	public static class HoverListener extends MouseAdapter {
		private final JComponent repaintComponent;
		private final Consumer<Boolean> hoverChanged;
		private final long hoverDelay;
		private Timer hoverTimer;

		/**
		 * Creates a new HoverListener with default instant hover.
		 *
		 * @param repaintComponent Component to repaint
		 * @param hoverChanged     Consumer to handle hover state changes
		 */
		public HoverListener(JComponent repaintComponent, Consumer<Boolean> hoverChanged) {
			this(repaintComponent, hoverChanged, 0);
		}

		/**
		 * Creates a new HoverListener with specified hover delay.
		 *
		 * @param repaintComponent Component to repaint
		 * @param hoverChanged     Consumer to handle hover state changes
		 * @param hoverDelay       Delay in milliseconds before hover takes effect
		 */
		public HoverListener(JComponent repaintComponent, Consumer<Boolean> hoverChanged, long hoverDelay) {
			this.repaintComponent = repaintComponent;
			this.hoverChanged = hoverChanged;
			this.hoverDelay = hoverDelay;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (hoverDelay > 0) {
				if (hoverTimer != null && hoverTimer.isRunning()) {
					hoverTimer.stop();
				}
				hoverTimer = new Timer((int) hoverDelay, evt -> {
					hoverChanged.accept(true);
					repaint();
				});
				hoverTimer.setRepeats(false);
				hoverTimer.start();
			} else {
				hoverChanged.accept(true);
				repaint();
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (hoverTimer != null && hoverTimer.isRunning()) {
				hoverTimer.stop();
			}
			hoverChanged.accept(false);
			repaint();
		}

		private void repaint() {
			if (repaintComponent != null && repaintComponent.isEnabled()) {
				repaintComponent.repaint();
			}
		}
	}
}
