package io.github.silvigarabis.rplayneko.power;

public class RPlayNekoPowerType {
    private final String name;
    public String getName(){
        return name;
    }
    public RPlayNekoPowerType(String name){
        this.name = name;
    }
    @Override
    public boolean equals(Object o){
        return o instanceof RPlayNekoPowerType otype && otype.getName().equals(this.getName());
    }
    @Override
    public int hashCode(){
        return this.getName().hashCode() * 31;
    }
}
