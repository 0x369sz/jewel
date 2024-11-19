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

package com.slobodanzivanovic.jewel.util.logging;

import com.slobodanzivanovic.jewel.util.platform.PlatformInfo;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class Logger {
	private final Path logFilePath;
	private final ReentrantLock lock = new ReentrantLock();
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final long MIN_REQUIRED_SPACE = 10 * 1024 * 1024;
	private static final int MAX_LOG_SIZE = 10 * 1024 * 1024;
	private static final long MAX_LOG_AGE_DAYS = 30;

	private static final String SESSION_FOLDER;
	private static final DateTimeFormatter SESSION_FOLDER_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

	static {
		SESSION_FOLDER = LocalDateTime.now().format(SESSION_FOLDER_FORMATTER);
	}

	public enum LogLevel {
		INFO, WARNING, ERROR, DEBUG
	}

	public Logger(String filename) throws IOException {
		Path logDir = getSystemLogDirectory().resolve(SESSION_FOLDER);
		Files.createDirectories(logDir);

		this.logFilePath = logDir.resolve(filename + ".log");

		validateSystem();
		if (!Files.exists(logFilePath)) {
			Files.createFile(logFilePath);
		}

		cleanupOldLogs();
	}

	public void info(String message) {
		log(LogLevel.INFO, message);
	}

	public void warning(String message) {
		log(LogLevel.WARNING, message);
	}

	public void error(String message) {
		log(LogLevel.ERROR, message);
	}

	public void debug(String message) {
		log(LogLevel.DEBUG, message);
	}

	private void log(LogLevel level, String message) {
		lock.lock();
		try {
			validateSystem();

			if (Files.size(logFilePath) > MAX_LOG_SIZE) {
				rotateLog();
			}

			String timestamp = LocalDateTime.now().format(dateFormatter);
			String formattedMessage = String.format("[%s] [%s] %s%n", timestamp, level, message);

			Files.write(logFilePath, formattedMessage.getBytes(), StandardOpenOption.APPEND);

		} catch (IOException e) {
			System.err.println("Failed to write to log file: " + e.getMessage());
		} finally {
			lock.unlock();
		}
	}

	private void rotateLog() throws IOException {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
		Path rotatedFile = logFilePath.resolveSibling(logFilePath.getFileName().toString().replace(".log", "-" + timestamp + ".log"));

		Files.move(logFilePath, rotatedFile, StandardCopyOption.REPLACE_EXISTING);
		Files.createFile(logFilePath);
	}

	private void cleanupOldLogs() {
		try {
			Path logsDir = getSystemLogDirectory();
			LocalDateTime cutoffDate = LocalDateTime.now().minusDays(MAX_LOG_AGE_DAYS);

			try (Stream<Path> folders = Files.list(logsDir)) {
				folders.forEach(folder -> {
					if (Files.isDirectory(folder)) {
						try {
							LocalDateTime folderDate = LocalDateTime.parse(folder.getFileName().toString(), SESSION_FOLDER_FORMATTER);
							if (folderDate.isBefore(cutoffDate)) {
								deleteDirectory(folder);
							}
						} catch (Exception e) {
							System.err.println("Skipping invalid log folder: " + folder.getFileName());
						}
					}
				});
			}
		} catch (IOException e) {
			System.err.println("Failed to cleanup old logs: " + e.getMessage());
		}
	}

	private void deleteDirectory(Path path) throws IOException {
		if (!Files.exists(path)) {
			return;
		}

		try (Stream<Path> contents = Files.walk(path)) {
			contents.sorted(Comparator.reverseOrder()).forEach(subPath -> {
				try {
					Files.delete(subPath);
				} catch (IOException e) {
					System.err.println("Failed to delete " + subPath + ": " + e.getMessage());
				}
			});
		}
	}

	private Path getSystemLogDirectory() {
		String userHome = System.getProperty("user.home");

		if (PlatformInfo.IS_WINDOWS) {
			// Windows: %APPDATA%\Jewel\logs
			return Paths.get(System.getenv("APPDATA"), "Jewel", "logs");
		} else if (PlatformInfo.IS_MAC) {
			// NOTE: We probably should use the home for both macOS and Linux
			// macOS: ~/Library/Logs/Jewel
			return Paths.get(userHome, "Library", "Logs", "Jewel");
		} else {
			// Linux/Unix: ~/.jewel/logs
			return Paths.get(userHome, ".jewel", "logs");
		}
	}

	public Path getLogFilePath() {
		return logFilePath;
	}

	public Path getSessionDirectory() {
		return logFilePath.getParent();
	}

	private void validateSystem() throws IOException {
		FileStore store = Files.getFileStore(logFilePath.getParent());
		long usableSpace = store.getUsableSpace();

		if (usableSpace < MIN_REQUIRED_SPACE) {
			throw new IOException("Insufficient disk space for logging. Required: " + MIN_REQUIRED_SPACE + " bytes, Available: " + usableSpace + " bytes");
		}

		if (!Files.isWritable(logFilePath.getParent())) {
			throw new IOException("No write permission in log directory: " + logFilePath.getParent());
		}
	}
}
