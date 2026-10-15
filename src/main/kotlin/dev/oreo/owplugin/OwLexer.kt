package dev.oreo.owplugin

import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

/**
 * A small hand-written lexer, just enough for syntax highlighting. It splits the
 * buffer into comments, strings, numbers, keywords, identifiers and whitespace.
 */
class OwLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var endOffset = 0
    private var tokenStart = 0
    private var tokenEnd = 0
    private var tokenType: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.endOffset = endOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        advance()
    }

    override fun getState(): Int = 0
    override fun getTokenType(): IElementType? = tokenType
    override fun getTokenStart(): Int = tokenStart
    override fun getTokenEnd(): Int = tokenEnd
    override fun getBufferSequence(): CharSequence = buffer
    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        tokenStart = tokenEnd
        if (tokenStart >= endOffset) {
            tokenType = null
            return
        }
        val c = buffer[tokenStart]
        when {
            c == '#' -> {
                var i = tokenStart
                while (i < endOffset && buffer[i] != '\n') i++
                tokenEnd = i
                tokenType = OwTokens.COMMENT
            }
            c == '"' -> {
                var i = tokenStart + 1
                while (i < endOffset && buffer[i] != '"' && buffer[i] != '\n') {
                    if (buffer[i] == '\\' && i + 1 < endOffset) i++
                    i++
                }
                if (i < endOffset && buffer[i] == '"') i++
                tokenEnd = i
                tokenType = OwTokens.STRING
            }
            c.isWhitespace() -> {
                var i = tokenStart
                while (i < endOffset && buffer[i].isWhitespace()) i++
                tokenEnd = i
                tokenType = TokenType.WHITE_SPACE
            }
            c.isDigit() -> {
                var i = tokenStart
                while (i < endOffset && buffer[i].isDigit()) i++
                tokenEnd = i
                tokenType = OwTokens.NUMBER
            }
            c.isLetter() || c == '_' -> {
                var i = tokenStart
                while (i < endOffset && (buffer[i].isLetterOrDigit() || buffer[i] == '_')) i++
                tokenEnd = i
                val word = buffer.subSequence(tokenStart, i).toString()
                tokenType = if (word in KEYWORDS) OwTokens.KEYWORD else OwTokens.IDENT
            }
            else -> {
                tokenEnd = tokenStart + 1
                tokenType = OwTokens.OTHER
            }
        }
    }

    companion object {
        val KEYWORDS = setOf(
            "main", "set", "print", "if", "else", "while", "fn", "return", "call",
            "switch", "case", "default", "enum", "interface", "class", "field", "model",
            "new", "this", "private", "public", "save", "find", "delete", "where",
            "route", "respond", "serve", "true", "false"
        )
    }
}
