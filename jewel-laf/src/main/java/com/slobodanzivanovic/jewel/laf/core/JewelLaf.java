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

import com.slobodanzivanovic.jewel.laf.core.ui.JewelEmptyBorder;
import com.slobodanzivanovic.jewel.laf.core.ui.JewelLineBorder;
import com.slobodanzivanovic.jewel.laf.core.util.UIScale;
import com.slobodanzivanovic.jewel.laf.core.util.UIScaledValue;
import com.slobodanzivanovic.jewel.util.logging.Logger;
import com.slobodanzivanovic.jewel.util.platform.PlatformInfo;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;
import java.util.function.Function;

/**
 * Abstract base class for the Jewel Look and Feel family.
 * Provides core functionality for UI customization, theme management, and platform-specific adaptations.
 * Extends BasicLookAndFeel to provide a foundation for both light and dark themes.
 */
public abstract class JewelLaf extends BasicLookAndFeel {
	private static final Logger logger;

	private static final String VARIABLE_PREFIX = "@";
	private static final String REF_PREFIX = VARIABLE_PREFIX + VARIABLE_PREFIX;
	private static final String OPTIONAL_PREFIX = "?";
	private static final String GLOBAL_PREFIX = "*.";

	/**
	 * Collection of default control properties for UI components
	 */
	private static final Map<String, Object> CONTROL_DEFAULTS = createControlDefaults();

	private final BasicLookAndFeel base;
	private String desktopPropertyName;
	private PropertyChangeListener desktopPropertyListener;
	private AWTEventListener mnemonicListener;
	private static volatile boolean altKeyPressed;

	static {
		try {
			logger = new Logger("jewel-laf");
		} catch (IOException e) {
			System.err.println("Failed to initialize logger: " + e.getMessage());
			throw new ExceptionInInitializerError(e);
		}
	}

	protected JewelLaf() {
		this.base = initializeBaseLookAndFeel();
	}

	/**
	 * Installs the specified Look and Feel.
	 * Provides a centralized way to set up any Jewel Look and Feel implementation.
	 *
	 * @param newLookAndFeel the Look and Feel to install
	 * @return true if installation was successful, false otherwise
	 */
	public static boolean install(LookAndFeel newLookAndFeel) {
		try {
			UIManager.setLookAndFeel(newLookAndFeel);
			return true;
		} catch (Exception ex) {
			logger.error("Failed to initialize look and feel " + newLookAndFeel.getClass().getName() + ": " + ex.getMessage());
			return false;
		}
	}

	/**
	 * Creates default control properties for text components.
	 * Initializes background and foreground colors for disabled states
	 * across various text input components like EditorPane, TextField, etc.
	 *
	 * @return Map of unmodifiable control defaults
	 */
	private static Map<String, Object> createControlDefaults() {
		Map<String, Object> defaults = new HashMap<>();
		String[] components = {
			"EditorPane", "FormattedTextField", "PasswordField",
			"TextArea", "TextField", "TextPane", "Spinner"
		};

		for (String component : components) {
			defaults.put(component + ".disabledBackground", "control");
			if ("TextArea".equals(component) || "TextPane".equals(component) || "EditorPane".equals(component)) {
				defaults.put(component + ".inactiveBackground", "control");
			}
		}
		defaults.put("Spinner.disabledForeground", "control");
		return Collections.unmodifiableMap(defaults);
	}

	/**
	 * Initializes platform-specific Look and Feel settings.
	 * For macOS, attempts to use the native system Look and Feel.
	 * Falls back to Metal Look and Feel if native initialization fails.
	 *
	 * @return initialized BasicLookAndFeel instance
	 */
	private BasicLookAndFeel initializeBaseLookAndFeel() {
		if (PlatformInfo.IS_MAC) {
			try {
				String systemLafClassName = UIManager.getSystemLookAndFeelClassName();
				UIManager.setLookAndFeel(systemLafClassName);
				LookAndFeel systemLaf = UIManager.getLookAndFeel();

				if (systemLaf instanceof BasicLookAndFeel) {
					return (BasicLookAndFeel) systemLaf;
				}
				logger.warning("System L&F is not BasicLookAndFeel, falling back to Metal");
			} catch (Exception ex) {
				logger.warning("Failed to initialize system look and feel: " + ex.getMessage());
			}
		}
		return new MetalLookAndFeel();
	}

