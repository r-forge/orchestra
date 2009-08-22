# TODO: update because there might nested browsers
.debugenv <- new.env()
.debugenv[["from"]] <- 0

#' function called at the Browse[]> prompt to cache the sys.frames and
#' sys.calls at that time
.cacheBrowseContext <- function( from = .debugenv[["from"]] ){
	
	stack <- sys.calls()
	.debugenv[["callstack"]]  <- if( from == 0 ) head( stack, -1 ) else stack[1:from]
	
	frames <- sys.frames()
	.debugenv[["framestack"]] <- if( from == 0 ) head( frames, -1 ) else frames[1:from]
}


#' retrieve the callstack at the last browse prompt
.getBrowseCallStack <- function(){
	.debugenv[["callstack"]]
}

.getBrowseCallStackNames <- function(){
	sapply( .debugenv[["callstack"]], function(x) limitedLabels(x)[1] )
}

.getBrowseCallStackLocations <- function(){
	lapply( .debugenv[["callstack"]], function(.){
		srcref <- attr( ., "srcref" )
		if( is.null( srcref ) ){
			return( NULL) 
		}
		positions <- as.integer( srcref )
		file <- attr( srcref, "srcfile" )[["filename"]]
		list( file = file , srcref = positions )
	} )
}

#' retrieve the framestack at the last browse prompt
.getBrowseFrameStack <- function(){
	.debugenv[["framestack"]]
}

.getBrowseFrameStackSize <- function( ){
	length( .debugenv[["framestack"]] ) 
}

.getBrowseFrame <- function(index){
	.debugenv[["framestack"]][[ index ]]
}

.flushBrowseContext <- function(){
	.debugenv[["callstack"]] <- NULL
	.debugenv[["framestack"]] <- NULL
}

