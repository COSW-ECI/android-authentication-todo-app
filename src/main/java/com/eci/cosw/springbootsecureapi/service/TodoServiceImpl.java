package com.eci.cosw.springbootsecureapi.service;

import com.eci.cosw.springbootsecureapi.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Santiago Carrillo
 * 10/31/17.
 */
@Service
public class TodoServiceImpl
    implements TodoService
{
    private List<Todo> todoList = new ArrayList<>();

    @Autowired
    public TodoServiceImpl()
    {
    }

    @PostConstruct
    private void populateSampleData()
    {
        todoList.add( new Todo( "todo task 1", 4 ) );
        todoList.add( new Todo( "todo task 2", 2 ) );
        todoList.add( new Todo( "todo task 3", 3 ) );
    }

    @Override
    public List<Todo> getTodoList()
    {
        return todoList;
    }

    @Override
    public Todo addTodo( Todo todo )
    {
        todoList.add( todo );
        return todo;
    }
}
