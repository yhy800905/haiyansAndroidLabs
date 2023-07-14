package algonquin.cst2335.haiyan0348.ui;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey(autoGenerate = true) //increment the ids for us
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo(name="Message")
    protected String message;

    @ColumnInfo(name="SendOrReceive")
    protected int sendOrReceive;

    @ColumnInfo(name="TimeSent")
    protected String timeSent;

    public ChatMessage() {
        // Empty constructor required by Room
    }

    public ChatMessage(String m, String t, int type)
    {
        this.message = m;
        this.timeSent = t;
        this.sendOrReceive = type;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public int getSendOrReceive() {
        return sendOrReceive;
    }
}
