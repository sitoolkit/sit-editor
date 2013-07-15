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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class TextFileEditor {

	private static final Logger LOG = LoggerFactory.getLogger(TextFileEditor.class);
	
	private String inputEncoding = "UTF-8";
	
	private String outputEncoding = "UTF-8";
	
	private String outputLineEnding = "\n";
	
	private List<EditorCommand> commands = new ArrayList<EditorCommand>();

	private Finder targetFinder; 
			
	/**
	 * テキストファイルを編集します。
	 * <ol>
	 * <li>編集対象ファイルを読み込みます。
	 * <li>編集対象ファイルの各行でXXXを見つけます。
	 * <li>編集を行います。
	 * 
	 * </ol>
	 * @param 編集対象ファイル
	 * @return 正常終了の場合に0
	 */
	public void edit(File input, File output) throws IOException {
		List<Line> lines = read(input);

		if (lines == null) {
			LOG.info("[{}]は編集対象ではありません。", input.getAbsolutePath());
			FileUtils.copyFile(input, output);
			
		} else {
			for (EditorCommand command : getCommands()) {
				command.execute();
			}

			write(lines, output);
		}
	}
	
	List<Line> read(File input) throws IOException {
		LOG.info("編集対象ファイルを読み込みます。{}", input.getAbsolutePath());
		List<Line> lines = new ArrayList<Line>();

		boolean isTarget = getTargetFinder() == null;
		for (String lineStr : FileUtils.readLines(input, getInputEncoding())) {
			Line line = new Line(lines.size() + 1, lineStr);
			lines.add(line);
			
			if (!isTarget) {
				isTarget = getTargetFinder().find(line);
			}

			for (EditorCommand command : getCommands()) {
				command.find(line);
			}
		}
		
		return isTarget ? lines : null;
	}

	void write(Collection<Line> lines, File output) throws IOException {
		List<String> outputLines = new ArrayList<String>();
		for (Line line : lines) {
			if (line.isDeleted()) {
				LOG.trace("removed line:{}", line);
				continue;
			}
			outputLines.addAll(line.getBefore());
			if (LOG.isTraceEnabled() && !line.getBefore().isEmpty()) {
				LOG.trace("insert before:{}", StringUtils.join(line.getBefore()));
			}

			if (line.getStr() != null) {
				outputLines.add(line.getStr());
				LOG.trace("write line:{}", line);
			}

			outputLines.addAll(line.getAfter());
			if (LOG.isTraceEnabled() && !line.getAfter().isEmpty()) {
				LOG.trace("insert before:{}", StringUtils.join(line.getAfter()));
			}
		}

		LOG.info("編集後ファイルを書き込みます。{}", output.getAbsolutePath());
		FileUtils.writeLines(output, getOutputEncoding(), outputLines, getOutputLineEnding());
	}

	public String getInputEncoding() {
		return inputEncoding;
	}

	public void setInputEncoding(String inputEncoding) {
		this.inputEncoding = inputEncoding;
	}

	public String getOutputEncoding() {
		return outputEncoding;
	}

	public void setOutputEncoding(String outputEncoding) {
		this.outputEncoding = outputEncoding;
	}

	public List<EditorCommand> getCommands() {
		return commands;
	}

	public void setCommands(List<EditorCommand> commands) {
		this.commands = commands;
	}
	
	public void addCommand(EditorCommand command) {
		commands.add(command);
	}

	public String getOutputLineEnding() {
		return outputLineEnding;
	}

	public void setOutputLineEnding(String outputLineEnding) {
		this.outputLineEnding = outputLineEnding;
	}

	public Finder getTargetFinder() {
		return targetFinder;
	}

	public void setTargetFinder(Finder targetFinder) {
		this.targetFinder = targetFinder;
	}
}

