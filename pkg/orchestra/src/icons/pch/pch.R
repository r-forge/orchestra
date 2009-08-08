#!/bin/env Rscript

for( pch in 1:25 ){
	png( sprintf("pch_%d.png", pch), width=16, height = 16 )
	par( mar = rep(0,4) )
	plot( 0, 0, type = "n", ann = FALSE, axes = FALSE )
	points( 0, 0, cex = 2, pch = pch, col="black", bg = "orange" )
	dev.off()
}

