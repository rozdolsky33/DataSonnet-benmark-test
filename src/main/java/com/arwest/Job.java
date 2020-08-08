package com.arwest;

public class Job
{
    private String title;
    private double annualIncome;
    private int yearsActive;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public double getAnnualIncome()
    {
        return annualIncome;
    }

    public void setAnnualIncome(final double annualIncome)
    {
        this.annualIncome = annualIncome;
    }

    public int getYearsActive()
    {
        return yearsActive;
    }

    public void setYearsActive(final int yearsActive)
    {
        this.yearsActive = yearsActive;
    }

    @Override
    public String toString()
    {
        return "Job{" +
            "title='" + title + '\'' +
            ", annualIncome=" + annualIncome +
            ", yearsActive=" + yearsActive +
            '}';
    }
}
