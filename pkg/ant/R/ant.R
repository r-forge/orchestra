# Copyright (c) 2009, Romain Francois <francoisromain@free.fr>
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

ant <- function( run = TRUE, arguments = commandArgs(TRUE) ){
    rscript( "ant", "ant.R", run = run, arguments = arguments )
}

rscript <- function( package, script, arguments = commandArgs(TRUE), run = TRUE, dir = "exec" ){
	
	Rscript <- file.path( R.home(), "bin", "Rscript" )
	script  <- system.file( dir, script, package = package )
	if( !file.exists( script ) ){
		stop( sprintf( "script '%s' does not exist", script) )
	}
	
	arguments <- if( length( arguments ) == 0 ) "" else paste( arguments, collapse = " " )
	cmd <- sprintf( '"%s" "%s" %s', Rscript, script, arguments )
	
	if( run ){
		system( cmd )
	} else {
		cmd
	}
}


ant.task.error.handler <- function( e ){
	self <- get( "self", .GlobalEnv )
	self$fails()
}
