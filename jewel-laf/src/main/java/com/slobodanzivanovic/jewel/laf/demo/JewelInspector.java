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

package com.slobodanzivanovic.jewel.laf.demo;

import com.slobodanzivanovic.jewel.laf.core.ui.JewelToolTipUI;
import com.slobodanzivanovic.jewel.laf.core.util.UIScale;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.lang.reflect.Field;

public class JewelInspector {
	private static final Integer HIGHLIGHT_LAYER = 401;
	private static final Integer TOOLTIP_LAYER = 402;

	private final JRootPane rootPane;
	private Component lastComponent;
	private int lastX;
	private int lastY;
	private JComponent highlightFigure;
	private JToolTip tip;

	public JewelInspector(JRootPane rootPane) {
		this.rootPane = rootPane;

		rootPane.getGlassPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				lastX = e.getX();
				lastY = e.getY();
				inspect(lastX, lastY);
			}
		});
	}

	public void setEnabled(boolean enabled) {
		rootPane.getGlassPane().setVisible(enabled);

		if (!enabled) {
			lastComponent = null;

			if (highlightFigure != null) {
				highlightFigure.getParent().remove(highlightFigure);
				highlightFigure = null;
			}

			if (tip != null) {
				tip.getParent().remove(tip);
				tip = null;
			}
		}
	}

	public void update() {
		if (!rootPane.getGlassPane().isVisible()) {
			return;
		}

		EventQueue.invokeLater(() -> {
			setEnabled(false);
			setEnabled(true);
			inspect(lastX, lastY);
		});
	}

	private void inspect(int x, int y) {
		Container contentPane = rootPane.getContentPane();
		Component c = SwingUtilities.getDeepestComponentAt(contentPane, x, y);
		if (c == contentPane || (c != null && c.getParent() == contentPane)) {
			c = null;
		}

		if (c == lastComponent) {
			return;
		}

		lastComponent = c;
		highlight(c);
		showToolTip(c, x, y);
	}

	private void highlight(Component c) {
		if (highlightFigure == null) {
			highlightFigure = createHighlightFigure();
			rootPane.getLayeredPane().add(highlightFigure, HIGHLIGHT_LAYER);
		}

		highlightFigure.setVisible(c != null);

		if (c != null) {
			Rectangle bounds = c.getBounds();
			Rectangle highlightBounds = SwingUtilities.convertRectangle(
				c.getParent(), bounds, rootPane);
			highlightFigure.setBounds(highlightBounds);
		}
	}

	private JComponent createHighlightFigure() {
		JComponent c = new JComponent() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		c.setBackground(new Color(255, 0, 0, 32));
		c.setBorder(new LineBorder(Color.red));
		return c;
	}

	private void showToolTip(Component c, int x, int y) {
		if (c == null) {
			if (tip != null) {
				tip.setVisible(false);
			}
			return;
		}

		if (tip == null) {
			tip = new JToolTip() {
				@Override
				public void updateUI() {
					setUI(new JewelToolTipUI());
				}
			};
			rootPane.getLayeredPane().add(tip, TOOLTIP_LAYER);
		} else {
			tip.setVisible(true);
		}

		tip.setTipText(buildToolTipText(c));

		// Position tooltip
		int tx = x + UIScale.scale(8);
		int ty = y + UIScale.scale(16);
		Dimension size = tip.getPreferredSize();

		// Keep tooltip within visible area
		Rectangle visibleRect = rootPane.getVisibleRect();
		if (tx + size.width > visibleRect.x + visibleRect.width) {
			tx = visibleRect.x + visibleRect.width - size.width;
		}
		if (ty + size.height > visibleRect.y + visibleRect.height) {
			ty = visibleRect.y + visibleRect.height - size.height;
		}
		if (tx < visibleRect.x) {
			tx = visibleRect.x;
		}
		if (ty < visibleRect.y) {
			ty = visibleRect.y;
		}

		tip.setBounds(tx, ty, size.width, size.height);
		tip.repaint();
	}

	private String buildToolTipText(Component c) {
		StringBuilder text = new StringBuilder();

		// Basic component info
		text.append("Class: ").append(c.getClass().getSimpleName())
			.append(" (").append(c.getClass().getPackage().getName()).append(")\n");

		// Size and position
		text.append(String.format("Size: %d,%d  @ %d,%d\n",
			c.getWidth(), c.getHeight(), c.getX(), c.getY()));

		// Container insets
		if (c instanceof Container) {
			text.append("Insets: ").append(toString(((Container) c).getInsets())).append('\n');
		}

		// Margin info for specific components
		Insets margin = null;
		switch (c) {
			case AbstractButton abstractButton -> margin = abstractButton.getMargin();
			case JTextComponent jTextComponent -> margin = jTextComponent.getMargin();
			case JMenuBar jMenuBar -> margin = jMenuBar.getMargin();
			case JToolBar jToolBar -> margin = jToolBar.getMargin();
			default -> {
			}
		}
		if (margin != null) {
			text.append("Margin: ").append(toString(margin)).append('\n');
		}

		// Size constraints
		Dimension prefSize = c.getPreferredSize();
		Dimension minSize = c.getMinimumSize();
		Dimension maxSize = c.getMaximumSize();
		text.append(String.format("Preferred size: %d,%d\n", prefSize.width, prefSize.height));
		text.append(String.format("Minimum size: %d,%d\n", minSize.width, minSize.height));
		text.append(String.format("Maximum size: %d,%d\n", maxSize.width, maxSize.height));

		// Border info for JComponents
		if (c instanceof JComponent) {
			text.append("Border: ").append(toString(((JComponent) c).getBorder())).append('\n');
		}

		// Colors and font
		text.append("Background: ").append(toString(c.getBackground())).append('\n');
		text.append("Foreground: ").append(toString(c.getForeground())).append('\n');
		text.append("Font: ").append(toString(c.getFont())).append('\n');

		// UI class info for JComponents
		if (c instanceof JComponent) {
			try {
				Field f = JComponent.class.getDeclaredField("ui");
				f.setAccessible(true);
				Object ui = f.get(c);
				text.append("UI: ").append(ui != null ? ui.getClass().getName() : "null").append('\n');
			} catch (Exception ex) {
				// Ignore reflection errors
			}
		}

		// Parent info
		text.append("Parent: ").append(c.getParent().getClass().getName());

		return text.toString();
	}

	private static String toString(Insets insets) {
		if (insets == null) return "null";
		return String.format("%d,%d,%d,%d",
			insets.top, insets.left, insets.bottom, insets.right);
	}

	private static String toString(Color c) {
		if (c == null) return "null";
		String s = String.format("%08x", c.getRGB() & 0xffffffffL);
		if (c instanceof UIResource) s += " UI";
		return s;
	}

	private static String toString(Font f) {
		if (f == null) return "null";
		return String.format("%s %d %d%s",
			f.getFamily(), f.getSize(), f.getStyle(),
			f instanceof UIResource ? " UI" : "");
	}

	private static String toString(Border b) {
		if (b == null) return "null";
		String s = b.getClass().getName();
		if (b instanceof EmptyBorder) {
			s += '(' + toString(((EmptyBorder) b).getBorderInsets()) + ')';
		}
		if (b instanceof UIResource) s += " UI";
		return s;
	}
}
