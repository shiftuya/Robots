package ru.nsu.fit.markelov.objects;

import ru.nsu.fit.markelov.interfaces.SimulationResult;

public class SimulationResultClass implements SimulationResult {

    private int id;
    private String date;
    private boolean successful;

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
