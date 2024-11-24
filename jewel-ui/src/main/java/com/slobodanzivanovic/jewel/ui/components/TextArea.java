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
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultCaret;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class TextArea extends JPanel {
	private final JTextArea textArea;
	private final LineNumbers lineNumbers;
	private static final int BUFFER_SIZE = 10000000;

	public TextArea() {
		setLayout(new BorderLayout());
		textArea = new JTextArea();
		configureTextArea();
		lineNumbers = new LineNumbers(textArea);
		JScrollPane scrollPane = createOptimizedScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		addEfficientDocumentListener();
	}

	private void configureTextArea() {
		textArea.setLineWrap(false);
		textArea.setWrapStyleWord(false);
		textArea.setMargin(new Insets(0, 2, 0, 2));
		textArea.setBorder(BorderFactory.createEmptyBorder());
		textArea.getDocument().putProperty(PlainDocument.tabSizeAttribute, 4);

		if (textArea.getDocument() instanceof AbstractDocument doc) {
			doc.putProperty("BUFFER_SIZE_DEFAULT", BUFFER_SIZE);
		}

		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.UPDATE_WHEN_ON_EDT);
	}

	private JScrollPane createOptimizedScrollPane() {
		JScrollPane scrollPane = new JScrollPane(textArea) {
			@Override
			public void paint(Graphics g) {
				if (g instanceof Graphics2D g2d) {
					g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
				}
				super.paint(g);
			}
		};

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(null);
		scrollPane.setRowHeaderView(lineNumbers);

		JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
		verticalScrollBar.setUnitIncrement(textArea.getFont().getSize() * 3);
		verticalScrollBar.setBlockIncrement(textArea.getFont().getSize() * 15);

		lineNumbers.setScrollPane(scrollPane);
		return scrollPane;
	}

	private void addEfficientDocumentListener() {
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				SwingUtilities.invokeLater(lineNumbers::refresh);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtilities.invokeLater(lineNumbers::refresh);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				SwingUtilities.invokeLater(lineNumbers::refresh);
			}
		});
	}

	public JTextArea getTextArea() {
		return textArea;
	}
}
