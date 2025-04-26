package com.x5.historyparser.controller;

import com.x5.historyparser.service.DividendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/")
public class DividendController {
    private final DividendService dividendService;

    public DividendController(DividendService dividendService) {
        this.dividendService = dividendService;
    }

    @GetMapping("history")
    public ResponseEntity<String> getHistory(@RequestParam(defaultValue = "x5") String dividendType) {
        if (!dividendType.equals("x5")) {
            return ResponseEntity.badRequest().body("Пока что вы можете посмотреть историю дивидендов только x5 group, " +
                    "параметр 'x5'");
        }

        /*String url = "https://investmint.ru/x5/"; // здесь подставляешь реальный URL
        String history = dividendService.parseHistory(url);*/
        String url = "https://www.dohod.ru/ik/analytics/dividend/x5"; // здесь подставляешь реальный URL
        String history = dividendService.parseHistoryDohod(url);
        return ResponseEntity.ok().body(history);
    }
}

