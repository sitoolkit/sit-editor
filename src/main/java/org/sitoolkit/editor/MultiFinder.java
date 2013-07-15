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

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

/**
 *
 * @author yuichi.kuwahara
 */
public class MultiFinder implements Finder {

	private Queue<Pattern> patterns = new LinkedList<Pattern>();

	public MultiFinder() {
	}

	public MultiFinder(String...patterns) {
		this();
		
		for (String pattern : patterns) {
			this.patterns.add(Pattern.compile(pattern));
		}
		
	}
	
	public boolean find(Line line) {
		if (patterns.isEmpty()) {
			return false;
		}
		Pattern p = patterns.peek();
		
		if (p.matcher(line.getStr()).matches()) {
			patterns.remove();
			
			return patterns.isEmpty();
		} else {
			return false;
		}
	}

	public Queue<Pattern> getPatterns() {
		return patterns;
	}

	public void setPatterns(Queue<Pattern> patterns) {
		this.patterns = patterns;
	}

}
