package ru.nsu.fit.markelov.objects_hardcoded;

import ru.nsu.fit.markelov.interfaces.SimulationResult;

import java.util.Date;

public class SimulationResultHardcoded implements SimulationResult {

    private int id;
    private Date date;
    private boolean successful;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public boolean isSuccessful(String username) {
        return successful;
    }

    @Override
    public String getLog(String username) {
        return "LOOOG!";
    }
}
