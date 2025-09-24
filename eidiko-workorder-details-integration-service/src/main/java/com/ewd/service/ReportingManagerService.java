package com.ewd.service;

import com.ewd.dto.Response;
import com.ewd.entity.ReportingManager;

public interface ReportingManagerService {

    Response addReportingManager(ReportingManager reportingManager);

    Response updateReportingManager(Long id, ReportingManager reportingManager);

    Response deleteReportingManager(Long id);

    Response getReportingManagerById(Long id);

    Response getAllReportingManagers();
}
