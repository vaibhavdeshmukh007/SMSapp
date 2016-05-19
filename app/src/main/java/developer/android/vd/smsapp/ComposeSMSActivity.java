package developer.android.vd.smsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by VD on 5/17/2016.
 */
public class ComposeSMSActivity extends AppCompatActivity {

    EditText newSMS;
    EditText toNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_sms);
        newSMS = (EditText) findViewById(R.id.composeNewSms);
        toNumber = (EditText) findViewById(R.id.toNumber);
    }

    public void sendNewSMS(View view) {
        SmsManager smsManager = SmsManager.getDefault();

        String sms = newSMS.getText().toString();
        String to = toNumber.getText().toString();
        if (!(sms.equals("") || to.equals(""))) {
            try {
                smsManager.sendTextMessage(to, null, sms, null, null);
                Toast.makeText(this, "Status of sent SMS can be checked in built-in Messaging app.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "SMS can not be sent to this number :(", Toast.LENGTH_SHORT).show();
                toNumber.setText("");
            } finally {
                finish();
            }
        }
    }
}
