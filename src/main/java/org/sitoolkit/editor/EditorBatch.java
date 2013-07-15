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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * <pre>
 * root
 * +- before
 *    +- a
 *       +- b1
 *          +- c1.txt
 *       +- b2
 *          +- c2.txt
 * +- after
 *    +- a
 *       +- b1
 *          +- c1.txt
 *       +- b2
 *          +- c2.txt
 * </pre>
 * 
 * 
 * @author yuichi.kuwahara
 */
public class EditorBatch {

	private static final Logger LOG = LoggerFactory.getLogger(EditorBatch.class);

	@Autowired
	ApplicationContext appCtx;
	
	private String[] extensions;
	
	private List<String> normalizedExtensions = new ArrayList<String>();
	
	private String dstRootDirSuffix = DateFormatUtils.format(
				System.currentTimeMillis(), "yyyyMMddHHmmss");
	
	public static void main(String[] args) {
		ApplicationContext appCtx = new ClassPathXmlApplicationContext("");
		EditorBatch batch = appCtx.getBean(EditorBatch.class);
		System.exit(batch.execute(args[0]));
	}
	
	public int execute(String srcRootDirPath) {
		int exitCode = 0;
		File srcRootDir = new File(srcRootDirPath);
		LOG.info("編集ルート:{}", srcRootDir.getAbsolutePath());
		LOG.info("編集ファイル拡張子:{}", Arrays.toString(getExtensions()));

		File dstRootDir = new File(srcRootDir.getAbsolutePath() + "_" + getDstRootDirSuffix());
		LOG.info("出力先ルート:{}", dstRootDir.getAbsolutePath());

		Map<File, File> fileMap = extract(srcRootDir, dstRootDir);
		normalizeExtentions();
				
		for (Entry<File, File> entry : fileMap.entrySet()) {
			File src = entry.getKey();
			File dst = entry.getValue();
			try {
				if (isEditingFile(src)) {
					TextFileEditor editor = appCtx.getBean(TextFileEditor.class);
					editor.edit(src, dst);
				} else {
					LOG.info("編集対象外のファイルをコピーします。{} to {}",
							src.getAbsolutePath(), dst.getAbsolutePath());
					FileUtils.copyFile(src, dst);
				}
			} catch (IOException e) {
				LOG.error("", e);
				exitCode = -1;
			}
		}
		return exitCode;
	}
	
	Map<File, File> extract(File srcRootDir, File dstRootDir) {
		Map<File, File> map = new HashMap<File, File>();

		for (File srcFile : FileUtils.listFiles(srcRootDir, null, true)) {
			String dstFilePath = srcFile.getAbsolutePath().replace(
					srcRootDir.getAbsolutePath(),
					dstRootDir.getAbsolutePath());
			File dstFile = new File(dstFilePath);
			map.put(srcFile, dstFile);
		}
		
		return map;
	}
	
	void normalizeExtentions() {
		normalizedExtensions.clear();
		
		for (String ext : getExtensions()) {
			if (ext.startsWith(".")) {
				normalizedExtensions.add(ext.toLowerCase());
			} else {
				normalizedExtensions.add("." + ext.toLowerCase());
			}
		}
	}
	
	boolean isEditingFile(File file) {
		if (!file.isFile()) {
			return false;
		}
		for (String ext : normalizedExtensions) {
			String path = file.getPath().toLowerCase();
			if (path.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	public String[] getExtensions() {
		return extensions;
	}

	public void setExtensions(String[] extensions) {
		this.extensions = extensions;
	}

	public String getDstRootDirSuffix() {
		return dstRootDirSuffix;
	}

	public void setDstRootDirSuffix(String dstRootDirSuffix) {
		this.dstRootDirSuffix = dstRootDirSuffix;
	}
}
