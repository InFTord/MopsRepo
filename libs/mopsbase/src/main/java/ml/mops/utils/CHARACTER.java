package ml.mops.utils;

import javax.annotation.CheckForNull;

public enum CHARACTER {
	DOT,                // . ТОЧКА
	PERIOD,             // . ТОЧКА
	COMMA,              // , ЗАПЯТАЯ
	COLON,              // : ДВОЕТОЧИЕ
	COLON_WITH_DOT,     // ; ДВОЕТОЧИЕ С ЗАПЯТОЙ
	SEMICOLON,          // ; ДВОЕТОЧИЕ С ЗАПЯТОЙ
	VERTICAL_LINE,      // | ЛИНИЯ
	DASH,               // - ТИРЕ
	TILDE,              // ~ ТИЛ(ть)ДА
	SPACE,              //   ПРОБЕЛ
	SPACEBAR,           //   ПРОБЕЛ
	DEFAULT;            //   СТАНДАРТНЫЙ (пробел)

	public char getCharacter() {
		char chr;
		switch (this) {
			case DOT, PERIOD -> chr = '.';
			case COMMA -> chr = ',';
			case COLON -> chr = ':';
			case COLON_WITH_DOT, SEMICOLON -> chr = ';';
			case VERTICAL_LINE -> chr = '|';
			case DASH -> chr = '-';
			case TILDE -> chr = '~';
			case SPACE, SPACEBAR, DEFAULT -> chr = ' ';
			default -> chr = ' ';
		}
		return chr;
	}

	public String getString() {
		final char[] chr = {getCharacter()};
		return new String(chr);
	}
}
