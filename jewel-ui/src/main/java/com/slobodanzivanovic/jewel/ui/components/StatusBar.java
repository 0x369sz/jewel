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

package com.slobodanzivanovic.jewel.ui.components;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
	private static final int PADDING = 10;

	private final JLabel positionLabel;
	private final JLabel fpsLabel;

	public StatusBar() {
		setLayout(new BorderLayout());

		positionLabel = new JLabel("Line 1, Column 1");
		positionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, PADDING));

		fpsLabel = new JLabel("FPS 0");
		fpsLabel.setBorder(BorderFactory.createEmptyBorder(0, PADDING, 0, 0));

		add(fpsLabel, BorderLayout.WEST);
		add(positionLabel, BorderLayout.EAST);
	}

	public void updateStatus(int line, int col) {
		positionLabel.setText("Line " + line + ", Column " + col);
	}

	public void updateFps(int fps) {
		fpsLabel.setText("FPS " + fps);
	}
}
