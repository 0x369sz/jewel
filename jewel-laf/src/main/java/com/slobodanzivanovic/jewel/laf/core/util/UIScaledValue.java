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

package com.slobodanzivanovic.jewel.laf.core.util;

import java.io.Serial;
import java.util.Objects;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * Represents a UI measurement that automatically scales based on the system's DPI settings.
 */
public final class UIScaledValue extends Number {
	@Serial
	private static final long serialVersionUID = 1L;
	private final int baseValue;

	/**
	 * Creates a new UIScaledValue with the specified base value.
	 * The actual value used will be scaled according to the system's DPI settings.
	 *
	 * @param baseValue The base value before scaling
	 */
	public UIScaledValue(int baseValue) {
		this.baseValue = baseValue;
	}

	/**
	 * Gets the unscaled base value.
	 *
	 * @return The original, unscaled value
	 */
	public int getBaseValue() {
		return baseValue;
	}

	@Override
	public int intValue() {
		return scale(baseValue);
	}

	@Override
	public long longValue() {
		return scale(baseValue);
	}

	@Override
	public float floatValue() {
		return scale((float) baseValue);
	}

	@Override
	public double doubleValue() {
		return scale((float) baseValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(baseValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof UIScaledValue other)) return false;
		return baseValue == other.baseValue;
	}

	@Override
	public String toString() {
		return String.format("UIScaledValue[base=%d, scaled=%d]", baseValue, intValue());
	}

	/**
	 * Creates a scaled value from an integer.
	 *
	 * @param value The base value to scale
	 * @return A new UIScaledValue instance
	 */
	public static UIScaledValue of(int value) {
		return new UIScaledValue(value);
	}
}
