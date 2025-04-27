package com.x5.historyparser.service;

import com.x5.historyparser.entity.Dividend;

import java.util.List;

public interface DividendService {
    String DOHOD_URL = "https://www.dohod.ru/ik/analytics/dividend/x5";
    String MINT_URL = "https://investmint.ru/x5/";

    void parseX5HistoryAndSave();
    List<Dividend> getX5HistoryDividends();
}
