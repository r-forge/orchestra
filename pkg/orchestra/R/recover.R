.debugenv <- new.env()

#' slightly modified version of recover that works with the
#' environment browser
jedit.recover <- function() {
    if(.isMethodsDispatchOn()) {
        ## turn off tracing
        tState <- tracingState(FALSE)
        on.exit(tracingState(tState))
    }
    ## find an interesting environment to start from
    calls <- sys.calls()
    from <- 0L
    n <- length(calls)
    if(identical(sys.function(n), jedit.recover))
        ## options(error=jedit.recover) produces a call to this function as an object
        n <- n - 1L
    ## look for a call inserted by trace() (and don't show frames below)
    ## this level.
    for(i in rev(seq_len(n))) {
        calli <- calls[[i]]
        fname <- calli[[1L]]
        ## deparse can use more than one line
        if(!is.na(match(deparse(fname)[1L],
                        c("methods::.doTrace", ".doTrace")))) {
            from <- i-1L
            break
        }
    }
  ## if no trace, look for the first frame from the bottom that is not
    ## stop or jedit.recover
    if(from == 0L)
      for(i in rev(seq_len(n))) {
        calli <- calls[[i]]
        fname <- calli[[1L]]
        if(!is.name(fname) ||
           is.na(match(as.character(fname), c("jedit.recover", "stop", "Stop")))) {
            from <- i
            break
        }
    }
    if(from > 0L) {
        if(!interactive()) {
            try(dump.frames())
            cat(gettext("jedit.recover called non-interactively; frames dumped, use debugger() to view\n"))
            return(NULL)
        }
        else if(identical(getOption("show.error.messages"), FALSE)) # from try(silent=TRUE)?
            return(NULL)
        calls <- limitedLabels(calls[1L:from])
        .debugenv[["from"]] <- from
        repeat {
            which <- menu(calls,
                          title="\nEnter a frame number, or 0 to exit  ")
			  if(which){
                eval(quote(browser()), envir = sys.frame(which))
              } else{ 
              	  .debugenv[["from"]] <- 0
                break
              }
        }
    }
    else
        cat(gettext("No suitable frames for jedit.recover()\n"))
}

