package org.lm.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.lm.LmLanguage
import org.lm.lexer.LmLexerAdapter
import org.lm.psi.LM_COMMENTS
import org.lm.psi.LM_WHITE_SPACES
import org.lm.psi.LmElementTypes
import org.lm.psi.LmFile

class LmParserDefinition : ParserDefinition {

    override fun createLexer(project: Project): Lexer = LmLexerAdapter()

    override fun getWhitespaceTokens(): TokenSet = LM_WHITE_SPACES

    override fun getCommentTokens(): TokenSet = LM_COMMENTS

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun createParser(project: Project): PsiParser = LmParser()

    override fun getFileNodeType(): IFileElementType = IFileElementType(LmLanguage)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = LmFile(viewProvider)

    override fun spaceExistanceTypeBetweenTokens(
            left: ASTNode,
            right: ASTNode
    ): ParserDefinition.SpaceRequirements = ParserDefinition.SpaceRequirements.MAY

    override fun createElement(node: ASTNode): PsiElement =
            LmElementTypes.Factory.createElement(node)
}
