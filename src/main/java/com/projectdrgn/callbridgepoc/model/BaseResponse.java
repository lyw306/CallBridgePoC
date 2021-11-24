package com.projectdrgn.callbridgepoc.model;

import lombok.Data;

@Data
public class BaseResponse {
    String uuid;
    Boolean success;
    String message;
}