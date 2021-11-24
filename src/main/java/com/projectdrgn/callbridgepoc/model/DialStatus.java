package com.projectdrgn.callbridgepoc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dialStatus")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
public class DialStatus extends BaseModel {

    private String dialcallstatus;

    private String dialcallsid;

    private String dialcallduration;

    private String recordingurl;

}
