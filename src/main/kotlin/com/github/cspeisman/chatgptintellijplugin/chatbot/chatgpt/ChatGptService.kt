import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName



data class ChatMessage(val content: String, val role: String = "user") {
    constructor(json: JsonObject) : this(
        json["content"].asString ?: "Sorry, no answer found",
        json["role"].asString ?: "system"
    )
}

data class ChatCompletionChoice(
    val index: Int,
    val message: ChatMessage,
    val finishReason: String
) {

    constructor(json: JsonObject) : this(
        json["index"].asInt,
        ChatMessage(json["message"].asJsonObject),
        if (json["finish_reason"].isJsonNull) "" else json["finish_reason"].asString
    )
}

data class ChatCompletionResponse(
    val id: String,
    val created: Long,
    val choices: List<ChatCompletionChoice>,
) {
    constructor(json: JsonObject) : this(
        json["id"].asString,
        json["created"].asLong,
        json["choices"].asJsonArray.map { ChatCompletionChoice(it.asJsonObject) },
    )
}

data class ChatCompletionRequest(
    val model: String, // recommend: "gpt-3.5-turbo"
    val messages: MutableList<ChatMessage>,
    private val temperature: Float = 0.0f,
    @SerializedName("top_p") val topP: Float = 0.0f,
    val n: Int = 1,
    val stream: Boolean = false,
    val stop: String? = null,
    @SerializedName("max_tokens") val maxTokens: Int? = null, // default is 4096
    @SerializedName("presence_penalty") val presencePenalty: Float = 0.0f,
    @SerializedName("frequency_penalty") val frequencyPenalty: Float = 0.0f,
    @SerializedName("logit_bias") val logitBias: JsonObject? = null,
    val user: String? = null
) {
    constructor(model: String, systemContent: String) : this(
        model,
        arrayListOf(ChatMessage(systemContent, "system"))
    )

    fun addMessage(prompt: String) {
        this.messages.add(ChatMessage(prompt))
    }
}

open class ChatBot(private val chatGptRepository: ChatGptRepository) {

    fun generateResponse(request: ChatCompletionRequest): ChatCompletionResponse {
        return chatGptRepository.getCompletionResponse(request)
    }
}
