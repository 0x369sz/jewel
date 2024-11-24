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

import com.slobodanzivanovic.jewel.laf.core.JewelLaf;
import com.slobodanzivanovic.jewel.laf.core.util.JewelUIUtils;
import com.slobodanzivanovic.jewel.laf.core.util.MigLayoutVisualPadding;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * UI delegate for JTabbedPane components in the Jewel Look and Feel.
 * Provides custom painting with configurable colors, borders, and scaling support,
 * implementing modern tab styling with underline indicators and hover effects.
 */
public class JewelTabbedPaneUI extends BasicTabbedPaneUI {

	private static final String KEY_DISABLED_FOREGROUND = "TabbedPane.disabledForeground";
	private static final String KEY_SELECTED_FOREGROUND = "TabbedPane.selectedForeground";
	private static final String KEY_UNDERLINE_COLOR = "TabbedPane.underlineColor";
	private static final String KEY_DISABLED_UNDERLINE_COLOR = "TabbedPane.disabledUnderlineColor";
	private static final String KEY_HOVER_COLOR = "TabbedPane.hoverColor";
	private static final String KEY_FOCUS_COLOR = "TabbedPane.focusColor";
	private static final String KEY_CONTENT_AREA_COLOR = "TabbedPane.contentAreaColor";
	private static final String KEY_TAB_HEIGHT = "TabbedPane.tabHeight";
	private static final String KEY_TAB_SELECTION_HEIGHT = "TabbedPane.tabSelectionHeight";
	private static final String KEY_CONTENT_SEPARATOR_HEIGHT = "TabbedPane.contentSeparatorHeight";
	private static final String KEY_HAS_FULL_BORDER = "TabbedPane.hasFullBorder";
	private static final String KEY_TABS_OVERLAP_BORDER = "TabbedPane.tabsOverlapBorder";
	private static final String KEY_COMPONENT_ARROW_TYPE = "Component.arrowType";
	private static final String KEY_TABBED_PANE_SHADOW = "TabbedPane.shadow";

	protected Color disabledForeground;
	protected Color selectedForeground;
	protected Color underlineColor;
	protected Color disabledUnderlineColor;
	protected Color hoverColor;
	protected Color focusColor;
	protected Color contentAreaColor;

	protected int tabHeight;
	protected int tabSelectionHeight;
	protected int contentSeparatorHeight;
	protected boolean hasFullBorder;
	protected boolean tabsOverlapBorder;

