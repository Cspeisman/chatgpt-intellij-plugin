package com.github.cspeisman.chatgptintellijplugin.chatbot.actions

import ChatBot
import ChatCompletionRequest
import ChatGptHttp
import com.github.cspeisman.chatgptintellijplugin.settings.AppSettingsState
import com.github.cspeisman.chatgptintellijplugin.ui.ContentPanelComponent
import com.github.cspeisman.chatgptintellijplugin.ui.PromptFormatter
import com.intellij.openapi.application.ApplicationManager


class ChatBotActionService(private val actionType: ChatBotActionType) {
    val action = if (actionType == ChatBotActionType.EXPLAIN) "explain" else "refactor"

    fun getLabel(): String {
        val capitalizedAction = action.capitalize()
        return "$capitalizedAction Code"
    }


    private fun getCodeSection(content: String): String {
        val pattern = "```(.+?)```".toRegex(RegexOption.DOT_MATCHES_ALL)
        val match = pattern.find(content)

        if (match != null) return match.groupValues[1].trim()
        return ""
    }


    private fun makeChatBotRequest(prompt: String): String {
        val apiKey = AppSettingsState.instance.apiKey
        val model = AppSettingsState.instance.model.ifEmpty { "gpt-3.5-turbo" }

        if (apiKey.isEmpty()) {
            return "Please add an API Key in the ChatBot settings"
        }

        val chatbot = ChatBot(ChatGptHttp(apiKey))
        val system = "Be as helpful as possible and concise with your response"
        val request = ChatCompletionRequest(model, system)
        request.addMessage(prompt)
        val generateResponse = chatbot.generateResponse(request)
        return generateResponse.choices[0].message.content
    }

    fun handlePromptAndResponse(
        ui: ContentPanelComponent,
        prompt: PromptFormatter,
        replaceSelectedText: ((response: String) -> Unit)? = null
    ) {
        ui.add(prompt.getUIPrompt(), true)
        ui.add("Loading...")

        ApplicationManager.getApplication().executeOnPooledThread {
            val response = this.makeChatBotRequest(prompt.getRequestPrompt())
            ApplicationManager.getApplication().invokeLater {
                when {
                    actionType === ChatBotActionType.REFACTOR -> ui.updateReplaceableContent(response) {
                        replaceSelectedText?.invoke(getCodeSection(response))
                    }
                    else -> ui.updateMessage(response)
                }
            }
        }
    }
}

enum class ChatBotActionType {
    REFACTOR,
    EXPLAIN
}
