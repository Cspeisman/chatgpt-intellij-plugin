package com.github.cspeisman.chatgptintellijplugin.ui

import com.intellij.ui.JBColor
import com.intellij.util.ui.JBEmptyBorder
import com.intellij.util.ui.UIUtil
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import javax.swing.JEditorPane
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledEditorKit

fun parseMarkdown(markdown: String): String {
    val parser = Parser.builder().build()
    val document: Node = parser.parse(markdown)
    val htmlRenderer = HtmlRenderer.builder().build()
    return htmlRenderer.render(document)
}

class MessageComponent(question: String, isPrompt: Boolean = false) : JEditorPane() {
    init {
        this.contentType = "text/html;charset=UTF-8"
        this.putClientProperty(HONOR_DISPLAY_PROPERTIES, true)
        this.font = UIUtil.getMenuFont()
        this.isEditable = false
        this.background = if (isPrompt) JBColor(0xEAEEF7, 0x45494A) else JBColor(0xE0EEF7, 0x2d2f30)
        this.border = JBEmptyBorder(10)
        this.text = if (isPrompt) question else parseMarkdown(question)
    }
}
