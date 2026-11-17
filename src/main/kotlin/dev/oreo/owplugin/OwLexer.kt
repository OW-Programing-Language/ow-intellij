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
                // Optional fractional part for float literals (e.g. 3.14).
                if (i + 1 < endOffset && buffer[i] == '.' && buffer[i + 1].isDigit()) {
                    i++
                    while (i < endOffset && buffer[i].isDigit()) i++
                }
                tokenEnd = i
                tokenType = OwTokens.NUMBER
            }
            c.isLetter() || c == '_' -> {
                var i = tokenStart
                while (i < endOffset && (buffer[i].isLetterOrDigit() || buffer[i] == '_')) i++
                tokenEnd = i
                val word = buffer.subSequence(tokenStart, i).toString()
                tokenType = when (word) {
                    in KEYWORDS -> OwTokens.KEYWORD
                    in BUILTINS -> OwTokens.BUILTIN
                    else -> OwTokens.IDENT
                }
            }
            c in OPERATOR_CHARS -> {
                var i = tokenStart
                while (i < endOffset && buffer[i] in OPERATOR_CHARS) i++
                tokenEnd = i
                tokenType = OwTokens.OPERATOR
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
            "route", "respond", "serve", "import", "true", "false",
            // error handling
            "try", "catch",
            // module system (flOW modules, Night services, functors)
            "module", "module_type", "functor", "functor_gen", "functor_type",
            "type", "with_type", "return_functor", "include"
        )

        // Built-in functions provided by the Ow runtime. Highlighted distinctly
        // from user identifiers. Kept in sync with the interpreter's builtin set.
        val BUILTINS = setOf(
            "input", "int", "str", "len", "array", "mod", "rand", "time_ms", "now_ms", "sleep",
            "json", "json_parse", "param", "query", "body", "body_json", "response",
            "upper", "lower", "trim", "substr", "index_of", "contains", "replace", "split", "char_at",
            "abs", "min", "max", "pow", "sqrt", "clamp", "floor", "round", "ceil",
            "save", "load", "exists", "pack", "unpack", "assert", "extension",
            // dictionaries / maps
            "map_get", "map_has", "map_set", "map_delete", "map_keys", "map_values", "map_size",
            // UI runtime
            "ui_text", "ui_heading", "ui_button", "ui_row", "ui_col", "ui_grid", "ui_disc",
            "ui_image", "ui_input", "ui_bar", "ui_space", "ui_card", "ui_backdrop",
            "ui_run", "ui_run_web", "ui_run_native", "ui_stop",
            // audio / mic
            "audio_play", "audio_music", "audio_stop", "mic_level", "mic_transcribe"
        )

        private val OPERATOR_CHARS = "+-*/<>=!".toSet()
    }
}
