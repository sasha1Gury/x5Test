package com.x5.historyparser.service;

import com.x5.historyparser.exception.ExceptSupport;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DividendService {
    public String parseHistory(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new ExceptSupport("DividendService_01",
                    String.format("Error while getting Jsoup connect, cause - %s", e.getMessage())
            );
        }

        Element historyDiv = doc.getElementById("history");
        if (historyDiv == null) {
            System.out.println("Div с id=history не найден");
            throw new ExceptSupport("DividendService_02", String.format("Div with id=history was not found in url - %s", url));
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

        return result.toString();
    }

    public String parseHistoryDohod(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new ExceptSupport("DividendService_01",
                    String.format("Error while getting Jsoup connect, cause - %s", e.getMessage())
            );
        }

        StringBuilder result = new StringBuilder();

        // Ищем таблицу с классом "content-table"
        Elements tables = doc.select("table.content-table");

        for (Element table : tables) {
            // Проверяем заголовки
            Elements headers = table.select("th");
            boolean correctTable = headers.stream().anyMatch(th -> th.text().contains("Дата объявления дивиденда"));

            if (correctTable) {
                // Парсим строки
                Elements rows = table.select("tbody > tr");

                for (Element row : rows) {
                    Elements tds = row.select("td");

                    if (tds.size() < 4) continue; // тут в твоей таблице минимум 4 td, а не 7!

                    String announcementDate = tds.get(0).text().trim();
                    String registryCloseDate = tds.get(1).text().trim();
                    String year = tds.get(2).text().trim();
                    String dividend = tds.get(3).text().trim();

                    result.append(String.format(
                            "Объявление: %s, Реестр: %s, Год: %s, Дивиденд: %s%n",
                            announcementDate, registryCloseDate, year, dividend
                    ));
                }
                break; // таблицу нашли — дальше искать не надо
            }
        }

        return result.toString();
    }
}
