package com.github.cspeisman.chatgptintellijplugin.settings;

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

import com.intellij.util.xmlb.XmlSerializerUtil


@State(
    name = "com.github.cspeisman.chatgptintellijplugin.settings.AppSettingsState",
    storages = [Storage("ChatGPTSettingsPlugin.xml")]
)
class AppSettingsState : PersistentStateComponent<AppSettingsState> {
    var apiKey = ""
    var model = ""


    override fun getState(): AppSettingsState {
        return this
    }

    override fun loadState(state: AppSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: AppSettingsState
            get() = ApplicationManager.getApplication().getService(AppSettingsState::class.java)
    }
}
