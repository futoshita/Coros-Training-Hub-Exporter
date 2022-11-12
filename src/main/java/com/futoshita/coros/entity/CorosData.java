package com.futoshita.coros.entity;

import lombok.Data;

import java.util.List;

@Data
public class CorosData {

    private List<CorosEntity> entities;
    private List<CorosProgram> programs;

    private String accessToken;
}
