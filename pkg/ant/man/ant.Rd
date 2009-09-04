\name{ant}
\Rdversion{1.1}
\alias{ant}
\title{
Apache Ant build tool
}
\description{
Apache Ant build tool
}
\usage{
ant(run = TRUE, arguments = commandArgs(TRUE) )
}
\arguments{
  \item{run}{
Should the generated command be run.
}
  \item{arguments}{
Additional arguments for the ant script
}
}
\details{
This function makes a call to the R script ant.R that 
is in the exec directory of this package
}
\value{
If run is TRUE, this will return the result of the system call, 
otherwise the command is returned.
}
\references{
The Apache ant project: \url{http://ant.apache.org/}
}
\examples{
	\dontrun{
		build.file <- system.file( "examples", "build.xml", package = "ant" )
		owd <- setwd( dirname( build.file ) )
		ant()
		setwd( owd )
	}
}
\author{
Romain Francois <francoisromain@free.fr>
}
\keyword{ manip }

