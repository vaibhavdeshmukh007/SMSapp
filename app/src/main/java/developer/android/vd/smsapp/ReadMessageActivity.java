package developer.android.vd.smsapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReadMessageActivity extends AppCompatActivity {

    EditText newMessage;
    ListView listOfMessages;
    String fromNumber;
    String fromName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);

        newMessage = (EditText) findViewById(R.id.writeNewSms);
        listOfMessages = (ListView) findViewById(R.id.listOfMessages);

        Intent intent = getIntent();
        fromNumber = intent.getStringExtra("from_number");
        fromName = intent.getStringExtra("from_name");

        ArrayAdapter<String> smsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, getAllSMSfrom(fromNumber));

        listOfMessages.setAdapter(smsAdapter);
        //to make sure we are able to view the latest sms i.e. last item of listView
        listOfMessages.setSelection(listOfMessages.getAdapter().getCount() - 1);
        try {
            getSupportActionBar().setTitle(fromName);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    public ArrayList<String> getAllSMSfrom(String number) {
        ArrayList<String> messageBodyArrayList = new ArrayList<>();
        // Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");
        // List required columns
        String[] reqCols = new String[]{"address", "body", "date"};
        Cursor cursor = getContentResolver().query(inboxURI, reqCols, null, null, "date" + " ASC");

        if (cursor.moveToFirst()) {
            // must check the result to prevent exception
            while (!cursor.isAfterLast()) {
                String fromNumber = cursor.getString(cursor.getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                //populate fromArrayList and bodyArrayList
                if (fromNumber.equals(number))
                    messageBodyArrayList.add(body);

                cursor.moveToNext();
            }
        } else {
            // empty box, no SMS
        }
        cursor.close();
        return messageBodyArrayList;
    }

    public void sendSMS(View view) {
        SmsManager smsManager = SmsManager.getDefault();

        String sms = newMessage.getText().toString();
        if (!sms.equals("")) {
            try {
                smsManager.sendTextMessage(fromNumber, null, sms, null, null);
                Toast.makeText(this, "Status of sent SMS can be checked in built-in Messaging app.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "SMS can not be sent to this number :(", Toast.LENGTH_SHORT).show();
            } finally {
                newMessage.setText("");
                finish();
            }
        }
    }
}
