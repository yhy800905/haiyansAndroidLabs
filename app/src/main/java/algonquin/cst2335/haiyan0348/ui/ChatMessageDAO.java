package algonquin.cst2335.haiyan0348.ui;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


    @Dao
    public interface ChatMessageDAO {
        @Insert
        public long insertMessage(ChatMessage m);

        @Query("Select * from ChatMessage")
        public List<ChatMessage> getAllMessages();

        @Delete
        void deleteMessage(ChatMessage m);

        @Update
        public int anyUpdate(ChatMessage updatedMessage);
    }


