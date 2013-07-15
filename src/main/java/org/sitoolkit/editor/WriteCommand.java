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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <dl>
 * <dt>上書き
 * <dd>編集前の行の文字列を、新しい固定の文字列で置き換えます。
 * <pre>
 * "abc" -> "xyz"
 * "def" -> "xyz"
 * "hij" -> "xyz"
 * </pre>
 * 
 * <dt>置換
 * <dd>
 * <pre>
 * "abc" -> "    abc"
 * "def" -> "    def"
 * "hij" -> "    hij"
 * </pre>
 * </dl>
 * @author yuichi.kuwahara
 */
public class WriteCommand extends EditorCommand {

	/**
	 * 単純上書きをする文字列
	 */
	private List<String> newLines = new ArrayList<String>();

	/**
	 * 
	 */
	private String replaceRegex;

	/**
	 * 編集前の文字列に一致した部分を置換する文字列
	 */
	private String replacement;
	
	public WriteCommand() {
		super();
	}

	public WriteCommand(String startPattern, String endPattern, String newLine) {
		super(startPattern, endPattern);
		this.newLines.add(newLine);
	}

	public WriteCommand(String startPattern, String endPattern, String replaceRegex, String replacement) {
		super(startPattern, endPattern);
		this.replaceRegex = replaceRegex;
		this.replacement = replacement;
	}
	
	public void edit() {
		if (getReplaceRegex() == null) {
			// 単純上書き
			for (Line line : getTargetLines()) {
				line.setStr(null);
			}
			getTargetLines().get(0).getBefore().addAll(getNewLines());
		} else {
			for (Line line : getTargetLines()) {
				line.setStr(line.getStr().replaceAll(getReplaceRegex(), getReplacement()));
			}
		}
	}

	public String getReplaceRegex() {
		return replaceRegex;
	}

	public void setReplaceRegex(String replaceRegex) {
		this.replaceRegex = replaceRegex;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public List<String> getNewLines() {
		return newLines;
	}

	public void setNewLines(List<String> newLines) {
		this.newLines = newLines;
	}

	public static void main(String[] args) {
		System.out.println("\t<table id=\"adm_header\">".matches(".*<table id=\"(adm_header|ins_header)\">"));
	}
}
