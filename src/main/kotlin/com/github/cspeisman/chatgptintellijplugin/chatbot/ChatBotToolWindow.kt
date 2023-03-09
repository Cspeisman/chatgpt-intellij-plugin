package com.github.cspeisman.chatgptintellijplugin.chatbot

import com.github.cspeisman.chatgptintellijplugin.chatbot.actions.ChatBotActionService
import com.github.cspeisman.chatgptintellijplugin.chatbot.actions.ChatBotActionType
import com.github.cspeisman.chatgptintellijplugin.ui.ContentPanelComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory


class ChatBotToolWindow : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val chatBotActionService = ChatBotActionService(ChatBotActionType.EXPLAIN)
        val contentPanel = ContentPanelComponent(chatBotActionService)
        val createContent = contentFactory?.createContent(contentPanel, "ChatBot", false)
        toolWindow.contentManager.addContent(createContent!!)
    }
}
