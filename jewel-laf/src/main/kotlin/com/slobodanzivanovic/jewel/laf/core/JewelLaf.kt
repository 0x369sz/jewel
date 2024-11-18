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

package com.slobodanzivanovic.jewel.laf.core

import java.io.IOException
import java.util.*
import javax.swing.UIDefaults
import javax.swing.plaf.ColorUIResource
import javax.swing.plaf.basic.BasicLookAndFeel
import javax.swing.plaf.metal.MetalLookAndFeel

abstract class JewelLaf : BasicLookAndFeel() {
	private var basicLookAndFeel: BasicLookAndFeel? = null

	override fun getID(): String {
		return name
	}

	override fun isNativeLookAndFeel(): Boolean {
		return true
	}

	override fun isSupportedLookAndFeel(): Boolean {
		return true
	}

	override fun initialize() {
		getBasicLookAndFeel().initialize()
		super.initialize()
	}

	override fun uninitialize() {
		if (basicLookAndFeel != null) {
			basicLookAndFeel!!.uninitialize()
		}
		super.uninitialize()
	}

	private fun getBasicLookAndFeel(): BasicLookAndFeel {
		if (basicLookAndFeel == null) {
			basicLookAndFeel = MetalLookAndFeel()
		}
		return basicLookAndFeel!!
	}

	override fun getDefaults(): UIDefaults {
		val defaults = getBasicLookAndFeel().defaults

		try {
			val properties = loadProperties()

			// First, handle global properties
			val globalDefaults = extractGlobalDefaults(properties)
			applyGlobalDefaults(defaults, globalDefaults)

			// Then handle specific properties
			for (key in properties.stringPropertyNames()) {
				if (!key.startsWith(VARIABLE_PREFIX) && !key.startsWith(GLOBAL_PREFIX)) {
					val value = resolveValue(properties, properties.getProperty(key))
					defaults[key] = parseValue(value)
				}
			}
		} catch (e: IOException) {
			System.err.println("Error loading properties: " + e.message)
		}

		return defaults
	}

	@Throws(IOException::class)
	private fun loadProperties(): Properties {
		val properties = Properties()
		val propertiesFileName = "/" + javaClass.name.replace('.', '/') + ".properties"

		javaClass.getResourceAsStream(propertiesFileName).use { inputStream ->
			if (inputStream != null) {
				properties.load(inputStream)
			}
		}
		// Load parent properties if they exist
		val parentPropertiesFileName = "/" + JewelLaf::class.java.name.replace('.', '/') + ".properties"
		JewelLaf::class.java.getResourceAsStream(parentPropertiesFileName).use { parentInputStream ->
			if (parentInputStream != null) {
				val parentProperties = Properties()
				parentProperties.load(parentInputStream)
				// Parent properties are loaded second so they don't override child properties
				for (key in parentProperties.stringPropertyNames()) {
					if (!properties.containsKey(key)) {
						properties[key] = parentProperties.getProperty(key)
					}
				}
			}
		}
		return properties
	}

	private fun extractGlobalDefaults(properties: Properties): Map<String, Any?> {
		val globals: MutableMap<String, Any?> = HashMap()
		for (key in properties.stringPropertyNames()) {
			if (key.startsWith(GLOBAL_PREFIX)) {
				val globalKey = key.substring(GLOBAL_PREFIX.length)
				val value = resolveValue(properties, properties.getProperty(key))
				globals[globalKey] = parseValue(value)
			}
		}
		return globals
	}

	private fun applyGlobalDefaults(defaults: UIDefaults, globalDefaults: Map<String, Any?>) {
		for (key in defaults.keys) {
			if (key is String) {
				val lastDot = key.lastIndexOf('.')
				if (lastDot >= 0) {
					val suffix = key.substring(lastDot + 1)
					val globalValue = globalDefaults[suffix]
					if (globalValue != null) {
						defaults[key] = globalValue
					}
				}
			}
		}
	}

	private fun resolveValue(properties: Properties, value: String): String {
		if (value.startsWith(VARIABLE_PREFIX)) {
			val resolvedValue = properties.getProperty(value)

			if (resolvedValue != null) {
				// If the resolved value is also a variable, resolve it recursively
				if (resolvedValue.startsWith(VARIABLE_PREFIX)) {
					return resolveValue(properties, resolvedValue)
				}
				return resolvedValue
			}
		}
		return value
	}

	private fun parseValue(value: String?): Any? {
		var value = value ?: return null

		value = value.trim { it <= ' ' }

		if (value.matches("[0-9A-Fa-f]{6}".toRegex())) {
			try {
				val rgb = value.toInt(16)
				return ColorUIResource(rgb)
			} catch (e: NumberFormatException) {
				// Not a valid hex color
			}
		}

		// Try parsing as integer
		try {
			return value.toInt()
		} catch (e: NumberFormatException) {
			// Not an integer
		}

		// Return as string if no other parsing succeeded
		return value
	}

	companion object {
		private const val VARIABLE_PREFIX = "@"
		private const val GLOBAL_PREFIX = "*."
	}
}
