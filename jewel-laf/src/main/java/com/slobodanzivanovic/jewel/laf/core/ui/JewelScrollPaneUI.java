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
import com.slobodanzivanovic.jewel.laf.core.util.MigLayoutVisualPadding;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.util.Objects;

/**
 * UI delegate for JScrollPane components in the Jewel Look and Feel.
 * Provides custom layout handling, focus management, and visual padding support.
 */
public class JewelScrollPaneUI extends BasicScrollPaneUI {
	private static final String KEY_FOCUS_WIDTH = "Component.focusWidth";

	private Handler handler;

	/**
	 * Creates a new UI delegate for the specified component.
	 *
	 * @param component the component that will use this delegate
	 * @return the UI delegate instance
	 * @throws NullPointerException if component is null
	 */
	public static ComponentUI createUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		return new JewelScrollPaneUI();
	}

	@Override
	public void installUI(JComponent component) {
		super.installUI(component);
		Objects.requireNonNull(component, "Component cannot be null");

		if (scrollpane.getLayout() instanceof UIResource) {
			scrollpane.setLayout(new JewelScrollPaneLayout());
		}

		MigLayoutVisualPadding.install(scrollpane, UIManager.getInt(KEY_FOCUS_WIDTH));
	}

	@Override
	public void uninstallUI(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");

		if (scrollpane.getLayout() instanceof JewelScrollPaneLayout) {
			scrollpane.setLayout(new ScrollPaneLayout.UIResource());
		}

		MigLayoutVisualPadding.uninstall(scrollpane);
		super.uninstallUI(component);
	}

	@Override
	protected void installListeners(JScrollPane scrollPane) {
		Objects.requireNonNull(scrollPane, "ScrollPane cannot be null");
		super.installListeners(scrollPane);
		addViewportListeners(scrollPane.getViewport());
	}

	@Override
	protected void uninstallListeners(JComponent component) {
		Objects.requireNonNull(component, "Component cannot be null");
		super.uninstallListeners(component);
		removeViewportListeners(scrollpane.getViewport());
		handler = null;
	}

	/**
	 * Returns the handler for focus and container events.
	 *
	 * @return the event handler instance
	 */
	Handler getHandler() {
		if (handler == null) {
			handler = new Handler();
		}
		return handler;
	}

	@Override
	protected void updateViewport(PropertyChangeEvent event) {
		Objects.requireNonNull(event, "PropertyChangeEvent cannot be null");
		super.updateViewport(event);

		JViewport oldViewport = (JViewport) event.getOldValue();
		JViewport newViewport = (JViewport) event.getNewValue();

		removeViewportListeners(oldViewport);
		addViewportListeners(newViewport);
	}

	/**
	 * Adds focus and container listeners to the viewport and its view.
	 *
	 * @param viewport the viewport to add listeners to
	 */
	private void addViewportListeners(JViewport viewport) {
		if (viewport == null) {
			return;
		}

		viewport.addContainerListener(getHandler());

		Component view = viewport.getView();
		if (view != null) {
			view.addFocusListener(getHandler());
		}
	}

	/**
	 * Removes focus and container listeners from the viewport and its view.
	 *
	 * @param viewport the viewport to remove listeners from
	 */
	private void removeViewportListeners(JViewport viewport) {
		if (viewport == null) {
			return;
		}

		viewport.removeContainerListener(getHandler());

		Component view = viewport.getView();
		if (view != null) {
			view.removeFocusListener(getHandler());
		}
	}

	@Override
	public void update(Graphics graphics, JComponent component) {
		Objects.requireNonNull(graphics, "Graphics cannot be null");
		Objects.requireNonNull(component, "Component cannot be null");

		if (component.isOpaque()) {
			JewelUIUtils.paintParentBackground(graphics, component);

			Insets insets = component.getInsets();
			graphics.setColor(component.getBackground());
			graphics.fillRect(
				insets.left,
				insets.top,
				component.getWidth() - insets.left - insets.right,
				component.getHeight() - insets.top - insets.bottom
			);
		}

		paint(graphics, component);
	}

	/**
	 * Handler class that manages focus and container events for the scroll pane.
	 */
	private class Handler implements ContainerListener, FocusListener {
		@Override
		public void componentAdded(ContainerEvent event) {
			Objects.requireNonNull(event, "ContainerEvent cannot be null");
			event.getChild().addFocusListener(this);
		}

		@Override
		public void componentRemoved(ContainerEvent event) {
			Objects.requireNonNull(event, "ContainerEvent cannot be null");
			event.getChild().removeFocusListener(this);
		}

		@Override
		public void focusGained(FocusEvent event) {
			scrollpane.repaint();
		}

		@Override
		public void focusLost(FocusEvent event) {
			scrollpane.repaint();
		}
	}

	/**
	 * Custom layout manager for JewelScrollPane that adjusts the vertical scrollbar
	 * position when a column header is present.
	 */
	private static class JewelScrollPaneLayout extends ScrollPaneLayout {
		@Override
		public void layoutContainer(Container parent) {
			Objects.requireNonNull(parent, "Parent container cannot be null");
			super.layoutContainer(parent);

			if (colHead != null && vsb != null && colHead.isVisible() && vsb.isVisible()) {
				Rectangle colHeadBounds = colHead.getBounds();
				Rectangle vsbBounds = vsb.getBounds();

				if (vsbBounds.y > colHeadBounds.y) {
					vsbBounds.height += (vsbBounds.y - colHeadBounds.y);
					vsbBounds.y = colHeadBounds.y;
					vsb.setBounds(vsbBounds);
				}
			}
		}
	}
}
