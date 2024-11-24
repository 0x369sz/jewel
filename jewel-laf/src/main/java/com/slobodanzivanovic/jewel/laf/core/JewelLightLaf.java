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

public final class JewelLightLaf extends JewelLaf {
	private static final Logger logger;
	private static final String NAME = "Jewel Light";
	private static final String DESCRIPTION = "Jewel Light Look and Feel";

	private static volatile JewelLightLaf instance;

	static {
		try {
			logger = new Logger("jewel-laf");
		} catch (java.io.IOException e) {
			System.err.println("Failed to initialize logger: " + e.getMessage());
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Creates a new instance of JewelLightLaf.
	 */
	public JewelLightLaf() {
		super();
		synchronized (JewelLightLaf.class) {
			if (instance == null) {
				instance = this;
				logger.debug("Created new instance of Jewel Light Look and Feel");
			}
		}
	}

	/**
	 * Installs the Jewel Light Look and Feel.
	 * This method is thread-safe.
	 *
	 * @return true if the installation was successful, false otherwise
	 */
	public static boolean install() {
		try {
			UIManager.setLookAndFeel(new JewelLightLaf());
			logger.info("Successfully installed Jewel Light Look and Feel");
			return true;
		} catch (UnsupportedLookAndFeelException e) {
			logger.error("Failed to install Jewel Light Look and Feel: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Gets the current instance of JewelLightLaf.
	 * Creates a new instance if one doesn't exist.
	 *
	 * @return the current instance of JewelLightLaf
	 */
	public static JewelLightLaf getInstance() {
		if (instance == null) {
			new JewelLightLaf();
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
	 * Checks if the Jewel Light Look and Feel is currently active.
	 *
	 * @return true if Jewel Light is the current look and feel
	 */
	public static boolean isActive() {
		boolean active = UIManager.getLookAndFeel() instanceof JewelLightLaf;
		logger.debug("Jewel Light Look and Feel active status: " + active);
		return active;
	}
}
