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
import javax.annotation.Resource;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.*;

/**
 *
 * @author yuichi.kuwahara
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TextFileEditorTest.TextFileEditorConfig.class,
		loader = AnnotationConfigContextLoader.class)
public class TextFileEditorTest {

	@Resource(name = "cutAndPasteEditor")
	TextFileEditor cutAndPasteEditor;

	@Resource(name = "writeEditor")
	TextFileEditor writeEditor;

	/**
	 * <dl>
	 * <dt>ケース
	 * <dd>切り取り編集
	 * 
	 * <dt>コンディション
	 * <dd>編集前のテキストファイル
	 * <pre>
	 * a
	 * b
	 * c
	 * d
	 * e
	 * </pre>
	 * 編集：「c」と「d」の行を「a」の行の前に移動
	 * <dt>期待する結果
	 * <dd>
	 * 編集後のテキストファイル
	 * <pre>
	 * c
	 * d
	 * a
	 * b
	 * e
	 * </pre>
	 * </dl>
	 */
	@Test
	public void testCutAndPaste() throws Exception {
		execAndAssert(cutAndPasteEditor, "CutAndPaste.txt");
	}

	/**
	 * <dl>
	 * <dt>ケース
	 * <dd>書き込み編集
	 * 
	 * <dt>コンディション
	 * <dd>編集前のテキストファイル
	 * <pre>
	 * a
	 * b
	 * c
	 * d
	 * </pre>
	 * 編集：「a」の行を「dfgh」に変更、「b」と「c」の行の前にタブを挿入
	 * <dt>期待する結果
	 * <dd>
	 * 編集後のテキストファイル
	 * <pre>
	 * efgh
	 *	b
	 *	c
	 * d
	 * </pre>
	 * </dl>
	 */
	@Test
	public void testWrite() throws Exception {
		execAndAssert(writeEditor, "Write.txt");
	}

	void execAndAssert(TextFileEditor editor, String fileName) throws Exception {
		File input = new File("in/" + fileName);
		File output = new File("out/" + fileName);
		editor.edit(input, output);
		
		String actual = FileUtils.readFileToString(output).replaceAll("\n|\r\n", "");
		String expected = FileUtils.readFileToString(
				new File("expected/" + fileName)).replaceAll("\n|\r\n", "");
		
		assertEquals(expected, actual);
	}
		
	@Configuration
	public static class TextFileEditorConfig {
		
		@Bean(name = "cutAndPasteEditor")
		public TextFileEditor getCutAndPasteEditor() {
			TextFileEditor editor = new TextFileEditor();

			editor.addCommand(new CopyCommand("c", "d", "a", true));

			return editor;
		}
		
		@Bean(name = "writeEditor")
		public TextFileEditor getWriteEditor() {
			TextFileEditor editor = new TextFileEditor();
			
			editor.addCommand(new WriteCommand("b", "c", "^", "\t"));
			editor.addCommand(new WriteCommand("a", null, "efgh"));

			return editor;
		}
	}
}