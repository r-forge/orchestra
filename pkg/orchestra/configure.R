#!/bin/env Rscript

# grab the path where rJava is installed
rjava.path <- system.file( package = "rJava" )
if( identical( rjava.path, "" ) ){
	stop( "could not find the rJava package" )
}
build.properties <- c( 
	readLines( "inst/javasrc/build.properties.in" ), 
	sprintf( "rjava.path=%s", rjava.path ) )
cat( build.properties , file = "inst/javasrc/build.properties", sep = "\n" )

# check that JRI is available
if( !file.exists( file.path( rjava.path, "jri", "JRI.jar" ) )  ){
	stop( "JRI.jar not available, please reinstall rJava" )
}


# call ant
setwd( "inst/javasrc" )
ant::ant()
setwd( "../.." )
q( 0, save = "no" )

