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

package com.slobodanzivanovic.jewel.laf.core;

import com.slobodanzivanovic.jewel.util.logging.Logger;

import javax.swing.*;

public final class JewelDarkLaf extends JewelLaf {
	private static final Logger logger;
	private static final String NAME = "Jewel Dark";
	private static final String DESCRIPTION = "Jewel Dark Look and Feel";

	private static volatile JewelDarkLaf instance;

	static {
		try {
			logger = new Logger("jewel-laf");
		} catch (java.io.IOException e) {
			System.err.println("Failed to initialize logger: " + e.getMessage());
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Creates a new instance of JewelDarkLaf.
	 */
	public JewelDarkLaf() {
		super();
		synchronized (JewelDarkLaf.class) {
			if (instance == null) {
				instance = this;
				logger.debug("Created new instance of Jewel Dark Look and Feel");
			}
		}
	}

	/**
	 * Installs the Jewel Dark Look and Feel.
	 * This method is thread-safe.
	 *
	 * @return true if the installation was successful, false otherwise
	 */
	public static boolean install() {
		try {
			UIManager.setLookAndFeel(new JewelDarkLaf());
			logger.info("Successfully installed Jewel Dark Look and Feel");
			return true;
		} catch (UnsupportedLookAndFeelException e) {
			logger.error("Failed to install Jewel Dark Look and Feel: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Gets the current instance of JewelDarkLaf.
	 * Creates a new instance if one doesn't exist.
	 *
	 * @return the current instance of JewelDarkLaf
	 */
	public static JewelDarkLaf getInstance() {
		if (instance == null) {
			new JewelDarkLaf();
		}
		return instance;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	/**
	 * Checks if the Jewel Dark Look and Feel is currently active.
	 *
	 * @return true if Jewel Dark is the current look and feel
	 */
	public static boolean isActive() {
		boolean active = UIManager.getLookAndFeel() instanceof JewelDarkLaf;
		logger.debug("Jewel Dark Look and Feel active status: " + active);
		return active;
	}
}
