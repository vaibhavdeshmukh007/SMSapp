package developer.android.vd.smsapp;

/**
 * Created by VD on 5/15/2016.
 */
public class SMSMessage {
    private String fromNumber;
    private int numberOfMessages;
    private String messageBody;

    public String getFromNumber() {
        return fromNumber;
    }
    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public int getNumberOfMessages() {
        return numberOfMessages;
    }
    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public String getMessageBody() {
        return messageBody;
    }
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
