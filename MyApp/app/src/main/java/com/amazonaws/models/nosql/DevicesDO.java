package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "smartworkflow-mobilehub-1238073234-Devices")

public class DevicesDO {
    private String _userId;
    private String _deviceName;
    private String _deviceOwner;
    private String _deviceOwnerID;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "DeviceName")
    public String getDeviceName() {
        return _deviceName;
    }

    public void setDeviceName(final String _deviceName) {
        this._deviceName = _deviceName;
    }
    @DynamoDBAttribute(attributeName = "DeviceOwner")
    public String getDeviceOwner() {
        return _deviceOwner;
    }

    public void setDeviceOwner(final String _deviceOwner) {
        this._deviceOwner = _deviceOwner;
    }
    @DynamoDBAttribute(attributeName = "DeviceOwnerID")
    public String getDeviceOwnerID() {
        return _deviceOwnerID;
    }

    public void setDeviceOwnerID(final String _deviceOwnerID) {
        this._deviceOwnerID = _deviceOwnerID;
    }

}
