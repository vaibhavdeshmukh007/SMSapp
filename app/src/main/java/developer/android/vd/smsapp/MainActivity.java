package developer.android.vd.smsapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by VD on 5/14/2016.
 */
public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<SMSMessage> smsMessageArrayList = new ArrayList<>();
    ArrayList<String> fromArrayList = new ArrayList<>();
    ArrayList<String> bodyArrayList = new ArrayList<>();
    MessageAdapter smsAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().setTitle(R.string.inbox);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        listView = (ListView) findViewById(R.id.listView);

        getAllInboxSMS();
        populateListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                String fromNumber = smsAdapter.getItem(position).getFromNumber();
                String fromName = ((TextView) view.findViewById(R.id.lblNumber)).getText().toString();

                Intent readMessagesIntent = new Intent(MainActivity.this, ReadMessageActivity.class);
                readMessagesIntent.putExtra("from_number", fromNumber);
                readMessagesIntent.putExtra("from_name", fromName);
                startActivity(readMessagesIntent);
            }
        });
    }

    public void getAllInboxSMS() {
        // Create Inbox URI
        Uri inboxURI = Uri.parse("content://sms/inbox");
        // List required columns
        String[] reqCols = new String[]{"address", "body"};
        Cursor cursor = getContentResolver().query(inboxURI, reqCols, null, null, null);

        // must check the result to prevent exception
        if (cursor.moveToFirst()) {
            // store message body and from Number in respective arrayLists
            while (!cursor.isAfterLast()) {
                String fromNumber = cursor.getString(cursor.getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                //populate fromArrayList and bodyArrayList
                fromArrayList.add(fromNumber);
                bodyArrayList.add(body);

                cursor.moveToNext();
            }
        } else {
            // empty box, no SMS
            new AlertDialog.Builder(this)
                    .setTitle("Title")
                    .setMessage("No messages in Inbox")
                    .show();
        }
        cursor.close();
    }

    public void populateListView() {
        for (int i = 0; i < fromArrayList.size(); i++) {
            SMSMessage smsMessage = new SMSMessage();
            boolean addTosmsArray = true;
            int occurrenceOfSameFrom = Collections.frequency(fromArrayList, fromArrayList.get(i));
            //do not add older message from same number in smsMessageArrayList
            for (int j = 0; j < smsMessageArrayList.size(); j++) {
                if (smsMessageArrayList.get(j).getFromNumber().equals(fromArrayList.get(i))) {
                    addTosmsArray = false;
                    break;
                }
            }
            //smsMessage is added to smsMessageArrayList only if sender is unique or is latest message(in case of multiple messages from same sender)
            if (addTosmsArray) {
                smsMessage.setFromNumber(fromArrayList.get(i));
                smsMessage.setNumberOfMessages(occurrenceOfSameFrom);
                smsMessage.setMessageBody(bodyArrayList.get(i));
                //add to smsMessageArrayList
                smsMessageArrayList.add(smsMessage);
            }
        }
        smsAdapter = new MessageAdapter(this, R.layout.row, smsMessageArrayList);
        listView.setAdapter(smsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Context context = getApplicationContext();

                Intent intent = new Intent(context, SearchResultsActivity.class);
                intent.putExtra("searched_text", query);
                startActivity(intent);

                searchView.setQuery("", false);
                searchView.clearFocus();

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.compose_sms) {
            Intent composeSMS = new Intent(getApplicationContext(), ComposeSMSActivity.class);
            startActivity(composeSMS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
