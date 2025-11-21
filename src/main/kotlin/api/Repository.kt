package org.example.api

import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URI
import java.util.concurrent.Executors

object Repository {
    private const val BASE_URL = "https://api.api-ninjas.com/v1/dictionary?word="
    private const val API_KEY = "Q3Oenu66nPB1kH9vLdFcZg==tFrfpwHXNqODOxxv"
    private const val HEADER_KEY = "X-Api-Key"

    private val json = Json{ignoreUnknownKeys = true}

    suspend fun loadDefinition(word:String):List<String>{
       return withContext(Dispatchers.IO){
            var connection:HttpURLConnection?=null
            try {
                val urlString = BASE_URL + word
                val url = URI(urlString)
                connection = (url.toURL().openConnection() as HttpURLConnection).apply {
                    addRequestProperty(HEADER_KEY, API_KEY)
                }
                val response = connection.inputStream.bufferedReader().readText()
                json.decodeFromString<Definition>(response).mapDefinitionToList()
            }catch (e:Exception){
                println(e)
                listOf()
            }finally {
                connection?.disconnect()
            }
        }
    }
    private fun Definition.mapDefinitionToList():List<String>{
        return definition.split(Regex("\\d. ")).map { it.trim() }.filter { it.isNotEmpty() }
    }
}
