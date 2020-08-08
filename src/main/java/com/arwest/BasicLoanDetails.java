package com.arwest;

public class BasicLoanDetails
{
    private double amount;
    private String startDate;
    private String endDate;

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(final double amount)
    {
        this.amount = amount;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(final String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(final String endDate)
    {
        this.endDate = endDate;
    }

    @Override
    public String toString()
    {
        return "LoanDetails{" +
            "amount=" + amount +
            ", startDate='" + startDate + '\'' +
            ", endDate='" + endDate + '\'' +
            '}';
    }
}
