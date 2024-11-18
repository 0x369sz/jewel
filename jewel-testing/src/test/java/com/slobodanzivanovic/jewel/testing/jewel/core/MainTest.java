package com.slobodanzivanovic.jewel.testing.jewel.core;

import com.slobodanzivanovic.jewel.core.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
	private final PrintStream originalOut = System.out;

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@BeforeEach
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}

	@AfterEach
	public void restoreStreams() {
		System.setOut(originalOut);
	}

	@Test
	public void testMainMethodOutput() {
		Main.main(new String[]{});

		assertEquals("Hello World!\nHello, World!\n", outContent.toString());
	}
}