	@Override
	public String getID() {
		return getName();
	}

	@Override
	public boolean isNativeLookAndFeel() {
		return true;
	}

	@Override
	public boolean isSupportedLookAndFeel() {
		return true;
	}

	@Override
	public void initialize() {
		base.initialize();
		super.initialize();
		initializeMnemonicListener();
		initializeDesktopPropertyListener();
	}

	/**
	 * Sets up the ALT key listener for mnemonic functionality.
	 * Tracks ALT key state for showing/hiding mnemonics in the UI.
	 */
	private void initializeMnemonicListener() {
		mnemonicListener = e -> {
			if (e instanceof KeyEvent && ((KeyEvent) e).getKeyCode() == KeyEvent.VK_ALT) {
				altKeyChanged(e.getID() == KeyEvent.KEY_PRESSED);
			}
		};
		Toolkit.getDefaultToolkit().addAWTEventListener(mnemonicListener, AWTEvent.KEY_EVENT_MASK);
	}

	/**
	 * Initializes platform-specific desktop property monitoring.
	 * Handles font and DPI changes for Windows and Linux systems.
	 */
	private void initializeDesktopPropertyListener() {
		desktopPropertyName = PlatformInfo.IS_WINDOWS ? "win.messagebox.font" :
			PlatformInfo.IS_LINUX ? "gnome.Xft/DPI" : null;

		if (desktopPropertyName != null) {
			desktopPropertyListener = e -> reSetLookAndFeel();
			Toolkit.getDefaultToolkit().addPropertyChangeListener(desktopPropertyName, desktopPropertyListener);
		}
	}

	@Override
	public void uninitialize() {
		Optional.ofNullable(desktopPropertyListener).ifPresent(listener -> {
			Toolkit.getDefaultToolkit().removePropertyChangeListener(desktopPropertyName, listener);
			desktopPropertyName = null;
		});

		Optional.ofNullable(mnemonicListener).ifPresent(listener ->
			Toolkit.getDefaultToolkit().removeAWTEventListener(listener));

		Optional.ofNullable(base).ifPresent(BasicLookAndFeel::uninitialize);
		super.uninitialize();
	}

	@Override
	public UIDefaults getDefaults() {
		UIDefaults defaults = base.getDefaults();
		Color control = defaults.getColor("control");

		CONTROL_DEFAULTS.forEach((key, value) ->
			defaults.put(key, "control".equals(value) ? control : value));

		boolean useScreenMenuBar = PlatformInfo.IS_MAC &&
			"true".equals(System.getProperty("apple.laf.useScreenMenuBar"));
		Object aquaMenuBarUI = useScreenMenuBar ? defaults.get("MenuBarUI") : null;

		initializeFonts(defaults);
		loadDefaultsFromProperties(defaults);

		if (useScreenMenuBar) {
			defaults.put("MenuBarUI", aquaMenuBarUI);
		}

		return defaults;
	}

	/**
	 * Initializes fonts based on the current platform.
	 * Applies platform-specific font scaling and sets default fonts for all UI components.
	 *
	 * @param defaults the UIDefaults to populate with font settings
	 */
	private void initializeFonts(UIDefaults defaults) {
		FontUIResource uiFont = getFontForPlatform(defaults);

		uiFont = UIScale.applyCustomScaleFactor(uiFont);
		applyFontToDefaults(defaults, uiFont);
	}

