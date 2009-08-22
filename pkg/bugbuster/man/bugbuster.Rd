\name{getBrowseFrom}
\Rdversion{1.1}
\alias{getBrowseFrom}
\alias{setBrowseFrom}
\alias{cacheBrowseContext}
\alias{getBrowseContext}
\alias{getBrowseContextSize}
\alias{flushBrowseContext}
\alias{addBrowseStackChangeListener}
\title{
Debugging utilities
}
\description{
Set of debugging utilities. The functionality works by 
calling \code{cacheBrowseContext} from the browse prompt
which captures the current stack of calls and the current 
stack of frames. 

Other functions are used to query these cached objects.
}
\usage{
getBrowseFrom()
setBrowseFrom(from=0L)
cacheBrowseContext( from = getBrowseFrom() )
getBrowseContext()
getBrowseContextSize()
flushBrowseContext()
addBrowseStackChangeListener(listener)
}
\arguments{
	\item{from}{..}
	\item{listener}{..}
}
\author{
Romain Francois <francoisromain@free.fr>
}
\seealso{
	\code{\link{browser}}, \code{\link{debug}}, \code{\link{recover}}
}
\keyword{ programming }

