package com.x5.historyparser.config;

import com.x5.historyparser.exception.ExceptDb;
import com.x5.historyparser.service.DividendService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class InitComponent {
    private final DividendService dividendService;

    public InitComponent(@Qualifier("dividendDohodServiceImpl") DividendService dividendService) {
        this.dividendService = dividendService;
    }

    @PostConstruct
    public void initialize() {
        try {
            dividendService.parseX5HistoryAndSave();
        } catch (Exception e) {
            throw new ExceptDb("InitComponent_01", String.format("Error while parsing X5HistoryAndSave, cause - %s", e.getMessage()));
        }
    }
}
