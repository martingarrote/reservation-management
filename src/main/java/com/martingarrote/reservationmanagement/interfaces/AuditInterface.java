package com.martingarrote.reservationmanagement.interfaces;

import java.sql.Timestamp;

public interface AuditInterface {

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    Timestamp getCreatedAt();

    void setCreatedAt(Timestamp createdAt);

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);

    Timestamp getUpdatedAt();

    void setUpdatedAt(Timestamp updatedAt);
}
