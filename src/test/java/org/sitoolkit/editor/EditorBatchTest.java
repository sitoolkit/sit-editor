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

import java.io.File;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author yuichi.kuwahara
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:sit-editor-conf.xml")
public class EditorBatchTest {

	@Autowired
	EditorBatch batch;

	// まだやらない。
//	@Test
	public void testSomeMethod() {
		assertEquals(0, batch.execute("in/root"));
		
		Collection<File> actual = FileUtils.listFiles(
				new File("in/root_testresult"), new String[]{"txt"}, true);
		
		assertEquals(2, actual.size());
	}
}