package com.addictedtor.jedit.matcher;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.TextUtilities;
import org.gjt.sp.jedit.buffer.JEditBuffer;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.StructureMatcher;
import org.gjt.sp.jedit.textarea.TextArea;

import com.addictedtor.jedit.tools.PrivateAccessor;

import sidekick.SideKickActions;

/**
 * R specific structure matcher. 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 */
public class RStructureMatcher implements StructureMatcher {

	private static final StructureMatcher.BracketMatcher matcher = new StructureMatcher.BracketMatcher();
	private static final Pattern pattern = Pattern
			.compile("^.*?([a-zA-Z_.0-9]+ *)$");
	private static final Pattern closingBracketPattern = Pattern
			.compile("(\\))( *?)$");
	private static final Pattern aloneOpeningCurly = Pattern.compile("^ *\\{");

	public StructureMatcher.Match getMatch(TextArea textArea) {
		JEditBuffer buffer = textArea.getBuffer();
		if( buffer.isLoading() ){
			return null; 
		}
		if (!buffer.getMode().getName().equals("R"))
			return null;
		if (textArea.getCaretPosition() == 0)
			return null;
		StructureMatcher.Match match = matcher.getMatch(textArea);
		if( match == null){
			return null; 
		}
		String previous = textArea.getText(textArea.getCaretPosition() - 1, 1);
		if (previous.equals(")")) {
			return getMatchForClosingRoundBracket(textArea, match);
		} else if (previous.equals("}")) {
			int startOffset = textArea.getLineStartOffset(textArea
					.getLineOfOffset(match.start));
			String matchingLineText = textArea.getText(startOffset, match.start
					+ 1 - startOffset);
			if (aloneOpeningCurly.matcher(matchingLineText).find()) {
				return getMatchForAloneClosingCurlyBracket(textArea, match);
			} else {
				return getMatchForClosingCurlyBracket(textArea, match);
			}
		} else if (previous.equals("]")) {
			return getMatchForClosingSquareBracket(textArea, match);
		} else {
			return match;
		}

	}

	private StructureMatcher.Match getMatchForClosingSquareBracket(
			TextArea textArea, StructureMatcher.Match match) {

		// get the text on the line where the start of the match is
		int line = textArea.getLineOfOffset(match.start);
		int lineStartOffset = textArea.getLineStartOffset(line);
		String text = textArea.getText(lineStartOffset, match.start
				- lineStartOffset + 1);

		Pattern pattern = Pattern.compile("[a-zA-Z.0-9]+ *(\\[*)? *\\z");
		Matcher m = pattern.matcher(text);
		if (m.find()) {
			int endLine = textArea.getLineOfOffset(match.end);
			match = new StructureMatcher.Match(this, line, match.start
					- m.group(0).length(), endLine, match.end);
		}
		return match;
	}

	private StructureMatcher.Match getMatchForAloneClosingCurlyBracket(
			TextArea textArea, StructureMatcher.Match match) {
		// first find out if there is an closing round bracket just before
		Pattern pattern = Pattern.compile("\\)[^)]*\\z", Pattern.MULTILINE
				| Pattern.DOTALL);

		int startMatchLine = textArea.getLineOfOffset(match.start);
		if (startMatchLine == 0) {
			return match;
		}

		// getting all the text up to the line before the match
		String text = textArea.getText(0, textArea
				.getLineEndOffset(startMatchLine - 1) - 1);

		Matcher m = pattern.matcher(text);
		if (!m.find()) {
			return match;
		}
		// extract where the match is
		int closingBracketOffset = m.start();
		int closingBracketLine = textArea.getLineOfOffset(closingBracketOffset);

		// now we know where the closing round bracket is
		// we need the corresponding opening one
		int openingBracketOffset = TextUtilities.findMatchingBracket(textArea
				.getBuffer(), closingBracketLine, closingBracketOffset
				- textArea.getLineStartOffset(textArea
						.getLineOfOffset(closingBracketOffset)));
		if (openingBracketOffset == -1) {
			return match;
		}

		// now we need to extract the word that is right before the bracket
		text = textArea.getText(0, openingBracketOffset);
		Pattern word = Pattern.compile("([a-zA-Z_.0-9]+)\\s*\\z",
				Pattern.DOTALL | Pattern.MULTILINE);
		m = word.matcher(text);
		if (!m.find()) {
			return match;
		}
		String token = m.group(1);
		if (!acceptToken(token)) {
			return match;
		}
		int startOffset = m.start();
		int startLine = textArea.getLineOfOffset(startOffset);
		int endLine = textArea.getLineOfOffset(match.end);

		match = new StructureMatcher.Match(this, startLine, startOffset,
				endLine, match.end);

		return match;
	}

	private boolean acceptToken(String token) {
		if (token.equals("for"))
			return true;
		if (token.equals("while"))
			return true;
		if (token.equals("if"))
			return true;
		if (token.equals("repeat"))
			return true;
		if (token.equals("function"))
			return true;
		return false;
	}

	private StructureMatcher.Match getMatchForClosingRoundBracket(
			TextArea textArea, StructureMatcher.Match match) {
		// try to extract the name of the function being called, assuming it is
		// on the same line as the bracket
		int end = match.start;
		int line = textArea.getLineOfOffset(end);
		int start = textArea.getLineStartOffset(line);
		String text = textArea.getText(start, end - start);

		// trim the whitespaces at the end of text
		Matcher m = pattern.matcher(text);
		if (m.find()) {
			match = new StructureMatcher.Match(this, line, match.start
					- m.group(1).length(), line, match.end);
		}
		return match;
	}

	private StructureMatcher.Match getMatchForClosingCurlyBracket(
			TextArea textArea, StructureMatcher.Match match) {

		int end = match.start;
		int line = textArea.getLineOfOffset(end);
		int start = textArea.getLineStartOffset(line);
		String text = textArea.getText(start, end - start);

		// ( see if there is a closing ) right before the { on the same line //
		// }
		Matcher m = closingBracketPattern.matcher(text);
		if (m.find()) {
			int offset = end - m.group(2).length() - 1;
			// now find where the opening ( for this ) is
			int bracketOffset = TextUtilities.findMatchingBracket(textArea
					.getBuffer(), line, offset
					- textArea.getLineStartOffset(line));
			if (bracketOffset != -1) {
				int line_ = textArea.getLineOfOffset(bracketOffset);
				String text_ = textArea.getText(textArea
						.getLineStartOffset(line_), bracketOffset
						- textArea.getLineStartOffset(line_));
				Matcher m_ = pattern.matcher(text_);
				if (m_.find()) {
					match = new StructureMatcher.Match(this, line_,
							bracketOffset - m_.group(1).length(), line, end + 1);
				}
			}
		}
		return match;
	}
	
	@Override
	public void selectMatch(TextArea textArea) {
		SideKickActions.selectAsset(GUIUtilities.getView(textArea));
	}

	/**
	 * installs this matcher as the first matcher of the list. Access to the private 
	 * field "structureMatchers" is achieved by reflection trick 
	 * 
	 * @param textArea the text area where to install this matcher
	 */
	@SuppressWarnings("unchecked")
	public void install(JEditTextArea textArea) {
		List<StructureMatcher> sms = (List<StructureMatcher>) PrivateAccessor
				.getPrivateField(textArea, "structureMatchers",
						TextArea.class);
		StructureMatcher bracketMatcher = sms.get(0);
		textArea.removeStructureMatcher(bracketMatcher);
		textArea.addStructureMatcher(this);
		textArea.addStructureMatcher(bracketMatcher);
	}

	

}
