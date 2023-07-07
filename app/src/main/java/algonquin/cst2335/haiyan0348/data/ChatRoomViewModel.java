package algonquin.cst2335.haiyan0348.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.haiyan0348.ui.ChatMessage;

public class ChatRoomViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatMessage>> chatMessages = new MutableLiveData<>();

    public void setChatMessages(ArrayList<ChatMessage> messages) {
        chatMessages.setValue(messages);
    }

    public MutableLiveData<ArrayList<ChatMessage>> getChatMessages() {
        return chatMessages;
    }
}