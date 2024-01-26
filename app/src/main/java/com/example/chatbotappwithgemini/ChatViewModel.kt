package com.example.chatbotappwithgemini

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbotappwithgemini.data.Chat
import com.example.chatbotappwithgemini.data.ChatData
import com.example.chatbotappwithgemini.data.ChatState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {
    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()
    fun onEvent(event : ChatUiEvent){
        when(event){
            is ChatUiEvent.SendPrompt -> {

                if(event.prompt.isNotEmpty()){
                    addPrompt(event.prompt,event.bitmap)
                }

                if(event.bitmap!= null){
                    getResponseWithImage(event.prompt,event.bitmap)
                }
                else{
                  getResponse(event.prompt)
                }

            }
            is ChatUiEvent.UpdatePrompt -> {
                _chatState.update {
                    it.copy(prompt = event.newPrompt)
                }

            }
        }

    }


    //Update the state of the textfield and chat
    private fun addPrompt(prompt : String, bitmap : Bitmap?) {
        _chatState.update{
            it.copy(
                chatList = it.chatList.toMutableList().apply{
                    add(0, Chat(prompt,bitmap,true))
                },
                prompt = "",
                bitmap = null
            )
        }
    }


    //getting the response

    //to get the response via text
    private fun getResponse(prompt : String){
        viewModelScope.launch {
            val chat = ChatData.getResponseText(prompt)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply{
                        add(0, chat)
                    },
                )
            }
        }

    }

    private fun getResponseWithImage(prompt : String,bitmap : Bitmap){
        viewModelScope.launch {
            val chat = ChatData.getResponseImagesAndText(prompt,bitmap)
            _chatState.update {
                it.copy(
                    chatList = it.chatList.toMutableList().apply{
                        add(0, chat)
                    }
                )
            }
        }

    }




}