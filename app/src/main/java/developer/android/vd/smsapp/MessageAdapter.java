package developer.android.vd.smsapp;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by VD on 5/15/2016.
 */
public class MessageAdapter extends ArrayAdapter<SMSMessage> {
    public MessageAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public MessageAdapter(Context context, int resource, List<SMSMessage> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.row, null);

            holder = new ViewHolder();
            holder.numberTextView = (TextView) view.findViewById(R.id.lblNumber);
            holder.countTextView = (TextView) view.findViewById(R.id.count);
            holder.messageTextView = (TextView) view.findViewById(R.id.lblMsg);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        SMSMessage smsItem = getItem(position);
        Cursor contactsCursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if (smsItem != null) {

            if (holder.numberTextView != null) {
                boolean contactFound = false;
                if (contactsCursor.moveToFirst()) {
                    while (!contactsCursor.isAfterLast()) {
                        String contactName = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String phoneNumber = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (smsItem.getFromNumber().equals(phoneNumber)) {
                            holder.numberTextView.setText(contactName);
                            contactFound = true;
                            break;
                        }
                        contactsCursor.moveToNext();
                    }
                }
                contactsCursor.close();
                if (!contactFound)
                    holder.numberTextView.setText(smsItem.getFromNumber());
            }

            if (holder.countTextView != null) {
                if (smsItem.getNumberOfMessages() > 1)
                    holder.countTextView.setText(String.valueOf(smsItem.getNumberOfMessages()));
                else
                    holder.countTextView.setText("");
            }

            if (holder.messageTextView != null) {
                holder.messageTextView.setText(smsItem.getMessageBody());
            }
        }
        return view;
    }
}

class ViewHolder {
    public TextView numberTextView;
    public TextView countTextView;
    public TextView messageTextView;
}