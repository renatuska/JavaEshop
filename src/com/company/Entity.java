package com.company;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Entity {

    protected HashMap<String, Object> properties;

    public Entity()
    {
        properties = new HashMap<String, Object>();
    }

    public String print(){
        return null;
    }
}
