package com.addictedtor.jedit.statusbar;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.EditBus;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.msg.BufferUpdate;

public class BufferSynchronizer {

	public static void synchronize( Buffer buffer, boolean live ){
		buffer.setProperty( "R-livebuffer" , new RLiveBufferProperty( live ) ) ;
		EditBus.send( new BufferUpdate( buffer, jEdit.getActiveView(), BufferUpdate.DIRTY_CHANGED ) ) ;
	}
}
