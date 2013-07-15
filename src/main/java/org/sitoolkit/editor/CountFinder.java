/*
 * Copyright 2013 Monocrea Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sitoolkit.editor;

import java.util.regex.Pattern;

/**
 * このクラスは、探す条件を表すVOです。
 * 
 * @author yuichi.kuwahara
 */
public class CountFinder implements Finder {
	
	private Pattern pattern;
	private int nth = 1;
	private int counter = 1;

	public CountFinder() {
		super();
	}
	
	public CountFinder(String pattern) {
		setPattern(pattern);
	}
	
	public CountFinder(String pattern, int nth) {
		this(pattern);
		this.nth = nth;
	}
	
	public boolean find(Line line) {
		if (counter <= nth) {
			boolean finded = pattern.matcher(line.getStr()).matches();
			if (finded) {
				counter++;
			}
			return nth < counter;
		} else {
			return false;
		}
	}
	
	public void setPattern(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	public int getNth() {
		return nth;
	}

	public void setNth(int nth) {
		this.nth = nth;
	}
}
