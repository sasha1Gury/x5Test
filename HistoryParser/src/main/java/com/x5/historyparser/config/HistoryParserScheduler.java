package com.x5.historyparser.config;

import com.x5.historyparser.service.DividendService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HistoryParserScheduler {
    private final DividendService dividendService;

    public HistoryParserScheduler(@Qualifier("dividendDohodServiceImpl") DividendService dividendService) {
        this.dividendService = dividendService;
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void parseHistoryEveryMonth() {
        dividendService.parseX5HistoryAndSave();
    }
}
