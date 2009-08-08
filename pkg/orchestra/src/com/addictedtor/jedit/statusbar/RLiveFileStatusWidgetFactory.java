package com.addictedtor.jedit.statusbar;

import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.statusbar.StatusWidgetFactory;

public class RLiveFileStatusWidgetFactory implements StatusWidgetFactory {

	/**
	 * @return the R widget
	 */
	@Override
	public RLiveFileWidget getWidget(View view) {
		RLiveFileWidget w = new RLiveFileWidget( view ) ;
		return w ;
	}
	

}
