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

package com.slobodanzivanovic.jewel.ui;

import javax.swing.*;
import java.awt.*;

public class EditorWindow extends JPanel implements Runnable {

	public EditorWindow() {
		this.setPreferredSize(new Dimension(1280, 720));

		this.setDoubleBuffered(true);
	}

	Thread editorThread;

	public void startEditorThread() {
		editorThread = new Thread(this);
		editorThread.start();
	}

	@Override
	public void run() {
		while (editorThread != null) {
//			System.out.println("Editor thread running");
		}
	}
}
