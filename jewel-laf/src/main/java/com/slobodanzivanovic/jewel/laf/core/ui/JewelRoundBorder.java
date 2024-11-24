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

package com.slobodanzivanovic.jewel.laf.core.ui;

import javax.swing.*;

import static com.slobodanzivanovic.jewel.laf.core.util.UIScale.scale;

/**
 * A rounded border implementation for the Jewel Look and Feel.
 */
public class JewelRoundBorder extends JewelBorder {
	protected final int arc = UIManager.getInt("Component.arc");

	@Override
	protected float getArc() {
		return scale((float) arc);
	}
}
