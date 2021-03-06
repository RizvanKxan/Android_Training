package com.example.listofemployees;

import android.content.Context;

import com.example.listofemployees.database.AppDatabase;
import com.example.listofemployees.database.dao.PersonDao;
import com.example.listofemployees.database.entity.Person;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PersonBank {
    private static PersonBank sPersonBank;
    private final PersonDao personDao;
    private final AppDatabase db;
    private final ExecutorService executorService;

    private PersonBank(Context context, ExecutorService executorService) {
        this.executorService = executorService;
        Context mContext = context.getApplicationContext();

        db = App.getInstance().getDatabase();
        personDao = db.personDao();
    }

    public static PersonBank get(Context context) {
        if (sPersonBank == null) {
            sPersonBank = new PersonBank(context, Executors.newSingleThreadExecutor());
        }
        return sPersonBank;
    }

    public void getPersons(Result<List<Person>> listener) {
        executorService.execute(() -> {
            try {
                List<Person> personList = personDao.getAll();
                listener.onSuccess(personList);
            } catch (Exception exception) {
                listener.onError(exception);
            }
        });
    }

    public void getPerson(Result<Person> listener, UUID uuid) {
        executorService.execute(() -> {
            try {
                Person person = personDao.getById(uuid);
                listener.onSuccess(person);
            } catch (Exception exception) {
                listener.onError(exception);
            }
        });
    }

    public void addPerson(Person person) {
        executorService.execute(() -> personDao.insertPerson(person));
    }

    public void addPersonList(List<Person> persons) {
        executorService.execute(() -> personDao.insertAllPerson(persons));
    }

    public void editPerson(UUID id, String firstName, String secondName, boolean isFemale) {
        executorService.execute(() -> {
            try {
                Person person = personDao.getById(id);
                person.setFirstName(firstName);
                person.setSecondName(secondName);
                person.setFemale(isFemale);
                personDao.updatePerson(person);
            } catch (Exception ex) {

            }
        });
    }

    public void deletePerson(Person person) {
        executorService.execute(() -> personDao.deletePerson(person));
    }

    public interface Result<T> {
        void onSuccess(T t);

        void onError(Exception exception);
    }
}
