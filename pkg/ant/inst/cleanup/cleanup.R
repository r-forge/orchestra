#!/bin/env Rscript

h2 <- function( txt ){
	sprintf( "<h2>%s</h2>\n", txt )
}
link <- function( txt, href ){
	sprintf( '<a href="%s">%s</a>', href, txt ) 
}
line <- function( link, txt ){
	sprintf( '<tr><td width="25%%">%s</td>\n<td>%s</td></tr>\n', link, txt )
}

ooindex.file <- file.path( Sys.getenv( "R_PACKAGE_DIR" ) , "html", "00Index.html" )
if( file.exists( ooindex.file ) ){
	# read the 00Index.html file
	ooindex <- readLines( ooindex.file )
	bottom <- grep( "^</table>", ooindex)
	bottom <- bottom[ length(bottom) ]
	
	# add java specific things
	txt <- paste( 
		'</table>\n', 
		h2( "Java" ), 
		'<table width="100%">\n',  
		line( link( "javadoc" , "../javadoc/index.html" ), "API documentation" ), 
		"</table>\n"
	 )
	ooindex[ bottom ] <- txt
	
	# write back
	cat( ooindex, file = ooindex.file, sep = "\n" )
}
