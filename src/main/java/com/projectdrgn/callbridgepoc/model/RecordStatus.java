package com.projectdrgn.callbridgepoc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "recordStatus")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RecordStatus extends BaseModel {
    String accountsid;
    String callsid;
    String recordingsid;
    String recordingurl;
    String recordingstatus;
    String recordingduration;
    String recordingchannels;
    String recordingstarttime;
    String recordingsource;

}
