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

package com.slobodanzivanovic.jewel.util.platform;

import com.slobodanzivanovic.jewel.util.logging.Logger;

import java.io.IOException;
import java.util.Locale;

public class PlatformInfo {
	private static final PlatformInfo INSTANCE = new PlatformInfo();

	public static final boolean IS_WINDOWS;
	public static final boolean IS_MAC;
	public static final boolean IS_LINUX;

	private final String osName;
	private final String osVersion;
	private final String osArch;
	private final OSType osType;
	private final String javaVersion;
	private final int javaMajorVersion;

	static {
		String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
		IS_WINDOWS = osName.startsWith("windows");
		IS_MAC = osName.startsWith("mac");
		IS_LINUX = osName.startsWith("linux");
	}

	private PlatformInfo() {
		this.osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		this.osVersion = System.getProperty("os.version");
		this.osArch = System.getProperty("os.arch");
		this.osType = determineOSType(osName);

		Runtime.Version version = Runtime.version();
		this.javaVersion = version.toString();
		this.javaMajorVersion = version.feature();
	}

	public enum OSType {
		WINDOWS, LINUX, MACOS, UNKNOWN
	}

	private OSType determineOSType(String osName) {
		if (osName.startsWith("mac")) return OSType.MACOS;
		if (osName.startsWith("win")) return OSType.WINDOWS;
		if (osName.contains("nix") || osName.contains("nux")) return OSType.LINUX;
		return OSType.UNKNOWN;
	}

	public String getOSName() {
		return osName;
	}

	public String getOSVersion() {
		return osVersion;
	}

	public String getOSArch() {
		return osArch;
	}

	public OSType getOSType() {
		return osType;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public int getJavaMajorVersion() {
		return javaMajorVersion;
	}

	public boolean isAarch64() {
		return "aarch64".equals(osArch) || "arm64".equals(osArch);
	}

	public boolean isUnix() {
		return IS_MAC || IS_LINUX;
	}

	public static PlatformInfo getInstance() {
		return INSTANCE;
	}

	public void logSystemInfo() {
		// NOTE: Prob should not be here but for now it is
		try {
			Logger logger = new Logger("system-info");
			logger.info("OS Name: " + osName);
			logger.info("OS Version: " + osVersion);
			logger.info("OS Arch: " + osArch);
			logger.info("Java Version: " + javaVersion);
			logger.info("Java Major Version: " + javaMajorVersion);
		} catch (IOException e) {
			System.err.println("Failed to initialize logger: " + e.getMessage());
		}
	}
}
