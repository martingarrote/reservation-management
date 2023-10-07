package com.martingarrote.reservationmanagement.utils;

import com.martingarrote.reservationmanagement.interfaces.AuditInterface;

import java.sql.Timestamp;

public class AuditUtils {

    public void AuditDefineFields(AuditInterface object) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if (object.getCreatedAt() == null || object.getCreatedBy() == null) {
            object.setCreatedBy("Admin");
            object.setCreatedAt(timestamp);
        }

        object.setUpdatedBy("Admin");
        object.setUpdatedAt(timestamp);
    }

}
