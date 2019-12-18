package ru.nsu.fit.markelov.objects;

import ru.nsu.fit.markelov.interfaces.Attempt;

public class AttemptClass implements Attempt {

    private int id;
    private String date;
    private boolean successed;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean isSuccessful() {
        return successed;
    }

    public void setSuccessed(boolean successed) {
        this.successed = successed;
    }
}
