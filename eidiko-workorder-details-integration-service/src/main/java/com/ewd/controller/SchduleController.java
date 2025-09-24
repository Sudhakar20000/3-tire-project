package com.ewd.controller;

import com.ewd.dto.EmployeeAndProjectDetails;
import com.ewd.exception.NoDataFoundException;
import com.ewd.service.ReportClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class SchduleController {

    private final ReportClient client;

    public SchduleController(ReportClient client) {
        this.client = client;
    }


    @GetMapping("/Search")
    public ResponseEntity<List<EmployeeAndProjectDetails>> getFilteredReport(
            @RequestParam(required = false) String mashreqReportingManager,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String status) {

        if (mashreqReportingManager == null &&
                employeeName == null &&
                projectName == null &&
                status == null) {
            throw new NoDataFoundException("At least one of mashreqReportingManager, employeeName, projectName, or status must be provided.");
        }

        List<EmployeeAndProjectDetails> details = client.callSendReportByFilter(
                mashreqReportingManager, employeeName, projectName, status);

        return ResponseEntity.ok(details);
    }


    @PostMapping("/sendMail")
    public ResponseEntity<String> triggerEmail(
            @RequestParam(required = false) String mashreqReportingManager,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String status) {

        if (mashreqReportingManager == null &&
                employeeName == null &&
                projectName == null &&
                status == null) {
            throw new NoDataFoundException("At least one of mashreqReportingManager, employeeName, projectName, or status must be provided.");
        }

        client.callSendEmail(mashreqReportingManager, employeeName, projectName, status);

        return ResponseEntity.ok("Email trigger request sent to Scheduler Service.");
    }
}
