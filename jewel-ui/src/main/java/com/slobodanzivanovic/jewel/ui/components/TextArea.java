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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class TextArea extends JPanel {
	private final JTextArea textArea;
	private final LineNumbers lineNumbers;

	public TextArea() {
		setLayout(new BorderLayout());
		textArea = new JTextArea();

		// CONFIG
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setMargin(new Insets(1, 1, 1, 1));
		textArea.setBorder(BorderFactory.createEmptyBorder());
		textArea.setFont(new Font("IBM Plex Mono", Font.PLAIN, 14));
		int tabSize = 4;
		textArea.getDocument().putProperty(PlainDocument.tabSizeAttribute, tabSize);

		lineNumbers = new LineNumbers(textArea);
		JScrollPane textScrollPane = new JScrollPane(textArea);
		textScrollPane.setBorder(null);
		textScrollPane.setRowHeaderView(lineNumbers);
		lineNumbers.setScrollPane(textScrollPane);

		add(textScrollPane, BorderLayout.CENTER);

		textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				lineNumbers.refresh();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				lineNumbers.refresh();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				lineNumbers.refresh();
			}
		});
	}

	public JTextArea getTextArea() {
		return textArea;
	}
}
