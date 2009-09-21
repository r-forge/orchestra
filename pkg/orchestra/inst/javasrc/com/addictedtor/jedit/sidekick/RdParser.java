package com.addictedtor.jedit.sidekick;

import java.io.File;
import java.util.Date;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.EditPane;

import sidekick.SideKickCompletion;
import sidekick.SideKickParsedData;
import sidekick.SideKickParser;
import errorlist.DefaultErrorSource;

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
public class RdParser extends SideKickParser {

	public RdParser(String serviceName) {
		super(serviceName);
	}

	public RdParser() {
		this("Rd");
	}

	@Override
	public SideKickParsedData parse(Buffer buffer,
			DefaultErrorSource errorSource) {
		SideKickParsedData data = new SideKickParsedData(buffer.getPath());
		new RdParsedFile(buffer.getPath(), data.root);
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

	public static void check(File file, DefaultErrorSource errorSource,
			Date lastCheck) {
		return;
	}
}
