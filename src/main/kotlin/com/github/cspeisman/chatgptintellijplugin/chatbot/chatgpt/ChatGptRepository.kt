import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

interface ChatGptRepository {
    fun getCompletionResponse(request: ChatCompletionRequest): ChatCompletionResponse
}
class ChatGptHttp(private val apiKey: String): ChatGptRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
    private val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
    private val gson = GsonBuilder().create()
    override fun getCompletionResponse(request: ChatCompletionRequest): ChatCompletionResponse {
        val json = gson.toJson(request)
        val body: RequestBody = json.toRequestBody(mediaType)

        val httpRequest: Request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(body)
            .build()

        // Block the thread and wait for OpenAI's json response
        val response = client.newCall(httpRequest).execute()
        val jsonResponse = response.body!!.string()
        val rootObject = JsonParser.parseString(jsonResponse).asJsonObject

        // Usually happens if you give improper arguments (either an unrecognized argument or bad argument value)
        if (rootObject.has("error"))
            throw IllegalArgumentException(rootObject["error"].asJsonObject["message"].asString)

         return ChatCompletionResponse(rootObject)
    }
}

class ChatGptFake: ChatGptRepository {
    data class ChatMessagePayload(val content: String, val role: String)
    data class ChatCompletionChoicePayload(val index: Int, val message: ChatMessagePayload, @SerializedName("finish_reason") val finishReason: String)
    data class ChatCompletionResponsePayload(val id: String, val created: String, val choices: List<ChatCompletionChoicePayload>)
    override fun getCompletionResponse(request: ChatCompletionRequest): ChatCompletionResponse {
        return ChatCompletionResponse("id-1234", 1, listOf(ChatCompletionChoice(
            1,
            ChatMessage("response for ${request.messages[1].content}"),
            "completed"
            )))
    }
}
