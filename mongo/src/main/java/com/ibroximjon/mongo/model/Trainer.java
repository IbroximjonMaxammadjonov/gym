package com.ibroximjon.mongo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document()
public class Trainer {
    private String username;
    private String firstName;
    private String lastName;
    private boolean status;
    private List<YearlySummary> yearlySummaries;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<YearlySummary> getYearlySummaries() {
        return yearlySummaries;
    }

    public void setYearlySummaries(List<YearlySummary> yearlySummaries) {
        this.yearlySummaries = yearlySummaries;
    }

    private class YearlySummary {
        private int year;
        private List<MonthlySummary> months = new ArrayList<>();

        public YearlySummary(int year) {
            this.year = year;
            for (int i = 1; i <= 12; i++) {
                months.add(new MonthlySummary(i, 0));
            }
        }

        public MonthlySummary getMonth(int monthNumber) {
            return months.get(monthNumber - 1);
        }
    }

    public class MonthlySummary {
        private int month; // 1-12
        private int trainingDurationSummary;

        public MonthlySummary(int i, int i1) {
            month = i;
            trainingDurationSummary = i1;
        }
    }

}
