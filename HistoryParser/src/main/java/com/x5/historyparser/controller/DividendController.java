package com.x5.historyparser.controller;

import com.x5.historyparser.entity.Dividend;
import com.x5.historyparser.service.DividendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "DividendController", description = "Контроллер для работы с дивидендами")
@RestController
@RequestMapping("/")
public class DividendController {
    private final DividendService dividendService;

    public DividendController(@Qualifier("dividendDohodServiceImpl") DividendService dividendService) {
        this.dividendService = dividendService;
    }

    @Operation(summary = "Получить историю дивидендов из бд", description = "Возвращает историю дивидендов компании по коду")
    @GetMapping("history")
    public ResponseEntity<?> getHistory(@RequestParam() String code) {
        if (!code.equals(Dividend.COMPANY_CODE_X5)) {
            return ResponseEntity.badRequest().body("Пока что вы можете посмотреть историю дивидендов только x5 group, " +
                    "параметр 'x5'");
        }
        List<Dividend> dividendList = dividendService.getX5HistoryDividends();
        return ResponseEntity.ok(dividendList);
    }
}

