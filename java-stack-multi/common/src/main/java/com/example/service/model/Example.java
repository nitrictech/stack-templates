package com.example.service.model;

/**
 * Provides a Example model class.
 */
public class Example {

    private String name;
    private String description;

    // Constructors -----------------------------------------------------------

    public Example() {        
    }

    public Example(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Public Methods ---------------------------------------------------------

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return the string representation of this object.
     * 
     * @retrun the string representation of this object
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + 
            "[name=" + name +
            ", description=" + description +
            "]";
    }
}