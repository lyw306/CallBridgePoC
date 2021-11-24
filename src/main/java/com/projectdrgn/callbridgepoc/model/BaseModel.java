package com.projectdrgn.callbridgepoc.model;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@SuppressWarnings("serial")
public class BaseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
