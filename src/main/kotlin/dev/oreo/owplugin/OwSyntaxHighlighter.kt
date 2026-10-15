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
        OwTokens.STRING -> STRING
        OwTokens.NUMBER -> NUMBER
        OwTokens.COMMENT -> COMMENT
        else -> EMPTY
    }

    companion object {
        private fun key(name: String, fallback: TextAttributesKey) =
            arrayOf(TextAttributesKey.createTextAttributesKey(name, fallback))

        val KEYWORD = key("OW_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val STRING = key("OW_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = key("OW_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val COMMENT = key("OW_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val EMPTY = emptyArray<TextAttributesKey>()
    }
}

class OwSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter =
        OwSyntaxHighlighter()
}
