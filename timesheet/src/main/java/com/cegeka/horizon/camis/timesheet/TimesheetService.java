package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.ResourceId;

import java.time.LocalDate;

public interface TimesheetService {
    Employee getTimesheetEntries(ResourceId resourceId, String employeeName, LocalDate dateFrom, LocalDate dateTo);
}