	/**
	 * Creates or returns the UI delegate for JTabbedPane components.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelTabbedPaneUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		disabledForeground = UIManager.getColor(KEY_DISABLED_FOREGROUND);
		selectedForeground = UIManager.getColor(KEY_SELECTED_FOREGROUND);
		underlineColor = UIManager.getColor(KEY_UNDERLINE_COLOR);
		disabledUnderlineColor = UIManager.getColor(KEY_DISABLED_UNDERLINE_COLOR);
		hoverColor = UIManager.getColor(KEY_HOVER_COLOR);
		focusColor = UIManager.getColor(KEY_FOCUS_COLOR);
		contentAreaColor = UIManager.getColor(KEY_CONTENT_AREA_COLOR);

		tabHeight = UIManager.getInt(KEY_TAB_HEIGHT);
		tabSelectionHeight = UIManager.getInt(KEY_TAB_SELECTION_HEIGHT);
		contentSeparatorHeight = UIManager.getInt(KEY_CONTENT_SEPARATOR_HEIGHT);
		hasFullBorder = UIManager.getBoolean(KEY_HAS_FULL_BORDER);
		tabsOverlapBorder = UIManager.getBoolean(KEY_TABS_OVERLAP_BORDER);

		textIconGap = scale(textIconGap);
		tabInsets = scale(tabInsets);
		selectedTabPadInsets = scale(selectedTabPadInsets);
		tabAreaInsets = scale(tabAreaInsets);
		tabHeight = scale(tabHeight);
		tabSelectionHeight = scale(tabSelectionHeight);
		contentSeparatorHeight = scale(contentSeparatorHeight);

		MigLayoutVisualPadding.install(tabPane, null);
	}

	@Override
	protected void uninstallDefaults() {
		MigLayoutVisualPadding.uninstall(tabPane);
		super.uninstallDefaults();
	}

	@Override
	protected PropertyChangeListener createPropertyChangeListener() {
		return new PropertyChangeHandler() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				super.propertyChange(event);

				if (KEY_HAS_FULL_BORDER.equals(event.getPropertyName())) {
					tabPane.revalidate();
					tabPane.repaint();
				}
			}
		};
	}

	@Override
	protected JButton createScrollButton(int direction) {
		return new JewelArrowButton(
			direction,
			UIManager.getString(KEY_COMPONENT_ARROW_TYPE),
			UIManager.getColor(KEY_TABBED_PANE_SHADOW),
			UIManager.getColor(KEY_DISABLED_FOREGROUND),
			null,
			UIManager.getColor(KEY_HOVER_COLOR)
		);
	}

	@Override
	protected void setRolloverTab(int index) {
		int oldIndex = getRolloverTab();
		super.setRolloverTab(index);

		if (index != oldIndex) {
			repaintTab(oldIndex);
			repaintTab(index);
		}
	}

	private void repaintTab(int tabIndex) {
		if (tabIndex < 0 || tabIndex >= tabPane.getTabCount()) {
			return;
		}

		Rectangle bounds = getTabBounds(tabPane, tabIndex);
		if (bounds != null) {
			tabPane.repaint(bounds);
		}
	}

	@Override
	protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
		int width = super.calculateTabWidth(tabPlacement, tabIndex, metrics) - 3;
		if (!isTopOrBottom(tabPlacement) && isScrollTabLayout()) {
			width += contentSeparatorHeight;
		}
		return width;
	}

	@Override
	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
		int height = super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) - 2;
		if (isTopOrBottom(tabPlacement) && isScrollTabLayout()) {
			height += contentSeparatorHeight;
		}
		return Math.max(tabHeight, height);
	}

	@Override
	protected Insets getContentBorderInsets(int tabPlacement) {
		boolean fullBorder = this.hasFullBorder ||
			(tabPane.getClientProperty(KEY_HAS_FULL_BORDER) == Boolean.TRUE);

		int separatorHeight = contentSeparatorHeight;
		Insets insets = fullBorder ?
			new Insets(separatorHeight, separatorHeight, separatorHeight, separatorHeight) :
			new Insets(separatorHeight, 0, 0, 0);

		if (isScrollTabLayout()) {
			insets.top = 0;
		}

		rotateInsets(insets, contentBorderInsets, tabPlacement);
		return contentBorderInsets;
	}

	@Override
	protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
		if (isScrollTabLayout() && !isTopOrBottom(tabPlacement)) {
			float shift = contentSeparatorHeight / 2f;
			return Math.round(tabPlacement == LEFT ? -shift : shift);
		}
		return 0;
	}

	@Override
	protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
		if (isScrollTabLayout() && isTopOrBottom(tabPlacement)) {
			float shift = contentSeparatorHeight / 2f;
			return Math.round(tabPlacement == TOP ? -shift : shift);
		}
		return 0;
	}

	@Override
	protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
		if (isScrollTabLayout()) {
			Rectangle bounds = g.getClipBounds();
			g.setColor(contentAreaColor);

			if (isTopOrBottom(tabPlacement)) {
				int y = (tabPlacement == TOP) ?
					bounds.y + bounds.height - contentSeparatorHeight : bounds.y;
				g.fillRect(bounds.x, y, bounds.width, contentSeparatorHeight);
			} else {
				int x = (tabPlacement == LEFT) ?
					bounds.x + bounds.width - contentSeparatorHeight : bounds.x;
				g.fillRect(x, bounds.y, contentSeparatorHeight, bounds.height);
			}
		}

		super.paintTabArea(g, tabPlacement, selectedIndex);
	}

	@Override
	protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics,
							 int tabIndex, String title, Rectangle textRect, boolean isSelected) {

		Objects.requireNonNull(g, "Graphics cannot be null");

		g.setFont(font);

		View view = getTextViewForTab(tabIndex);
		if (view != null) {
			view.paint(g, textRect);
			return;
		}

		Color color;
		if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
			color = tabPane.getForegroundAt(tabIndex);
			if (isSelected && (color instanceof UIResource) && selectedForeground != null) {
				color = selectedForeground;
			}
		} else {
			color = disabledForeground;
		}

		int mnemIndex = JewelLaf.isShowMnemonics() ?
			tabPane.getDisplayedMnemonicIndexAt(tabIndex) : -1;

		g.setColor(color);
		JewelUIUtils.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex,
			textRect.x, textRect.y + metrics.getAscent());
	}

	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
									  int x, int y, int w, int h, boolean isSelected) {

		Objects.requireNonNull(g, "Graphics cannot be null");

		if (isScrollTabLayout()) {
			if (isTopOrBottom(tabPlacement)) {
				if (tabPlacement == BOTTOM) {
					y += contentSeparatorHeight;
				}
				h -= contentSeparatorHeight;
			} else {
				if (tabPlacement == RIGHT) {
					x += contentSeparatorHeight;
				}
				w -= contentSeparatorHeight;
			}
		}

		boolean enabled = tabPane.isEnabled();
		boolean isRollover = getRolloverTab() == tabIndex;
		Color backgroundColor;

		if (enabled && tabPane.isEnabledAt(tabIndex) && isRollover) {
			backgroundColor = hoverColor;
		} else if (enabled && isSelected && tabPane.hasFocus()) {
			backgroundColor = focusColor;
		} else {
			backgroundColor = tabPane.getBackgroundAt(tabIndex);
		}

		g.setColor(backgroundColor);
		g.fillRect(x, y, w, h);
	}

	@Override
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
								  int x, int y, int w, int h, boolean isSelected) {

		Objects.requireNonNull(g, "Graphics cannot be null");

		if (!isSelected) {
			return;
		}

		g.setColor(tabPane.isEnabled() ? underlineColor : disabledUnderlineColor);
		Insets contentInsets = getContentBorderInsets(tabPlacement);

		switch (tabPlacement) {
			case TOP -> {
				int sy = y + h + contentInsets.top - tabSelectionHeight;
				g.fillRect(x, sy, w, tabSelectionHeight);
			}
			case BOTTOM -> g.fillRect(x, y - contentInsets.bottom, w, tabSelectionHeight);
			case LEFT -> {
				int sx = x + w + contentInsets.left - tabSelectionHeight;
				g.fillRect(sx, y, tabSelectionHeight, h);
			}
			case RIGHT -> g.fillRect(x - contentInsets.right, y, tabSelectionHeight, h);
		}
	}

	@Override
	protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
		Objects.requireNonNull(g, "Graphics cannot be null");

		if (tabPane.getTabCount() <= 0) {
			return;
		}

		Insets insets = tabPane.getInsets();
		Insets tabAreaInsets = getTabAreaInsets(tabPlacement);

		int x = insets.left;
		int y = insets.top;
		int w = tabPane.getWidth() - insets.right - insets.left;
		int h = tabPane.getHeight() - insets.top - insets.bottom;

		switch (tabPlacement) {
			case LEFT -> {
				x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
				if (tabsOverlapBorder) {
					x -= tabAreaInsets.right;
				}
				w -= (x - insets.left);
			}
			case RIGHT -> {
				w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
				if (tabsOverlapBorder) {
					w += tabAreaInsets.left;
				}
			}
			case BOTTOM -> {
				h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
				if (tabsOverlapBorder) {
					h += tabAreaInsets.top;
				}
			}
			case TOP -> {
				y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
				if (tabsOverlapBorder) {
					y -= tabAreaInsets.bottom;
				}
				h -= (y - insets.top);
			}
		}

		g.setColor(contentAreaColor);
		g.fillRect(x, y, w, h);
	}

	@Override
	protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
									   Rectangle iconRect, Rectangle textRect, boolean isSelected) {
	}

	private boolean isScrollTabLayout() {
		return tabPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT;
	}

	private boolean isTopOrBottom(int tabPlacement) {
		return tabPlacement == TOP || tabPlacement == BOTTOM;
	}
}
