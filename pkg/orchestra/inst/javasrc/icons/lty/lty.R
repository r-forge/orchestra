#!/bin/env Rscript

for( lty in c("solid", "dashed", "blank", "dotted", "dotdash", "twodash", "longdash" ) ){
	png( sprintf("lty_%s.png", lty), width=32, height = 16, pointsize = 4 )
	par( mar = rep(0,4) )
	plot( 0, type = "n", ann = FALSE, axes = FALSE )
	abline( h = 0, lty = lty, lwd = 2, col = "black", lend = "square" )
	dev.off()
}

