package dev.oreo.owplugin

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType

class OwSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = OwLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> = when (tokenType) {
        OwTokens.KEYWORD -> KEYWORD
        OwTokens.BUILTIN -> BUILTIN
        OwTokens.STRING -> STRING
        OwTokens.NUMBER -> NUMBER
        OwTokens.COMMENT -> COMMENT
        OwTokens.OPERATOR -> OPERATOR
        else -> EMPTY
    }

    companion object {
        private fun key(name: String, fallback: TextAttributesKey) =
            arrayOf(TextAttributesKey.createTextAttributesKey(name, fallback))

        val KEYWORD = key("OW_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val BUILTIN = key("OW_BUILTIN", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL)
        val STRING = key("OW_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = key("OW_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val COMMENT = key("OW_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val OPERATOR = key("OW_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val EMPTY = emptyArray<TextAttributesKey>()
    }
}

class OwSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter =
        OwSyntaxHighlighter()
}
