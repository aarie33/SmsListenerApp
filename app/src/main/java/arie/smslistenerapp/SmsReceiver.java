package arie.smslistenerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    final SmsManager sms = SmsManager.getDefault();

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = getIncomingMessage(pdusObj[i], bundle);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    Log.i("Sms Receiver", "sender Num: " + senderNum + "; message : " + message);
                    Intent showsmsIntent = new Intent(context, SmsReceiverActivity.class);
                    showsmsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    showsmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_NO, phoneNumber);
                    showsmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_MESSAGE, message);
                    context.startActivity(showsmsIntent);
                }
            }
        } catch (Exception e) {
            Log.e("Sms Receiver ", "Exception smsReceiver " + e);
        }
    }

    private SmsMessage getIncomingMessage(Object object, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) object, format);
        }else{
            currentSMS = SmsMessage.createFromPdu((byte[]) object);
        }
        return currentSMS;
    }
}
