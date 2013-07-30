package com.yoalert.cops;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: lei
 * Date: 7/30/13
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Incident {
    public static final String UNPROCESS = "unprocess";
    public static final String PROCESSING = "processing";
    public static final String PROCESSED = "processed";
    private long id;
    private String category;
    private Date date;
    public String status;

    public Incident(long id, String category, Date date, String status) {
        this.id = id;
        this.category = category;
        this.date = date;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }
}
