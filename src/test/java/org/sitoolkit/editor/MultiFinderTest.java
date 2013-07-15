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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yuichi.kuwahara
 */
public class MultiFinderTest {
	
	@Test
	public void testOne() {
		MultiFinder finder = new MultiFinder("b");
		
		assertFalse(finder.find(new Line("a")));
		assertTrue(finder.find(new Line("b")));
		assertFalse(finder.find(new Line("c")));
		assertFalse(finder.find(new Line("b")));
	}
	
	@Test
	public void testTree() {
		MultiFinder finder = new MultiFinder("a", "b", "c");
		
		assertFalse(finder.find(new Line("a")));
		assertFalse(finder.find(new Line("c")));
		assertFalse(finder.find(new Line("b")));
		assertTrue(finder.find(new Line("c")));
		assertFalse(finder.find(new Line("c")));
	}
}