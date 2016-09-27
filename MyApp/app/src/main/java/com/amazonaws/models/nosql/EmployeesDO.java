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

@DynamoDBTable(tableName = "smartworkflow-mobilehub-1238073234-Employees")

public class EmployeesDO {
    private String _userId;
    private String _firstName;
    private String _city;
    private Boolean _clockStatus;
    private String _company;
    private String _familyName;
    private Double _phoneNumber;
    private String _state;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBRangeKey(attributeName = "FirstName")
    @DynamoDBAttribute(attributeName = "FirstName")
    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(final String _firstName) {
        this._firstName = _firstName;
    }
    @DynamoDBAttribute(attributeName = "City")
    public String getCity() {
        return _city;
    }

    public void setCity(final String _city) {
        this._city = _city;
    }
    @DynamoDBAttribute(attributeName = "ClockStatus")
    public Boolean getClockStatus() {
        return _clockStatus;
    }

    public void setClockStatus(final Boolean _clockStatus) {
        this._clockStatus = _clockStatus;
    }
    @DynamoDBAttribute(attributeName = "Company")
    public String getCompany() {
        return _company;
    }

    public void setCompany(final String _company) {
        this._company = _company;
    }
    @DynamoDBAttribute(attributeName = "FamilyName")
    public String getFamilyName() {
        return _familyName;
    }

    public void setFamilyName(final String _familyName) {
        this._familyName = _familyName;
    }
    @DynamoDBAttribute(attributeName = "PhoneNumber")
    public Double getPhoneNumber() {
        return _phoneNumber;
    }

    public void setPhoneNumber(final Double _phoneNumber) {
        this._phoneNumber = _phoneNumber;
    }
    @DynamoDBAttribute(attributeName = "State")
    public String getState() {
        return _state;
    }

    public void setState(final String _state) {
        this._state = _state;
    }

}
