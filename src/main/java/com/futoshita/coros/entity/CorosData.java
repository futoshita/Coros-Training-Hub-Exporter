package com.futoshita.coros.entity;

import lombok.Data;

import java.util.List;

@Data
public class CorosData {

    // login.json
    private String accessToken;

    // training-calendar.json
    private List<CorosEntity> entities;
    private List<CorosProgram> programs;

    // activities.json
    private Integer count;
    private Integer pageNumber;
    private Integer totalPage;
    private List<CorosActivity> dataList;

    // activity.json
    private String fileUrl;
}
