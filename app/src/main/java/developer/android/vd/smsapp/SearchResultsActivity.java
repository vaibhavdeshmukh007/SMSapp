package developer.android.vd.smsapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {
    ListView searchListView;
    ArrayList<SMSMessage> smsMessageArrayList = new ArrayList<>();
    MessageAdapter smsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        searchListView = (ListView) findViewById(R.id.listOfSearchResults);

        Intent intent = getIntent();
        String searchCriteria = intent.getStringExtra("searched_text");

        showSearchedResults(searchCriteria);

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                String fromNumber = smsAdapter.getItem(position).getFromNumber();
                String fromName = ((TextView) view.findViewById(R.id.lblNumber)).getText().toString();

                Intent readMessagesIntent = new Intent(SearchResultsActivity.this, ReadMessageActivity.class);
                readMessagesIntent.putExtra("from_number", fromNumber);
                readMessagesIntent.putExtra("from_name", fromName);
                startActivity(readMessagesIntent);
            }
        });
    }

    public void showSearchedResults(String sequence) {
        int count = 0;
        // Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");
        // List required columns
        String[] reqCols = new String[]{"_id", "address", "body"};
        Cursor cursor = getContentResolver().query(inboxURI, reqCols, null, null, null);

        // must check the result to prevent exception
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                String fromNumber = cursor.getString(cursor.getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));

                if (body.contains(sequence)) {
                    count++;
                    SMSMessage smsMessage = new SMSMessage();

                    smsMessage.setFromNumber(fromNumber);
                    smsMessage.setNumberOfMessages(1);
                    smsMessage.setMessageBody(body);

                    smsMessageArrayList.add(smsMessage);
                }

                cursor.moveToNext();
            }
        } else {
            // empty box, no SMS
        }
        cursor.close();

        try {
            String result_text = "results";
            if (count < 2)
                result_text = "result";
            getSupportActionBar().setTitle(count + " " + result_text + " for \"" + sequence + "\"");
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        } finally {
            smsAdapter = new MessageAdapter(this, R.layout.row, smsMessageArrayList);
            searchListView.setAdapter(smsAdapter);
        }
    }
}
