package com.hackerda.platform.controller.vo;

import lombok.Data;

import java.util.List;

@Data
public class PostDetailVO {

    private long nextMaxId;

    private long nextTimestamp;

    private int count;

    private List<PostVO> postList;

}
