/*
 * Copyright (c) 2009, Romain Francois <francoisromain@free.fr>
 *
 * This file is part of the biocep editor plugin.
 *
 * The biocep editor plugin is free software: 
 * you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The biocep editor plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the biocep editor plugin. If not, see <http://www.gnu.org/licenses/>.
 */

package com.addictedtor.jedit.sidekick;

import java.awt.Color;
import java.util.Vector;
import java.util.regex.Pattern;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineException;

import com.addictedtor.jedit.R.RPlugin;
import com.addictedtor.jedit.tools.StringFilter;

/**
 * Color Completion
 * 
 * @author Romain Francois <francoisromain@free.fr>
 * 
 */
public class ColCompletion extends Completion {

	private String value;

	/**
	 * Color object
	 */
	private ColorObject color;

	/**
	 * Token this is completing
	 */
	private String token;

	/**
	 * Array of color objects for all named colors available in R
	 */
	private static final ColorObject[] namedColors = safeInitColors( "colors()") ; 

	/**
	 * Regular expression that indicates that the caret is inside the string so
	 * that we should indeed complete
	 */
	private static final Pattern oneQuotePattern = Pattern
	.compile("^.*['\"].*$");

	/**
	 * Regular expression that indicates that the caret is outside the string
	 * and that no completion is necessary
	 */
	private static final Pattern twoQuotePattern = Pattern
	.compile("^.*(['\"]).*\\1.*$");

	/**
	 * Constructor
	 * 
	 * @param red
	 *            red channel
	 * @param green
	 *            green channel
	 * @param blue
	 *            blue channel
	 * @param value
	 *            value of the match
	 * @param token
	 *            token this is completing
	 */
	public ColCompletion(int red, int green, int blue, String value,
			String token) {
		this.value = value;
		color = new ColorObject(new Color(red, green, blue));
		this.token = token;
	}

	private static ColorObject[] safeInitColors(String col) {
		ColorObject[] out = null ;
		try{
			out = initColors( col ) ;
		} catch( REXPMismatchException e){
			return null;
		}
		return out; 
	}

	/**
	 * Returns an array of color objects computed by the R expression col
	 * (typically col is "palette()" or "colors()"
	 * 
	 * @param col
	 *            R expression that should return a vector of colors
	 * @return Array of ColorObject, one object per result of the R expression
	 */
	private static ColorObject[] initColors(String col) throws REXPMismatchException {

		REngine r = RPlugin.getR() ;
		int lock_id = r.lock();

		String[] names = null;
		int[] rgb  =null ; 
		try{
			// FIXME: this evaluates col twice !!
			names = r.parseAndEval( col ).asStrings() ;
			rgb   = r.parseAndEval( "col2rgb( "+ col + ")" ).asIntegers() ;
		} catch( REngineException e2){

		} finally{
			r.unlock( lock_id); 
		}
		if( rgb == null){
			return null ;
		}
		int n = rgb.length / 3;
		ColorObject[] cols = new ColorObject[n];
		int k;
		for (int i = 0; i < n; i++) {
			k = 3 * i;
			cols[i] = new ColorObject(names[i], rgb[k], rgb[k + 1], rgb[k + 2]);
		}
		return cols;
	}

	/**
	 * Returns the value of the match
	 * 
	 * @return the value of the match
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Description of the color
	 * 
	 * @return description of the color
	 */
	public String getDescription() {
		return getValue();
	}

	@Override
	public String toString() {
		return getValue();
	}

	/**
	 * Returns the token
	 * 
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * The Color object associated with this completion
	 * 
	 * @return the color object
	 */
	public ColorObject getColor() {
		return (color);
	}

	/**
	 * Returns an array of color completions based on the line of code
	 * 
	 * @param line
	 *            a line of R code
	 * @return Array of possible completions for the R code
	 */
	public static ColCompletion[] complete(String line) {
		String token = line.replaceFirst("^.*= *", "");
		if (twoQuotePattern.matcher(token).matches()) {
			return null;
		}
		ColCompletion[] comps;
		StringFilter filter;
		String endofline;
		if (oneQuotePattern.matcher(token).matches()) {
			String name;
			endofline = token.replaceAll("['\"]", "");
			filter = new StringFilter(endofline);

			Vector<ColCompletion> vec = new Vector<ColCompletion>();
			for (int i = 0; i < namedColors.length; i++) {
				name = (namedColors[i]).getName();
				if (filter.accept(name)) {
					Color col = namedColors[i].getColor();
					vec.add(new ColCompletion(col.getRed(), col.getGreen(), col
							.getBlue(), "'" + name + "'", token));
				}
			}
			if (vec.size() == 0)
				return null;
			comps = new ColCompletion[vec.size()];
			vec.toArray(comps);
			return comps;
		}

		// call R to get palette colors
		ColorObject[] cols ;
		try{
			cols = initColors("palette()");
		} catch(REXPMismatchException e){
			return null; 
		}
		Color col;
		comps = new ColCompletion[cols.length];
		for (int i = 0; i < cols.length; i++) {
			col = cols[i].getColor();
			comps[i] = new ColCompletion(col.getRed(), col.getGreen(), col
					.getBlue(), "" + (i + 1), token);
		}
		return comps;

	}

}
