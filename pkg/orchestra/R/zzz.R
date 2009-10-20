
# this is just to make R CMD check happy 
view <- NULL
textArea <- NULL

isInJedit <- function(){
	tryCatch( is( .jfindClass( "org/gjt/sp/jedit/jEdit" ), "jobjRef" ) , Exception = function(e) FALSE )
}

.onLoad <- function( libname, pkgname){
	
	if( ! isInJedit() ){
		cat( "this package is only useful if used from within jedit" ) 
	} else{
		.flushBrowseContext() 
		
		nm <- getNamespace( "orchestra") 
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
	if( Sys.getenv( "ORCHESTRA_HOME", unset = "" ) == "" ){
		unlockBinding( "buffer", .GlobalEnv )
		rm( list = "buffer", envir = .GlobalEnv )
	}
}

