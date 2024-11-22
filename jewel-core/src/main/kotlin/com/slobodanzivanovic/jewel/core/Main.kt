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

package com.slobodanzivanovic.jewel.core

import com.slobodanzivanovic.jewel.laf.core.JewelDarkLaf
import com.slobodanzivanovic.jewel.ui.EditorWindow
import com.slobodanzivanovic.jewel.util.logging.Logger
import com.slobodanzivanovic.jewel.util.platform.PlatformInfo
import java.awt.Dimension
import java.io.IOException
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException

fun main() {
	val platformInfo = PlatformInfo.getInstance()

	platformInfo.logSystemInfo()

	try {
		UIManager.setLookAndFeel(JewelDarkLaf())
		if (PlatformInfo.IS_MAC) {
			System.setProperty("apple.laf.useScreenMenuBar", "true")
		}
	} catch (laf: UnsupportedLookAndFeelException) {
		laf.printStackTrace()
	}

	SwingUtilities.invokeLater {
		JFrame().apply {
			add(EditorWindow().also { editorWindow ->
				editorWindow.startEditorThread()
			})
			defaultCloseOperation = JFrame.EXIT_ON_CLOSE
			isResizable = true
			minimumSize = Dimension(800, 600)
			pack()
			setLocationRelativeTo(null)
			isVisible = true
		}
	}

	try {
		val logger = Logger("main")
		logger.info("Application started")

		val sessionDirectory = logger.sessionDirectory
		println("Session directory is: $sessionDirectory")

		val logFilePath = logger.logFilePath
		println("Log file is located at: $logFilePath")
	} catch (e: IOException) {
		System.err.println("Failed to initialize logger: " + e.message)
	}
}
