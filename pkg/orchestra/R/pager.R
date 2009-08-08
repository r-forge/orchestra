
._setBuffer <- function( file, synchronize = FALSE ){
	if( !is.character(file) || length(file) != 1 ){
		stop( "should be a character vector of length one" )
	}
	.jcall( "com/addictedtor/jedit/tools/BufferSetter", "V" , "set", 
		.jnew( "java/lang/String", file ) )
	invisible( NULL )
	
}

jedit.pager <- function( files, header, title, delete.file ){
	sapply( files, ._setBuffer )
	invisible( NULL )
}

jedit.editor <- function( name, file, title ){
	sapply( file, ._setBuffer, synchronize = TRUE )
}