	/**
	 * Retrieves the base font for the Look and Feel.
	 * Uses a consistent font approach with proper scaling based on platform DPI.
	 *
	 * @param defaults current UI defaults
	 * @return FontUIResource scaled appropriately for the platform
	 */
	private FontUIResource getFontForPlatform(UIDefaults defaults) {
		int fontSize = 13;
		String fontFamily = "SansSerif";

		double dpiScale = 1.0;
		if (PlatformInfo.IS_WINDOWS) {
			Font winFont = (Font) Toolkit.getDefaultToolkit().getDesktopProperty("win.messagebox.font");
			if (winFont != null) {
				dpiScale = winFont.getSize() / 12.0;
				fontFamily = winFont.getFamily();
			}
		} else if (PlatformInfo.IS_MAC) {
			Font font = defaults.getFont("Label.font");
			if (font != null) {
				return font instanceof FontUIResource ? (FontUIResource) font : new FontUIResource(font);
			}
		} else if (PlatformInfo.IS_LINUX) {
			dpiScale = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDefaultConfiguration()
				.getNormalizingTransform()
				.getScaleY();

			if (dpiScale == 1.0) {
				Object value = Toolkit.getDefaultToolkit().getDesktopProperty("gnome.Xft/DPI");
				if (value instanceof Integer) {
					int dpi = (Integer) value / 1024;
					if (dpi > 0) {
						dpiScale = dpi / 96.0;
					}
				}
			}
		}

		fontSize = (int) (fontSize * dpiScale);

		return new FontUIResource(fontFamily, Font.PLAIN, fontSize);
	}

	/**
	 * Applies the specified font to all font-related UI defaults.
	 * Updates both standard component fonts and special cases like accelerator fonts.
	 *
	 * @param defaults UI defaults to update
	 * @param uiFont   the font to apply
	 */
	private void applyFontToDefaults(UIDefaults defaults, FontUIResource uiFont) {
		defaults.keySet().stream()
			.filter(key -> key instanceof String)
			.map(key -> (String) key)
			.filter(key -> key.endsWith(".font"))
			.forEach(key -> defaults.put(key, uiFont));
		defaults.put("MenuItem.acceleratorFont", uiFont);
	}

	/**
	 * Loads and processes UI defaults from properties files.
	 * Handles variable resolution, global properties, and platform-specific settings.
	 *
	 * @param defaults the UIDefaults to populate
	 */
	private void loadDefaultsFromProperties(UIDefaults defaults) {
		try {
			Properties properties = loadProperties();
			Function<String, String> resolver = value -> resolveValue(properties, value);
			Map<String, Object> globals = processGlobals(properties, resolver);

			applyGlobalsToDefaults(defaults, globals);
			applyPropertiesToDefaults(defaults, properties, resolver);
		} catch (IOException ex) {
			logger.error("Failed to load properties: " + ex.getMessage());
		}
	}

	/**
	 * Loads Look and Feel properties from resource files.
	 * Searches through the class hierarchy to find and merge all relevant properties.
	 *
	 * @return Properties containing all Look and Feel settings
	 * @throws IOException if properties cannot be loaded
	 */
	private Properties loadProperties() throws IOException {
		Properties properties = new Properties();
		List<Class<?>> lafClasses = getLafClassHierarchy();

		for (Class<?> lafClass : lafClasses) {
			String propertiesName = "/" + lafClass.getName().replace('.', '/') + ".properties";
			try (InputStream in = lafClass.getResourceAsStream(propertiesName)) {
				if (in != null) {
					properties.load(in);
				}
			}
		}
		return properties;
	}

	/**
	 * Builds the class hierarchy for property loading.
	 * Creates an ordered list starting from the concrete implementation up to JewelLaf.
	 *
	 * @return ordered List of Look and Feel classes
	 */
	private List<Class<?>> getLafClassHierarchy() {
		List<Class<?>> lafClasses = new ArrayList<>();
		for (Class<?> lafClass = getClass(); JewelLaf.class.isAssignableFrom(lafClass);
			 lafClass = lafClass.getSuperclass()) {
			lafClasses.addFirst(lafClass);
		}
		return lafClasses;
	}

	/**
	 * Processes global properties that apply to multiple components.
	 * Handles property resolution and value parsing for global settings.
	 *
	 * @param properties raw properties
	 * @param resolver   function to resolve property references
	 * @return Map of processed global properties
	 */
	private Map<String, Object> processGlobals(Properties properties, Function<String, String> resolver) {
		Map<String, Object> globals = new HashMap<>();
		properties.entrySet().stream()
			.filter(e -> ((String) e.getKey()).startsWith(GLOBAL_PREFIX))
			.forEach(e -> {
				String key = ((String) e.getKey()).substring(GLOBAL_PREFIX.length());
				String value = resolveValue(properties, (String) e.getValue());
				globals.put(key, parseValue(key, value, resolver));
			});
		return globals;
	}

