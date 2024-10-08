package com.example.auth.employee.model;

public class EmployeeDto {

    private Long id;
    private String name;

    /**
     * 
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id new value of {@link #getId}
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name new value of {@link #getName}
     */
    public void setName(String name) {
        this.name = name;
    }

}
