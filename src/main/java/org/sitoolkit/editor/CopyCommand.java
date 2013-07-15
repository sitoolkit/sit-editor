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
 * このクラスは、「コピー」編集を表すエンティティです。
 * @author yuichi.kuwahara
 */
public class CopyCommand extends EditorCommand {
	
	/**
	 * 挿入先の行を探すためのFinder
	 */
	private Finder insertFinder;
	/**
	 * 挿入先の行
	 */
	private Line insertLine;

	private Position position = Position.before;
	/**
	 * 切り取りの場合(コピー元を削除する)にtrue
	 */
	private boolean cut;

	private int removeRowsTop = 0;
	
	private int removeRowsBottom = 0;
	/**
	 * 
	 */
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

	private String replaceRegex;

	/**
	 * 編集前の文字列に一致した部分を置換する文字列
	 */
	private String replacement;

	
	public CopyCommand() {
		super();
	}
	
	public CopyCommand(String startPattern, String endPattern, String insertPattern, boolean cut) {
		super(startPattern, endPattern);
		this.insertFinder = new CountFinder(insertPattern);
		this.cut = cut;
	}
	
	/**
	 * コピー開始位置、コピー終了位置、コピー先位置を特定します。
	 * @param line 
	 */
	public void find(Line line) {
		if (insertFinder.find(line)) {
			insertLine = line;
		}
		super.find(line);
	}
	
	public void edit() {
		if (insertLine == null) {
			log.warn("[{}]の挿入先行が特定できません。", getName());
			return;
		} 

		List<String> insertingLines = position == Position.before
				? insertLine.getBefore()
				: insertLine.getAfter();

		if (position == Position.replace) {
			insertLine.setStr(null);
		}

		List<Line> removedLines = getTargetLines().subList(
				getRemoveRowsTop(), 
				getTargetLines().size() - getRemoveRowsBottom());
		for (Line line : removedLines) {
			String str = getReplaceRegex() == null
					? line.getStr()
					: line.getStr().replaceAll(getReplaceRegex(), getReplacement());
			insertingLines.add(str);
			line.setDeleted(cut);
		}
	}

	public Finder getInsertFinder() {
		return insertFinder;
	}

	public void setInsertFinder(Finder insertFinder) {
		this.insertFinder = insertFinder;
	}

	public boolean isCut() {
		return cut;
	}

	public void setCut(boolean cut) {
		this.cut = cut;
	}
	
	public void setPositionStr(String position) {
		this.position = Position.valueOf(position);
	}

	public int getRemoveRowsTop() {
		return removeRowsTop;
	}

	public void setRemoveRowsTop(int removeRowsTop) {
		this.removeRowsTop = removeRowsTop;
	}

	public int getRemoveRowsBottom() {
		return removeRowsBottom;
	}

	public void setRemoveRowsBottom(int removeRowsBottom) {
		this.removeRowsBottom = removeRowsBottom;
	}

	enum Position {
		before,
		replace,
		after
	}
	
}
