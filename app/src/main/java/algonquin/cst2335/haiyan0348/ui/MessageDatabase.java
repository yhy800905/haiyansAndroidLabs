package algonquin.cst2335.haiyan0348.ui;

import androidx.room.Database;
import androidx.room.RoomDatabase;


    @Database(entities = {ChatMessage.class}, version=1)
    public abstract class MessageDatabase extends RoomDatabase {

        public abstract ChatMessageDAO cmDAO();//only 1 function for how to interact

    }

