package com.github.cspeisman.chatgptintellijplugin.chatbot.actions

import com.github.cspeisman.chatgptintellijplugin.ui.ActionPromptFormatter
import com.github.cspeisman.chatgptintellijplugin.ui.ContentPanelComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.wm.ToolWindowManager

abstract class ChatBotBaseAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val toolWindowManager = ToolWindowManager.getInstance(project!!).getToolWindow("ChatBot")
        val contentManager = toolWindowManager?.contentManager

        val caretModel = event.getData(CommonDataKeys.EDITOR)?.caretModel
        val selectedText = caretModel?.currentCaret?.selectedText ?: ""
        val lang = event.getData(CommonDataKeys.PSI_FILE)?.language?.displayName ?: ""

        val chatBotActionService = ChatBotActionService(getActionType())
        val contentPanel = ContentPanelComponent(chatBotActionService)
        val content = contentManager?.factory?.createContent(contentPanel, chatBotActionService.getLabel(), false)
        contentManager?.removeAllContents(true)
        contentManager?.addContent(content!!)
        toolWindowManager?.activate(null)

        chatBotActionService.handlePromptAndResponse(
            contentPanel,
            ActionPromptFormatter(chatBotActionService.action, lang, selectedText), getReplaceableAction(event)
        )

    }

    open fun getReplaceableAction(event: AnActionEvent): ((response: String) -> Unit)? {
        return null
    }

    abstract fun getActionType(): ChatBotActionType
}
