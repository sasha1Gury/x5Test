package com.x5.historyparser.service;

import com.x5.historyparser.entity.Dividend;
import com.x5.historyparser.exception.ExceptSupport;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DividendMintServiceImpl implements DividendService {


    public void parseX5HistoryAndSave() {
        Document doc = null;
        try {
            doc = Jsoup.connect(MINT_URL).get();
        } catch (IOException e) {
            throw new ExceptSupport("DividendService_01",
                    String.format("Error while getting Jsoup connect, cause - %s", e.getMessage())
            );
        }

        Element historyDiv = doc.getElementById("history");
        if (historyDiv == null) {
            System.out.println("Div с id=history не найден");
            throw new ExceptSupport("DividendService_02", String.format("Div with id=history was not found in url - %s", MINT_URL));
        }

        Elements rows = historyDiv.select("table.table tbody tr");

        StringBuilder result = new StringBuilder();

        for (Element row : rows) {
            Elements tds = row.select("td");
            if (tds.size() < 7) continue;

            String buyUntil = tds.get(1).text();
            String registryDate = tds.get(2).text();
            String paymentDate = tds.get(3).text();
            String period = tds.get(4).text();
            String dividend = tds.get(5).text();
            String yield = tds.get(6).text();

            result.append(String.format(
                    "Купить до: %s, Реестр: %s, Выплата: %s, Период: %s, Дивиденд: %s, Доходность: %s%n",
                    buyUntil, registryDate, paymentDate, period, dividend, yield
            ));
        }

    }

    public List<Dividend> getX5HistoryDividends() {
        return null;
    }
}
