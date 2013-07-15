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
 * ファイルの1行を表すエンティティ
 * @author yuichi.kuwahara
 */
public class Line {

	/**
	 * 編集前の状態のファイル内での行番号
	 */
	private int rownum;
	
	/**
	 * 編集前の状態のファイル1行の文字列
	 */
	private String str;

	private boolean deleted = false;

	/**
	 * 編集後のファイルでこの行の前に挿入する文字列
	 */
	private List<String> before = new ArrayList<String>();

	/**
	 * 編集後のファイルでこの行の後ろに挿入する文字列
	 */
	private List<String> after = new ArrayList<String>();

	public Line(String str) {
		this.str = str;
	}
	
	public Line(int rownum, String str) {
		this(str);
		this.rownum = rownum;
	}

	public int getRownum() {
		return rownum;
	}

	public void setRownum(int rownum) {
		this.rownum = rownum;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public List<String> getBefore() {
		return before;
	}
			
	public void setBefore(List<String> before) {
		this.before = before;
	}

	public List<String> getAfter() {
		return after;
	}
	
	public void setAfter(List<String> after) {
		this.after = after;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return str;
	}
}
