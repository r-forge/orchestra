
# this is just to make R CMD check happy 
view <- NULL
textArea <- NULL

.onLoad <- function( libname, pkgname){

	if( Sys.getenv( "JEDITR_HOME", unset = "" ) == "" ){
		cat( "this package is only useful when used with jedit\n" )
		
		
	} else{
		.flushBrowseContext() 
		
		nm <- getNamespace( "jeditr") 
		makeActiveBinding( "buffer", function(arg){
			v <- .jcall( "org/gjt/sp/jedit/jEdit", "Lorg/gjt/sp/jedit/View;", "getActiveView" )
			if(missing(arg)){
				b <- .jcall( v, "Lorg/gjt/sp/jedit/Buffer;", "getBuffer" )
				.jcall( b, "S", "getPath" ) 
			} else{
				._setBuffer( arg ) 
			}
		}, .GlobalEnv )
		
		makeActiveBinding( "view", function(arg){
			if( missing(arg) ){
				.jcall( "org/gjt/sp/jedit/jEdit", "Lorg/gjt/sp/jedit/View;", "getActiveView" )
			} else{
				stop( "<- is not supported for binding `view` " )
			}
		}, nm )
		
		makeActiveBinding( "textArea", function(arg){
			if( missing(arg) ){
				v <- .jcall( "org/gjt/sp/jedit/jEdit", "Lorg/gjt/sp/jedit/View;", "getActiveView" )
				.jcall( v, "Lorg/gjt/sp/jedit/textarea/JEditTextArea;", "getTextArea" )
			} else{
				stop( "<- is not supported for binding `textArea` " )
			}
		}, nm )
		
		# set the options we need
		options( pager  = jedit.pager   )
		options( gui    = "jedit"       )
		options( editor = jedit.editor  )
		options( error  = jedit.recover )
    	
		# setup the call back that flushes the browse cache
		# h <- taskCallbackManager()
		# h$add( function(expr, value, ok, visible) {
    	#   .flushBrowseContext() 
    	#    return(TRUE)
		# }, name = "jeditFlushBrowseCache" )
	}
}

.onUnload <- function( libname, pkgname){
	unlockBinding( "buffer", .GlobalEnv )
	rm( list = "buffer", envir = .GlobalEnv )
}

