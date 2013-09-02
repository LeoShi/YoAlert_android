package com.yoalert.cops;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: lei
 * Date: 7/30/13
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Incident implements Serializable {
    public static final String OPEN = "Open";
    public static final String PROCESSING = "In Progress";
    public static final String PROCESSED = "Closed";
    private long id;
    private String category;
    private String updatedTime;
    private String status;
    private String location;
    private String victim;
    private String contact;
    private String reference;
    private String createTime;

    public Incident(long id, String category, String updatedTime, String status, String location,
                    String victim, String contact, String reference, String createTime) {
        this.id = id;
        this.category = category;
        this.updatedTime = updatedTime;
        this.status = status;
        this.location = location;
        this.victim = victim;
        this.contact = contact;
        this.reference = reference;
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getUpdateTime() {
        return updatedTime;
    }

    public String getLocation() {
        return location;
    }

    public String getVictim() {
        return victim;
    }

    public String getContact() {
        return contact;
    }

    public String getReference() {
        return reference;
    }

    public String getStatus() {
        return status;
    }

    public String getCreateTime() {
        return createTime;
    }
}


