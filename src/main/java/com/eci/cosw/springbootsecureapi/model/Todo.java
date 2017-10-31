package com.eci.cosw.springbootsecureapi.model;

/**
 * @author Santiago Carrillo
 * 10/31/17.
 */
public class Todo
{

    private String description;

    private int priority;

    private boolean completed;

    public Todo( String description, int priority )
    {
        this.description = description;
        this.priority = priority;
    }

    public String getDescription()
    {
        return description;
    }

    public int getPriority()
    {
        return priority;
    }

    public boolean isCompleted()
    {
        return completed;
    }
}
