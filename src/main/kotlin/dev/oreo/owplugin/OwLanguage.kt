package dev.oreo.owplugin

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.tree.IElementType
import javax.swing.Icon

object OwLanguage : Language("Ow")

object OwIcons {
    @JvmField
    val FILE: Icon = IconLoader.getIcon("/icons/ow.png", OwIcons::class.java)
}

object OwFileType : LanguageFileType(OwLanguage) {
    override fun getName(): String = "Ow file"
    override fun getDescription(): String = "Ow source file"
    override fun getDefaultExtension(): String = "ow"
    override fun getIcon(): Icon = OwIcons.FILE
}

class OwTokenType(debugName: String) : IElementType(debugName, OwLanguage)

object OwTokens {
    val KEYWORD = OwTokenType("OW_KEYWORD")
    val STRING = OwTokenType("OW_STRING")
    val NUMBER = OwTokenType("OW_NUMBER")
    val COMMENT = OwTokenType("OW_COMMENT")
    val IDENT = OwTokenType("OW_IDENT")
    val OTHER = OwTokenType("OW_OTHER")
}
