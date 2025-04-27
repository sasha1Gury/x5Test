package com.x5.historyparser.service;

import com.x5.historyparser.entity.Dividend;
import com.x5.historyparser.exception.ExceptDb;
import com.x5.historyparser.exception.ExceptSupport;
import com.x5.historyparser.repository.DividendRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DividendDohodServiceImpl implements DividendService {

    public static final String NOT_AVAILABLE = "n/a";
    public static final String FORECAST_MARK = "(прогноз)";

    private final DividendRepository dividendRepository;

    @Transactional
    public void parseX5HistoryAndSave() {
        List<Dividend> dividendList = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.connect(DOHOD_URL).get();
        } catch (IOException e) {
            throw new ExceptSupport("DividendService_01",
                    String.format("Error while getting Jsoup connect, cause - %s", e.getMessage())
            );
        }

        Elements tables = doc.select("table.content-table");
        for (Element table : tables) {
            Elements headers = table.select("th");
            boolean correctTable = headers.stream().anyMatch(th -> th.text().contains("Дата объявления дивиденда"));

            if (correctTable) {
                Elements rows = table.select("tbody > tr");

                for (Element row : rows) {
                    Elements tds = row.select("td");
                    if (tds.size() < 4) continue;

                    String declarationDate = tds.get(0).text().trim();
                    String registryCloseDateStr = tds.get(1).text().trim();
                    String period = tds.get(2).text().trim();
                    String dividendAmountStr = tds.get(3).text().trim();

                    Dividend dividend = new Dividend();
                    dividend.setCreated(LocalDateTime.now());
                    dividend.setStateDate(LocalDateTime.now());
                    dividend.setCompanyName("X5 Group");
                    dividend.setCompanyCode(Dividend.COMPANY_CODE_X5);
                    if (NOT_AVAILABLE.equals(declarationDate) && NOT_AVAILABLE.equals(period)) {
                        if (!registryCloseDateStr.contains(FORECAST_MARK)) {
                            dividend.setDividendDeclarationDate(null);
                            dividend.setDividendPeriod(null);
                            dividend.setCloseRegisterDate(parseDate(registryCloseDateStr));
                            dividend.setState(Dividend.STATE_EXPECTED);
                        } else {
                            dividend.setDividendDeclarationDate(null);
                            dividend.setDividendPeriod(null);
                            registryCloseDateStr = registryCloseDateStr.replace(FORECAST_MARK, "").trim();
                            dividend.setCloseRegisterDate(parseDate(registryCloseDateStr));
                            dividend.setState(Dividend.STATE_FORECAST);
                        }
                    } else {
                        dividend.setDividendDeclarationDate(parseDate(declarationDate));
                        dividend.setCloseRegisterDate(parseDate(registryCloseDateStr));
                        dividend.setDividendPeriod(period);
                        dividend.setState(Dividend.STATE_PAID);
                    }

                    dividend.setDividendAmount(parseAmount(dividendAmountStr));
                    dividend.setPaymentDate(null);
                    dividend.setBuyBeforeDate(null);

                    if (!dividendRepository.existsByCompanyCodeAndCloseRegisterDate(
                            dividend.getCompanyCode(),
                            dividend.getCloseRegisterDate()
                    )) {
                        dividendList.add(dividend);
                    }

                }
                break;
            }
        }
        if (!dividendList.isEmpty()) {
            try {
                dividendRepository.saveAll(dividendList);
            } catch (Exception e) {
                throw new ExceptDb("DividendDohodService_01",
                        String.format("Error while saveAll Dividend, cause - %s", e.getMessage()));
            }
        }
    }

    public List<Dividend> getX5HistoryDividends() {
        try {
            return dividendRepository.findPaidDividendsForX5();
        } catch (Exception e) {
            throw new ExceptDb("DividendDohodService_02",
                    String.format("SELECT d FROM Dividend d WHERE d.companyCode = '" + Dividend.COMPANY_CODE_X5 +
                            "' AND d.state = '" + Dividend.STATE_PAID + "', cause - %s",
                            e.getMessage()));
        }
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    private BigDecimal parseAmount(String amountStr) {
        String cleaned = amountStr.replace(" ", "").replace(",", ".");
        return new BigDecimal(cleaned);
    }
}
