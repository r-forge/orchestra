ant <- function( run = TRUE, arguments = paste( commandArgs(TRUE), collapse = " " ) ){
    rscript    <- file.path( R.home(), "bin", "Rscript" )
    ant.script <- system.file( 'exec', 'ant.R', package = 'ant' )
    cmd <- sprintf( '%s %s %s', rscript, ant.script, arguments)
    if( run ){
        system( cmd )
    } else{
    	cmd
    }
}

