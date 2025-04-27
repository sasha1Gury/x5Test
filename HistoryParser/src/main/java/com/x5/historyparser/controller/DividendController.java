package com.x5.historyparser.controller;

import com.x5.historyparser.entity.Dividend;
import com.x5.historyparser.service.DividendService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/")
public class DividendController {
    private final DividendService dividendService;

    public DividendController(@Qualifier("dividendDohodServiceImpl") DividendService dividendService) {
        this.dividendService = dividendService;
    }

    @GetMapping("history")
    public ResponseEntity<?> getHistory(@RequestParam(defaultValue = "x5") String dividendType) {
        if (!dividendType.equals("x5")) {
            return ResponseEntity.badRequest().body("Пока что вы можете посмотреть историю дивидендов только x5 group, " +
                    "параметр 'x5'");
        }
        List<Dividend> dividendList = dividendService.getX5HistoryDividends();
        return ResponseEntity.ok(dividendList);
    }
}

