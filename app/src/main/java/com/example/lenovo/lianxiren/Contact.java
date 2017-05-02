package com.example.lenovo.lianxiren;

/**
 * Created by Lenovo on 2017/4/13.
 */

public class Contact
{
    private String name;
    private String number;
    private String sortKey;//排序字母
    public Contact(){

    }
    public Contact(String name,String number,String sortKey)
    {
        this.name=name;
        this.number=number;
        this.sortKey=sortKey;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
    public void setSortKey(String sortKey)
    {
        this.sortKey=sortKey;
    }
    public String getSortKey()
    {
        return sortKey;
    }
    public void setNumber(String number)
    {
        this.number=number;
    }
    public String getNumber()
    {
        return number;
    }

}
