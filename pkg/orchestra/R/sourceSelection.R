
#' source the expression that is after the start offset in the file
#' 
#' @param file the source file
#' @param start the offset where to source from
#' @param env the environment in which to source
sourceNextExpression <- function( file, start = 0, env = .GlobalEnv ){
	f <- file( file, open = "r" ); on.exit( close( f ) )
	seek( f, start )
	p <- try( parse( f, n = 1 ), silent = TRUE )
	if( ! inherits( p, "try-error" ) ){
		eval( p, envir = env )
	}
}


