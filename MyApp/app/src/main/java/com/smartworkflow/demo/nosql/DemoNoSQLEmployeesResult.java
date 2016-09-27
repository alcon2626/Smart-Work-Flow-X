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
import com.amazonaws.models.nosql.EmployeesDO;

import java.util.Set;

public class DemoNoSQLEmployeesResult implements DemoNoSQLResult {
    private static final int KEY_TEXT_COLOR = 0xFF333333;
    private final EmployeesDO result;

    DemoNoSQLEmployeesResult(final EmployeesDO result) {
        this.result = result;
    }
    @Override
    public void updateItem() {
        final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        final String originalValue = result.getCity();
        result.setCity(DemoSampleDataGenerator.getRandomSampleString("City"));
        try {
            mapper.save(result);
        } catch (final AmazonClientException ex) {
            // Restore original data if save fails, and re-throw.
            result.setCity(originalValue);
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
        final TextView firstNameKeyTextView;
        final TextView firstNameValueTextView;
        final TextView cityKeyTextView;
        final TextView cityValueTextView;
        final TextView clockStatusKeyTextView;
        final TextView clockStatusValueTextView;
        final TextView companyKeyTextView;
        final TextView companyValueTextView;
        final TextView familyNameKeyTextView;
        final TextView familyNameValueTextView;
        final TextView phoneNumberKeyTextView;
        final TextView phoneNumberValueTextView;
        final TextView stateKeyTextView;
        final TextView stateValueTextView;
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

            firstNameKeyTextView = new TextView(context);
            firstNameValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(firstNameKeyTextView, firstNameValueTextView);
            layout.addView(firstNameKeyTextView);
            layout.addView(firstNameValueTextView);

            cityKeyTextView = new TextView(context);
            cityValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(cityKeyTextView, cityValueTextView);
            layout.addView(cityKeyTextView);
            layout.addView(cityValueTextView);

            clockStatusKeyTextView = new TextView(context);
            clockStatusValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(clockStatusKeyTextView, clockStatusValueTextView);
            layout.addView(clockStatusKeyTextView);
            layout.addView(clockStatusValueTextView);

            companyKeyTextView = new TextView(context);
            companyValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(companyKeyTextView, companyValueTextView);
            layout.addView(companyKeyTextView);
            layout.addView(companyValueTextView);

            familyNameKeyTextView = new TextView(context);
            familyNameValueTextView = new TextView(context);
            setKeyAndValueTextViewStyles(familyNameKeyTextView, familyNameValueTextView);
            layout.addView(familyNameKeyTextView);
            layout.addView(familyNameValueTextView);

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
        } else {
            layout = (LinearLayout) convertView;
            resultNumberTextView = (TextView) layout.getChildAt(0);

            userIdKeyTextView = (TextView) layout.getChildAt(1);
            userIdValueTextView = (TextView) layout.getChildAt(2);

            firstNameKeyTextView = (TextView) layout.getChildAt(3);
            firstNameValueTextView = (TextView) layout.getChildAt(4);

            cityKeyTextView = (TextView) layout.getChildAt(5);
            cityValueTextView = (TextView) layout.getChildAt(6);

            clockStatusKeyTextView = (TextView) layout.getChildAt(7);
            clockStatusValueTextView = (TextView) layout.getChildAt(8);

            companyKeyTextView = (TextView) layout.getChildAt(9);
            companyValueTextView = (TextView) layout.getChildAt(10);

            familyNameKeyTextView = (TextView) layout.getChildAt(11);
            familyNameValueTextView = (TextView) layout.getChildAt(12);

            phoneNumberKeyTextView = (TextView) layout.getChildAt(13);
            phoneNumberValueTextView = (TextView) layout.getChildAt(14);

            stateKeyTextView = (TextView) layout.getChildAt(15);
            stateValueTextView = (TextView) layout.getChildAt(16);
        }

        resultNumberTextView.setText(String.format("#%d", + position+1));
        userIdKeyTextView.setText("userId");
        userIdValueTextView.setText(result.getUserId());
        firstNameKeyTextView.setText("FirstName");
        firstNameValueTextView.setText(result.getFirstName());
        cityKeyTextView.setText("City");
        cityValueTextView.setText(result.getCity());
        clockStatusKeyTextView.setText("ClockStatus");
        clockStatusValueTextView.setText("" + result.getClockStatus());
        companyKeyTextView.setText("Company");
        companyValueTextView.setText(result.getCompany());
        familyNameKeyTextView.setText("FamilyName");
        familyNameValueTextView.setText(result.getFamilyName());
        phoneNumberKeyTextView.setText("PhoneNumber");
        phoneNumberValueTextView.setText("" + result.getPhoneNumber().longValue());
        stateKeyTextView.setText("State");
        stateValueTextView.setText(result.getState());
        return layout;
    }
}
