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
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yuichi.kuwahara
 */
public abstract class EditorCommand {

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 編集内容を表す名前
	 */
	private String name = "名前無しの編集";

	/**
	 * 編集開始位置
	 */
	private Finder startFinder;
	/**
	 * 編集終了位置
	 */
	private Finder endFinder;

	private State state = State.finding;

	private List<Line> targetLines = new ArrayList<Line>();

	public EditorCommand() {
	}
	
	public EditorCommand(String startPattern, String endPattern) {
		this();
		if (startPattern != null) {
			this.startFinder = new CountFinder(startPattern);
		}
		if (endPattern != null) {
			this.endFinder = new CountFinder(endPattern);
		}
	}

	public Finder getStartFinder() {
		return startFinder;
	}

	public void setStartFinder(Finder startFinder) {
		this.startFinder = startFinder;
	}

	public Finder getEndFinder() {
		return endFinder;
	}

	public void setEndFinder(Finder endFinder) {
		this.endFinder = endFinder;
	}

	public List<Line> getTargetLines() {
		return Collections.unmodifiableList(targetLines);
	}
	
	public void find(Line line) {
		switch (state) {
			case finding:
				if (startFinder == null || startFinder.find(line)) {
					log.info("[{}]の編集開始位置が{}行目に見つかりました。", 
							getName(), line.getRownum());
					targetLines.add(line);
					state = endFinder == null ? State.end : State.reading;
				}
				break;
			case reading:
				targetLines.add(line);
				if (endFinder.find(line)) {
					log.info("[{}]の編集終了位置が{}行目に見つかりました。", 
							getName(), line.getRownum());
					state = State.end;
				}
				break;
			case end:
			default:
		}
	}

	public void execute() {
		if (getTargetLines().isEmpty()) {
			log.warn("[{}]の編集対象行が特定できません。", getName());
		} else {
			edit();
		}
	}

	protected abstract void edit();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected enum State {
		finding,
		reading,
		end
	}
}
