
.getBrowseCallStackLocations <- function(){
	calls <- sys.calls()
	calls <- head( calls, -1 )
	lapply( calls , function(.){
		srcref <- attr( ., "srcref" )
		if( is.null( srcref ) ){
			return( NULL) 
		}
		positions <- as.integer( srcref )
		file <- attr( srcref, "srcfile" )[["filename"]]
		list( file = file , srcref = positions )
	} )
}

