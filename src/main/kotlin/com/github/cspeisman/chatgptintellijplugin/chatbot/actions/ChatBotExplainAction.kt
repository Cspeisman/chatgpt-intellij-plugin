package com.github.cspeisman.chatgptintellijplugin.chatbot.actions

import com.intellij.openapi.actionSystem.AnActionEvent


class ChatBotExplainAction : ChatBotBaseAction() {
    override fun actionPerformed(event: AnActionEvent) {
        super.actionPerformed(event)
    }

    override fun getActionType(): ChatBotActionType {
        return ChatBotActionType.EXPLAIN
    }
}