	/**
	 * Applies global properties to all matching UI defaults.
	 * Global properties are applied based on component property name suffixes.
	 *
	 * @param defaults UI defaults to update
	 * @param globals  processed global properties
	 */
	private void applyGlobalsToDefaults(UIDefaults defaults, Map<String, Object> globals) {
		defaults.keySet().stream()
			.filter(key -> key instanceof String && ((String) key).contains("."))
			.forEach(key -> {
				String skey = (String) key;
				String globalKey = skey.substring(skey.lastIndexOf('.') + 1);
				Object globalValue = globals.get(globalKey);
				if (globalValue != null) {
					defaults.put(key, globalValue);
				}
			});
	}

	/**
	 * Applies non-global properties to UI defaults.
	 * Processes and applies individual component properties, excluding
	 * variables and global properties.
	 *
	 * @param defaults   UI defaults to update
	 * @param properties source properties to process
	 * @param resolver   function to resolve property references
	 */
	private void applyPropertiesToDefaults(UIDefaults defaults, Properties properties,
										   Function<String, String> resolver) {
		properties.entrySet().stream()
			.filter(e -> {
				String key = (String) e.getKey();
				return !key.startsWith(VARIABLE_PREFIX) && !key.startsWith(GLOBAL_PREFIX);
			})
			.forEach(e -> {
				String key = (String) e.getKey();
				String value = resolveValue(properties, (String) e.getValue());
				defaults.put(key, parseValue(key, value, resolver));
			});
	}

