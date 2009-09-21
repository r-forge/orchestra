/*
 * Copyright (c) 2009, Romain Francois <francoisromain@free.fr>
 *
 * This file is part of the biocep editor plugin.
 *
 * The biocep editor plugin is free software: 
 * you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The biocep editor plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the biocep editor plugin. If not, see <http://www.gnu.org/licenses/>.
 */

package com.addictedtor.jedit.sidekick;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.EditPane;

import sidekick.SideKickCompletion;
import sidekick.SideKickParsedData;
import sidekick.SideKickParser;

import com.addictedtor.jedit.sidekick.packagesourcemanager.Namespace;
import com.addictedtor.jedit.sidekick.packagesourcemanager.PackageSourceManager;

import errorlist.DefaultErrorSource;

public class NamespaceFileParser extends SideKickParser {

	public static final DefaultErrorSource error = new DefaultErrorSource(
			"NamespaceFileParser");

	public NamespaceFileParser(String serviceName) {
		super(serviceName);
	}

	public NamespaceFileParser() {
		this("NAMESPACE");
	}

	@Override
	public SideKickParsedData parse(Buffer buffer,
			DefaultErrorSource errorSource) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("NAMESPACE");
		SideKickParsedData data = new SideKickParsedData(buffer.getPath());
		Namespace NAMESPACE = new Namespace(new File(buffer.getPath()));
		PackageSourceManager packageManager = Namespace
				.packageManager(NAMESPACE.getFilename());
		if (packageManager == null) {
			NAMESPACE.check(errorSource, null);
		} else {
			packageManager.check();
		}
		data.root.add(node);
		return data;
	}

	@Override
	public boolean supportsCompletion() {
		return true;
	}

	@Override
	public boolean canCompleteAnywhere() {
		return true;
	}

	@Override
	public SideKickCompletion complete(EditPane editPane, int caret) {
		return null;
	}

}
