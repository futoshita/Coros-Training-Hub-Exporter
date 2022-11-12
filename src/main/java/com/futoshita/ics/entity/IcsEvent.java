package com.futoshita.ics.entity;

import lombok.Data;

@Data
public class IcsEvent {

    private Integer dtEnd;
    private Integer dtStart;
    private String description;
    private String summary;
}
