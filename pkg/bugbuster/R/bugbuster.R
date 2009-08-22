# TODO: update because there might nested browsers
.debugenv <- new.env()
.debugenv[["from"]] <- 0
.debugenv[["stack"]] <- NULL
.debugenv[["listeners"]] <- NULL

getBrowseFrom <- function( ){
	.debugenv[["from"]]
}

setBrowseFrom <- function( from = 0L ){
	if( length( from ) != 1L ){
		stop( "`from` should be of length one" )
	}
	if( ! ( is.integer( from ) || is.numeric( from ) ) ){
		stop( "`from` should be a number (numeric or integer)" ) 
	}
	if( is.numeric( from ) ) {
		from <- as.integer( from )
	}
	if( from < 0L ){
		from <- 0L
	}
	.debugenv[["from"]] <- from
	from
}

#' function called at the Browse[]> prompt to cache the sys.frames and
#' sys.calls at that time
cacheBrowseContext <- function( from = getBrowseFrom() ){
	setBrowseFrom( .debugenv[["from"]] )
	
	keep <- function( stack, from ){
		if( from == 0 ) head( stack, -1 ) else stack[1:from]
	}
	
	# grab the call stack and the frame stack
	callstack <- sys.calls()
	callstack <- keep( callstack, from )
	frames <- sys.frames()
	frames   <- keep( frames, from )
	
	size <- length( callstack ) 
	out <- lapply( seq_len( size ), function(i){
		list( call = callstack[[i]], frame = frames[[i]] )
	} )
	names( out ) <- sapply( callstack, function(x) limitedLabels(x)[1] )
	.debugenv[["stack"]] <- out
	signalConditionAndListeners( 
		debugStackChangeCondition("update") )
	invisible( NULL )
}

signalConditionAndListeners <- function( con ){
	# signal the condition
	signalCondition( con )
	
	# notify the listeners
	if( ! is.null( .debugenv[["listeners"]] ) ){
		lapply( .debugenv[["listeners"]], function(f){
			f(con) 
		} )
	}
}

debugStackChangeCondition <- function( kind = c("update", "flush") ){
	kind <- match.arg( kind )
	con <- simpleCondition( kind )
	class(con) <- c( sprintf( "debugStackChange-%s", kind ), class(con))
	con
}

flushBrowseContext <- function( ){
	.debugenv[["stack"]] <- NULL
	signalConditionAndListeners( 
		debugStackChangeCondition("flush") )
	invisible( )
}

addBrowseStackChangeListener <- function( listener ){
	# first check that the listener has the correct format
	if( !is.function( listener ) ){
		stop( "`listener` should be a function" )
	}
	if( length( args(listener) ) != 1L ){
		stop( "`listener` should have exactly one argument")
	}
	# then add it to the list
	.debugenv[["listeners"]] <- append( list( listener ), .debugenv[["listeners"]] )
}

getBrowseContext <- function( ){
	.debugenv[["stack"]]
}

getBrowseContextSize <- function(){
	length( .debugenv[["stack"]] )
}
