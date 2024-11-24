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
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

import static com.slobodanzivanovic.jewel.laf.core.JewelClientProperties.*;
import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * ButtonUI implementation for the Jewel Look and Feel.
 * Provides custom rendering for buttons including default, help, and toolbar buttons.
 */
public class JewelButtonUI extends BasicButtonUI {

	// UI Properties
	protected int focusWidth;
	protected int arc;
	protected int minimumWidth;
	protected int iconTextGap;

	// Regular button colors
	protected Color focusedBackground;
	protected Color hoverBackground;
	protected Color pressedBackground;
	protected Color disabledText;

	// Default button colors
	protected Color defaultBackground;
	protected Color defaultForeground;
	protected Color defaultFocusedBackground;
	protected Color defaultHoverBackground;
	protected Color defaultPressedBackground;
	protected boolean defaultBoldText;

	// Toolbar button colors
	protected Color toolbarHoverBackground;
	protected Color toolbarPressedBackground;

	protected Icon helpButtonIcon;
	private boolean defaults_initialized = false;
	private static ComponentUI instance;

	public static ComponentUI createUI(JComponent c) {
		if (instance == null) {
			instance = new JewelButtonUI();
		}
		return instance;
	}

	@Override
	protected void installDefaults(AbstractButton b) {
		super.installDefaults(b);

		if (!defaults_initialized) {
			initializeUIDefaults();
		}

		// Add null check and fallback for help button icon
		if (helpButtonIcon == null) {
			helpButtonIcon = UIManager.getIcon("JewelLaf.HelpButton.icon");
			// If still null, create a simple default icon
			if (helpButtonIcon == null) {
				helpButtonIcon = createDefaultHelpIcon();
			}
		}

		LookAndFeel.installProperty(b, "iconTextGap", scale(iconTextGap));
		MigLayoutVisualPadding.install(b, focusWidth);
	}

	private void initializeUIDefaults() {
		String prefix = getPropertyPrefix();

		// Core properties
		focusWidth = UIManager.getInt("Component.focusWidth");
		arc = UIManager.getInt(prefix + "arc");
		minimumWidth = UIManager.getInt(prefix + "minimumWidth");
		iconTextGap = JewelUIUtils.getUIInt(prefix + "iconTextGap", 4);

		// Regular button colors
		focusedBackground = UIManager.getColor(prefix + "focusedBackground");
		hoverBackground = UIManager.getColor(prefix + "hoverBackground");
		pressedBackground = UIManager.getColor(prefix + "pressedBackground");
		disabledText = UIManager.getColor(prefix + "disabledText");

		// Default button properties
		defaultBackground = UIManager.getColor("Button.default.background");
		defaultForeground = UIManager.getColor("Button.default.foreground");
		defaultFocusedBackground = UIManager.getColor("Button.default.focusedBackground");
		defaultHoverBackground = UIManager.getColor("Button.default.hoverBackground");
		defaultPressedBackground = UIManager.getColor("Button.default.pressedBackground");
		defaultBoldText = UIManager.getBoolean("Button.default.boldText");

		// Toolbar button colors
		toolbarHoverBackground = UIManager.getColor(prefix + "toolbar.hoverBackground");
		toolbarPressedBackground = UIManager.getColor(prefix + "toolbar.pressedBackground");

		helpButtonIcon = UIManager.getIcon("JewelLaf.HelpButton.icon");

		defaults_initialized = true;
	}

	@Override
	protected void uninstallDefaults(AbstractButton b) {
		super.uninstallDefaults(b);
		MigLayoutVisualPadding.uninstall(b);
		defaults_initialized = false;
	}

