package com.example.listofemployees;

import java.util.Calendar;
import java.util.UUID;

public interface IAction {
    void addPerson(String firstName, String secondName, boolean isFemale, Calendar dareOfBirth);

    void editPerson(String firstName, String secondName, boolean isFemale, UUID mSelectedPersonUUID);
}