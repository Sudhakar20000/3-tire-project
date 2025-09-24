package com.ewd.service;

import com.ewd.dto.EmployeeAndProjectDetails;
import com.ewd.exception.NoDataFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReportClient {

    @Autowired
    private WebClient webClient;

    public List<EmployeeAndProjectDetails> callSendReportByFilter(String mashreqReportingManager,
                                                                  String employeeName,
                                                                  String projectName,
                                                                  String status) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/sendReportByFilter")
                        .queryParamIfPresent("mashreqReportingManager", Optional.ofNullable(mashreqReportingManager))
                        .queryParamIfPresent("employeeName", Optional.ofNullable(employeeName))
                        .queryParamIfPresent("projectName", Optional.ofNullable(projectName))
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .build())
                .retrieve()
                .onStatus(statuss -> statuss.value() >= 400 && statuss.value() < 500, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.warn("404 error from downstream: {}", errorBody);
                                    return Mono.error(new NoDataFoundException("No employee or project data found"));
                                });
                    }
                    return clientResponse.createException().flatMap(Mono::error);
                })
                .bodyToFlux(EmployeeAndProjectDetails.class)
                .collectList()
                .block();
    }



    public void callSendEmail(String mashreqReportingManager,
                              String employeeName,
                              String projectName,
                              String status) {

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/sendEmail")
                        .queryParamIfPresent("mashreqReportingManager", Optional.ofNullable(mashreqReportingManager))
                        .queryParamIfPresent("employeeName", Optional.ofNullable(employeeName))
                        .queryParamIfPresent("projectName", Optional.ofNullable(projectName))
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .build())
                .retrieve()
                .onStatus(statuss -> statuss.value() >= 400 && statuss.value() < 500, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.warn("404 error from downstream: {}", errorBody);
                                    return Mono.error(new NoDataFoundException("No data found to send email."));
                                });
                    }
                    return clientResponse.createException().flatMap(Mono::error);
                })
                .toBodilessEntity()
                .block();
    }


}