	/**
	 * Resolves property values, handling variables and references.
	 * Supports optional values and nested references.
	 *
	 * @param properties source properties
	 * @param value      value to resolve
	 * @return resolved value
	 * @throws IllegalArgumentException if a required reference cannot be resolved
	 */
	private String resolveValue(Properties properties, String value) {
		if (!value.startsWith(VARIABLE_PREFIX)) {
			return value;
		}

		if (value.startsWith(REF_PREFIX)) {
			value = value.substring(REF_PREFIX.length());
		}

		boolean optional = false;
		if (value.startsWith(OPTIONAL_PREFIX)) {
			value = value.substring(OPTIONAL_PREFIX.length());
			optional = true;
		}

		String newValue = properties.getProperty(value);
		if (newValue == null) {
			if (optional) {
				return "null";
			}
			String errorMsg = "Variable or reference '" + value + "' not found";
			logger.error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		return resolveValue(properties, newValue);
	}

	/**
	 * Parses various types of UI property values.
	 * Handles colors, integers, insets, borders, and other UI-specific types.
	 *
	 * @param key      property key indicating the expected type
	 * @param value    string value to parse
	 * @param resolver function to resolve references
	 * @return parsed Object appropriate for the property type
	 */
	private Object parseValue(String key, String value, Function<String, String> resolver) {
		value = value.trim();

		switch (value) {
			case "null":
				return null;
			case "false":
				return false;
			case "true":
				return true;
		}

		if (key.endsWith(".border") || key.endsWith("Border")) {
			return parseBorder(value, resolver);
		}

		if (key.endsWith(".icon") || key.endsWith("Icon")) {
			return parseInstance(value);
		}

		if (key.endsWith(".margin") || key.endsWith(".padding") ||
			key.endsWith("Margins") || key.endsWith("Insets")) {
			return parseInsets(value);
		}

		UIScaledValue uiScaledValue = parseUIScaledValue(key, value);
		if (uiScaledValue != null) {
			return uiScaledValue;
		}

		if (key.endsWith("Size") && !key.equals("SplitPane.dividerSize")) {
			return parseSize(value);
		}

		if (key.endsWith("Width") || key.endsWith("Height")) {
			return parseInteger(value, true);
		}

		ColorUIResource color = parseColor(value, false);
		if (color != null) {
			return color;
		}

		Integer integer = parseInteger(value, false);
		if (integer != null) {
			return integer;
		}

		return value;
	}

	/**
	 * Parses border specifications from string values.
	 * Creates appropriate JewelLineBorder or JewelEmptyBorder instances.
	 *
	 * @param value    border specification string
	 * @param resolver function to resolve color references
	 * @return LazyValue that creates the specified border
	 */
	private Object parseBorder(String value, Function<String, String> resolver) {
		if (value.indexOf(',') >= 0) {
			List<String> parts = split(value, ',');
			Insets insets = parseInsets(value);
			ColorUIResource lineColor = (parts.size() == 5) ?
				parseColor(resolver.apply(parts.get(4)), true) : null;

			return (UIDefaults.LazyValue) t -> (lineColor != null) ?
				new JewelLineBorder(insets, lineColor) : new JewelEmptyBorder(insets);
		}
		return parseInstance(value);
	}

	/**
	 * Creates instances of UI classes specified by fully qualified class names.
	 * Used for instantiating custom UI delegates and components.
	 *
	 * @param value fully qualified class name
	 * @return LazyValue that creates the specified instance
	 */
	private Object parseInstance(String value) {
		return (UIDefaults.LazyValue) t -> {
			try {
				return Class.forName(value).getDeclaredConstructor().newInstance();
			} catch (ReflectiveOperationException ex) {
				logger.error("Failed to create instance of " + value + ": " + ex.getMessage());
				return null;
			}
		};
	}

	/**
	 * Parses insets specifications from string values.
	 * Expects four comma-separated integers for top, left, bottom, and right.
	 *
	 * @param value insets specification string
	 * @return InsetsUIResource with the specified values
	 * @throws NumberFormatException if values cannot be parsed
	 */
	private Insets parseInsets(String value) {
		List<String> numbers = split(value, ',');
		try {
			return new InsetsUIResource(
				Integer.parseInt(numbers.get(0)),
				Integer.parseInt(numbers.get(1)),
				Integer.parseInt(numbers.get(2)),
				Integer.parseInt(numbers.get(3))
			);
		} catch (NumberFormatException ex) {
			logger.error("Invalid insets '" + value + "'");
			throw ex;
		}
	}

	/**
	 * Parses dimension specifications from string values.
	 * Expects two comma-separated integers for width and height.
	 *
	 * @param value size specification string
	 * @return DimensionUIResource with the specified dimensions
	 * @throws NumberFormatException if values cannot be parsed
	 */
	private Dimension parseSize(String value) {
		List<String> numbers = split(value, ',');
		try {
			return new DimensionUIResource(
				Integer.parseInt(numbers.get(0)),
				Integer.parseInt(numbers.get(1))
			);
		} catch (NumberFormatException ex) {
			logger.error("Invalid size '" + value + "'");
			throw ex;
		}
	}

	/**
	 * Parses color values from string representation.
	 * Supports 6-digit RGB and 8-digit RGBA hexadecimal formats.
	 *
	 * @param value       the color value as a string
	 * @param reportError whether to report parsing errors
	 * @return ColorUIResource instance or null if parsing fails
	 */
	private ColorUIResource parseColor(String value, boolean reportError) {
		try {
			int rgb = Integer.parseInt(value, 16);
			if (value.length() == 6) {
				return new ColorUIResource(rgb);
			}
			if (value.length() == 8) {
				return new ColorUIResource(new Color(rgb, true));
			}

			if (reportError) {
				throw new NumberFormatException(value);
			}
		} catch (NumberFormatException ex) {
			if (reportError) {
				logger.error("Invalid color '" + value + "'");
				throw ex;
			}
		}
		return null;
	}

	/**
	 * Parses integer values from strings.
	 * Provides optional error reporting for invalid values.
	 *
	 * @param value       string to parse
	 * @param reportError whether to throw exception on parsing failure
	 * @return parsed Integer or null if parsing fails and reportError is false
	 * @throws NumberFormatException if parsing fails and reportError is true
	 */
	private Integer parseInteger(String value, boolean reportError) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			if (reportError) {
				logger.error("Invalid integer '" + value + "'");
				throw ex;
			}
		}
		return null;
	}

	/**
	 * Parses scaled number specifications for specific UI properties.
	 * Handles special cases where values need to be scaled based on UI scaling factor.
	 * Only applies to specific properties like button widths and split pane settings.
	 *
	 * @param key   property key to check if scaling should be applied
	 * @param value string value to parse
	 * @return ScaledNumber instance if applicable, null otherwise
	 * @throws NumberFormatException if value cannot be parsed
	 */
	private UIScaledValue parseUIScaledValue(String key, String value) {
		if (!key.equals("OptionPane.buttonMinimumWidth") &&
			!key.equals("SplitPane.oneTouchButtonSize") &&
			!key.equals("SplitPane.oneTouchButtonOffset")) {
			return null;
		}

		try {
			return UIScaledValue.of(Integer.parseInt(value));
		} catch (NumberFormatException ex) {
			logger.error("Invalid integer '" + value + "'");
			throw ex;
		}
	}

	/**
	 * Splits a string by delimiter character.
	 *
	 * @param str   string to split
	 * @param delim delimiter character
	 * @return list of substrings
	 */
	public static List<String> split(String str, char delim) {
		ArrayList<String> strs = new ArrayList<>();
		int delimIndex = str.indexOf(delim);
		int index = 0;
		while (delimIndex >= 0) {
			strs.add(str.substring(index, delimIndex));
			index = delimIndex + 1;
			delimIndex = str.indexOf(delim, index);
		}
		strs.add(str.substring(index));
		return strs;
	}

	/**
	 * Resets and reapplies the current Look and Feel.
	 * Used to handle system property changes that affect the UI.
	 */
	private static void reSetLookAndFeel() {
		EventQueue.invokeLater(() -> {
			try {
				LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
				UIManager.setLookAndFeel(lookAndFeel);

				PropertyChangeEvent e = new PropertyChangeEvent(
					UIManager.class, "lookAndFeel", lookAndFeel, lookAndFeel);

				for (PropertyChangeListener l : UIManager.getPropertyChangeListeners()) {
					l.propertyChange(e);
				}

				updateUI();
			} catch (UnsupportedLookAndFeelException ex) {
				logger.error("Failed to reset look and feel: " + ex.getMessage());
			}
		});
	}

	/**
	 * Updates the UI for all open windows.
	 * Should be called when the Look and Feel changes or needs refresh.
	 */
	public static void updateUI() {
		for (Window w : Window.getWindows()) {
			SwingUtilities.updateComponentTreeUI(w);
		}
	}

	/**
	 * Checks if mnemonics should be displayed.
	 * Based on ALT key state and system settings.
	 *
	 * @return true if mnemonics should be shown, false otherwise
	 */
	public static boolean isShowMnemonics() {
		return altKeyPressed || !UIManager.getBoolean("Component.hideMnemonics");
	}

	/**
	 * Handles ALT key state changes for mnemonic display.
	 * Updates UI components when mnemonic visibility changes.
	 *
	 * @param pressed true if ALT key is pressed, false otherwise
	 */
	private static void altKeyChanged(boolean pressed) {
		if (pressed == altKeyPressed) {
			return;
		}

		altKeyPressed = pressed;

		if (!UIManager.getBoolean("Component.hideMnemonics")) {
			return;
		}

		Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
		if (focusOwner == null) {
			return;
		}

		Window window = SwingUtilities.windowForComponent(focusOwner);
		if (window == null) {
			return;
		}

		repaintMnemonics(window);
	}

	/**
	 * Repaints components that might display mnemonics.
	 * Recursively processes all components in the container hierarchy.
	 *
	 * @param container container to process
	 */
	private static void repaintMnemonics(Container container) {
		for (Component c : container.getComponents()) {
			if (hasMnemonic(c)) {
				c.repaint();
			}

			if (c instanceof Container) {
				repaintMnemonics((Container) c);
			}
		}
	}

	/**
	 * Checks if a component can display mnemonics.
	 * Supports labels, buttons, and tabbed panes.
	 *
	 * @param c component to check
	 * @return true if the component can display mnemonics
	 */
	private static boolean hasMnemonic(Component c) {
		if (c instanceof JLabel && ((JLabel) c).getDisplayedMnemonicIndex() >= 0) {
			return true;
		}

		if (c instanceof AbstractButton && ((AbstractButton) c).getDisplayedMnemonicIndex() >= 0) {
			return true;
		}

		if (c instanceof JTabbedPane tabPane) {
			int tabCount = tabPane.getTabCount();
			for (int i = 0; i < tabCount; i++) {
				if (tabPane.getDisplayedMnemonicIndexAt(i) >= 0) {
					return true;
				}
			}
		}

		return false;
	}
}
