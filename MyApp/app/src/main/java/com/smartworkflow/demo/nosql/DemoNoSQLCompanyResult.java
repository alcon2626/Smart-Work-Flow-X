package com.smartworkflow.demo.nosql;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.CompanyDO;

import java.util.Set;

public class DemoNoSQLCompanyResult implements DemoNoSQLResult {
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    private final CompanyDO result;

    DemoNoSQLCompanyResult(final CompanyDO result) {
        this.result = result;
    }
    @Override
    public void updateItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        final String originalValue = result.getAddress();
        result.setAddress(DemoSampleDataGenerator.getRandomSampleString("Address"));
        try {
            mapper.save(result);
        } catch (final AmazonClientException ex) {
            // Restore original data if save fails, and re-throw.
            result.setAddress(originalValue);
            throw ex;
        }
    }

    @Override
    public void deleteItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        mapper.delete(result);
    }

    private void setKeyTextViewStyle(final TextView textView) {
        textView.setTextColor(KEY_TEXT_COLOR);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(5), dp(2), dp(5), 0);
        textView.setLayoutParams(layoutParams);
    }

    /**
     * @param dp number of design pixels.
     * @return number of pixels corresponding to the desired design pixels.
     */
    private int dp(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    private void setValueTextViewStyle(final TextView textView) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dp(15), 0, dp(15), dp(2));
        textView.setLayoutParams(layoutParams);
    }

    private void setKeyAndValueTextViewStyles(final TextView keyTextView, final TextView valueTextView) {
        setKeyTextViewStyle(keyTextView);
        setValueTextViewStyle(valueTextView);
    }

    private static String bytesToHexString(byte[] bytes) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("%02X", bytes[0]));
        for(int index = 1; index < bytes.length; index++) {
            builder.append(String.format(" %02X", bytes[index]));
        }
        return builder.toString();
    }

    private static String byteSetsToHexStrings(Set<byte[]> bytesSet) {
        final StringBuilder builder = new StringBuilder();
        int index = 0;
        for (byte[] bytes : bytesSet) {
            builder.append(String.format("%d: ", ++index));
            builder.append(bytesToHexString(bytes));
            if (index < bytesSet.size()) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    @Override
    public View getView(final Context context, final View convertView, int position) {
        final LinearLayout layout;
        final TextView resultNumberTextView;
        final TextView userIdKeyTextView;
        final TextView userIdValueTextView;
        final TextView nameKeyTextView;
        final TextView nameValueTextView;
        final TextView addressKeyTextView;
        final TextView addressValueTextView;
        final TextView cityKeyTextView;
        final TextView cityValueTextView;
        final TextView phoneNumberKeyTextView;
        final TextView phoneNumberValueTextView;
        final TextView stateKeyTextView;
        final TextView stateValueTextView;
        final TextView zipCodeKeyTextView;
        final TextView zipCodeValueTextView;
        if (convertView == null) {
            layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            resultNumberTextView = new TextView(context);
            resultNumberTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(resultNumberTextView);


            userIdKeyTextView = new TextView(context);
            userIdValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(userIdKeyTextView, userIdValueTextView);
            layout.addView(userIdKeyTextView);
            layout.addView(userIdValueTextView);

            nameKeyTextView = new TextView(context);
            nameValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(nameKeyTextView, nameValueTextView);
            layout.addView(nameKeyTextView);
            layout.addView(nameValueTextView);

            addressKeyTextView = new TextView(context);
            addressValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(addressKeyTextView, addressValueTextView);
            layout.addView(addressKeyTextView);
            layout.addView(addressValueTextView);

            cityKeyTextView = new TextView(context);
            cityValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(cityKeyTextView, cityValueTextView);
            layout.addView(cityKeyTextView);
            layout.addView(cityValueTextView);

            phoneNumberKeyTextView = new TextView(context);
            phoneNumberValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(phoneNumberKeyTextView, phoneNumberValueTextView);
            layout.addView(phoneNumberKeyTextView);
            layout.addView(phoneNumberValueTextView);

            stateKeyTextView = new TextView(context);
            stateValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(stateKeyTextView, stateValueTextView);
            layout.addView(stateKeyTextView);
            layout.addView(stateValueTextView);

            zipCodeKeyTextView = new TextView(context);
            zipCodeValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(zipCodeKeyTextView, zipCodeValueTextView);
            layout.addView(zipCodeKeyTextView);
            layout.addView(zipCodeValueTextView);
        } else {
            layout = (LinearLayout) convertView;
            resultNumberTextView = (TextView) layout.getChildAt(0);

            userIdKeyTextView = (TextView) layout.getChildAt(1);
            userIdValueTextView = (TextView) layout.getChildAt(2);

            nameKeyTextView = (TextView) layout.getChildAt(3);
            nameValueTextView = (TextView) layout.getChildAt(4);

            addressKeyTextView = (TextView) layout.getChildAt(5);
            addressValueTextView = (TextView) layout.getChildAt(6);

            cityKeyTextView = (TextView) layout.getChildAt(7);
            cityValueTextView = (TextView) layout.getChildAt(8);

            phoneNumberKeyTextView = (TextView) layout.getChildAt(9);
            phoneNumberValueTextView = (TextView) layout.getChildAt(10);

            stateKeyTextView = (TextView) layout.getChildAt(11);
            stateValueTextView = (TextView) layout.getChildAt(12);

            zipCodeKeyTextView = (TextView) layout.getChildAt(13);
            zipCodeValueTextView = (TextView) layout.getChildAt(14);
        }

        resultNumberTextView.setText(String.format("#%d", + position+1));
        userIdKeyTextView.setText("userId");
        userIdValueTextView.setText(result.getUserId());
        nameKeyTextView.setText("Name");
        nameValueTextView.setText(result.getName());
        addressKeyTextView.setText("Address");
        addressValueTextView.setText(result.getAddress());
        cityKeyTextView.setText("City");
        cityValueTextView.setText(result.getCity());
        phoneNumberKeyTextView.setText("PhoneNumber");
        phoneNumberValueTextView.setText("" + result.getPhoneNumber().longValue());
        stateKeyTextView.setText("State");
        stateValueTextView.setText(result.getState());
        zipCodeKeyTextView.setText("ZipCode");
        zipCodeValueTextView.setText("" + result.getZipCode().longValue());
        return layout;
    }
}