	private Icon createDefaultHelpIcon() {
		return new Icon() {
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				try {
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(c.isEnabled() ? c.getForeground() : disabledText);

					// Draw circle
					g2.drawOval(x + 1, y + 1, 14, 14);

					// Draw question mark
					g2.setFont(new Font("Dialog", Font.BOLD, 12));
					g2.drawString("?", x + 5, y + 13);
				} finally {
					g2.dispose();
				}
			}

			@Override
			public int getIconWidth() {
				return 16;
			}

			@Override
			public int getIconHeight() {
				return 16;
			}
		};
	}

	// Button state utility methods
	static boolean isContentAreaFilled(Component c) {
		return !(c instanceof AbstractButton) || ((AbstractButton) c).isContentAreaFilled();
	}

	static boolean isDefaultButton(Component c) {
		return c instanceof JButton && ((JButton) c).isDefaultButton();
	}

	static boolean isIconOnlyButton(Component c) {
		if (!(c instanceof JButton button)) return false;
		String text = button.getText();
		return button.getIcon() != null && (text == null || text.isEmpty());
	}

	static boolean isHelpButton(Component c) {
		return c instanceof JButton && clientPropertyEquals((JButton) c, BUTTON_TYPE, BUTTON_TYPE_HELP);
	}

	static boolean isToolBarButton(JComponent c) {
		return c.getParent() instanceof JToolBar;
	}

	@Override
	public void update(Graphics g, JComponent c) {
		if (isHelpButton(c)) {
			paintHelpButton(g, c);
			return;
		}

		paintRegularButton(g, c);
	}

	private void paintHelpButton(Graphics g, JComponent c) {
		JewelUIUtils.paintParentBackground(g, c);
		if (helpButtonIcon != null) {
			helpButtonIcon.paintIcon(c, g, 0, 0);
		}
	}

	private void paintRegularButton(Graphics g, JComponent c) {
		if (c.isOpaque() && isContentAreaFilled(c)) {
			JewelUIUtils.paintParentBackground(g, c);
			paintButtonBackground(g, c);
		}
		paint(g, c);
	}

	private void paintButtonBackground(Graphics g, JComponent c) {
		Color background = getBackground(c);
		if (background != null) {
			Graphics2D g2 = (Graphics2D) g.create();
			try {
				JewelUIUtils.setRenderingHints(g2);
				paintButtonBackgroundImpl(g2, c, background);
			} finally {
				g2.dispose();
			}
		}
	}

	private void paintButtonBackgroundImpl(Graphics2D g2, JComponent c, Color background) {
		Border border = c.getBorder();
		float focusWidth = (border instanceof JewelBorder) ? scale((float) this.focusWidth) : 0;
		float arc = (border instanceof JewelButtonBorder || isToolBarButton(c)) ? scale((float) this.arc) : 0;

		g2.setColor(background);
		JewelUIUtils.fillRoundRectangle(g2, 0, 0, c.getWidth(), c.getHeight(), focusWidth, arc);
	}

	@Override
	protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
		if (isHelpButton(b)) return;

		if (shouldUseBoldFont(b)) {
			paintBoldText(g, b, textRect, text);
		} else {
			paintText(g, b, textRect, text, b.isEnabled() ? getForeground(b) : disabledText);
		}
	}

	private boolean shouldUseBoldFont(AbstractButton b) {
		return defaultBoldText && isDefaultButton(b) && b.getFont() instanceof UIResource;
	}

	private void paintBoldText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
		Font boldFont = g.getFont().deriveFont(Font.BOLD);
		g.setFont(boldFont);

		int boldWidth = b.getFontMetrics(boldFont).stringWidth(text);
		if (boldWidth > textRect.width) {
			textRect.x -= (boldWidth - textRect.width) / 2;
			textRect.width = boldWidth;
		}

		paintText(g, b, textRect, text, b.isEnabled() ? getForeground(b) : disabledText);
	}

	static void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text, Color foreground) {
		FontMetrics fm = b.getFontMetrics(b.getFont());
		int mnemonicIndex = JewelLaf.isShowMnemonics() ? b.getDisplayedMnemonicIndex() : -1;

		g.setColor(foreground);
		JewelUIUtils.drawStringUnderlineCharAt(b, g, text, mnemonicIndex,
			textRect.x, textRect.y + fm.getAscent());
	}

	protected Color getBackground(JComponent c) {
		if (!c.isEnabled()) return null;

		if (isToolBarButton(c)) {
			return getToolBarButtonBackground(c);
		}

		boolean def = isDefaultButton(c);
		return buttonStateColor(c,
			def ? defaultBackground : c.getBackground(),
			null,
			def ? defaultFocusedBackground : focusedBackground,
			def ? defaultHoverBackground : hoverBackground,
			def ? defaultPressedBackground : pressedBackground);
	}

	private Color getToolBarButtonBackground(JComponent c) {
		ButtonModel model = ((AbstractButton) c).getModel();
		if (model.isPressed()) return toolbarPressedBackground;
		if (model.isRollover()) return toolbarHoverBackground;
		return c.getParent().getBackground();
	}

	public static Color buttonStateColor(Component c, Color enabledColor, Color disabledColor,
										 Color focusedColor, Color hoverColor, Color pressedColor) {
		if (!(c instanceof AbstractButton b)) return enabledColor;

		if (!c.isEnabled()) return disabledColor;
		if (pressedColor != null && b.getModel().isPressed()) return pressedColor;
		if (hoverColor != null && b.getModel().isRollover()) return hoverColor;
		if (focusedColor != null && c.hasFocus()) return focusedColor;

		return enabledColor;
	}

	protected Color getForeground(JComponent c) {
		return isDefaultButton(c) ? defaultForeground : c.getForeground();
	}

	@Override
	public Dimension getPreferredSize(JComponent c) {
		if (isHelpButton(c)) {
			if (helpButtonIcon != null) {
				return new Dimension(helpButtonIcon.getIconWidth(),
					helpButtonIcon.getIconHeight());
			} else {
				return new Dimension(16, 16);
			}
		}

		Dimension prefSize = super.getPreferredSize(c);

		if (!isToolBarButton(c) && !isIconOnlyButton(c)) {
			prefSize.width = Math.max(prefSize.width, scale(minimumWidth + (focusWidth * 2)));
		}

		return prefSize;
	}
}
