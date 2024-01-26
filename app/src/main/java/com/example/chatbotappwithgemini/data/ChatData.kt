package com.example.chatbotappwithgemini.data

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatData {
    val api_key = "AIzaSyDZja-bfbugY4_DfzbxeenMAjPL9lsXeTk"


    suspend fun  getResponseText(prompt : String) : Chat{
        val generativeModel = GenerativeModel(
            // For text-and-images input (multimodal), use the gemini-pro-vision model
            modelName = "gemini-pro",
            apiKey = api_key
        )

        try{
            val response = withContext(Dispatchers.IO){
                generativeModel.generateContent(prompt)
            }

            return Chat(
                prompt = response.text ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }
        catch(e: Exception){
            return Chat(
                prompt = e.message ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }
    }


    suspend fun  getResponseImagesAndText(prompt : String,bitmap : Bitmap) : Chat{
        val generativeModel = GenerativeModel(
            // For text-and-images input (multimodal), use the gemini-pro-vision model
            modelName = "gemini-pro-vision",

            apiKey = api_key
        )

        try{
            val inputContent = content{
                image(bitmap)
                text(prompt)
            }
            val response = withContext(Dispatchers.IO){
                generativeModel.generateContent(inputContent)
            }

            return Chat(
                prompt = response.text ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }
        catch(e: Exception){
            return Chat(
                prompt = e.message ?: "error",
                bitmap = null,
                isFromUser = false
            )
        }
    }

}
