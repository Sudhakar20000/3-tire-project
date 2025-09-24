package com.ewd.controller;

import com.ewd.entity.ReportingManager;
import com.ewd.dto.Response;
import com.ewd.service.ReportingManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ReportingManagerController {

    @Autowired
    private ReportingManagerService reportingManagerService;

    @PostMapping("/addManager")
    public ResponseEntity<Response> add(@RequestBody ReportingManager reportingManager) {
        log.info("Entered into addManager API");
        Response response = reportingManagerService.addReportingManager(reportingManager);
        log.info("Exiting from addManager API");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateManager/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody ReportingManager reportingManager) {
        log.info("Entered into updateManager API");
        Response response = reportingManagerService.updateReportingManager(id, reportingManager);
        log.info("Exiting from updateManager API");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteManager/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        log.info("Entered into deleteManager API");
        Response response = reportingManagerService.deleteReportingManager(id);
        log.info("Exiting from deleteManager API");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getManager/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        log.info("Entered into getManager API");
        Response response = reportingManagerService.getReportingManagerById(id);
        log.info("Exiting from getManager API");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllManagers")
    public ResponseEntity<Response> getAll() {
        log.info("Entered into getAllManagers API");
        Response response = reportingManagerService.getAllReportingManagers();
        log.info("Exiting from getAllManagers API");
        return ResponseEntity.ok(response);
    }
}
