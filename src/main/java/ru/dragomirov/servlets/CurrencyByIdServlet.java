package ru.dragomirov.servlets;

import ru.dragomirov.models.Currency;
import ru.dragomirov.services.CurrencyService;
import ru.dragomirov.utils.BaseServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.IOException;

/**
 * @doGet: Получение конкретной валюты.
 */
@WebServlet(name = "CurrencyByIdServlet", urlPatterns = "/currency/*")
public class CurrencyByIdServlet extends BaseServletUtils {
    private CurrencyService currencyService;

    @Override
    public void init() {
        currencyService = new CurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            String currencyCode = pathInfo.substring(1);

            if (currencyCode.isEmpty()) {
                http400Errors(resp,"Код валюты отсутствует в адресе");
                return;
            }

            Currency currency = currencyService.findByCode(currencyCode);

            if (currency == null) {
                http404Errors(resp, "Валюта не найдена");
                return;
            }

            Gson gson = new Gson();
            String json = gson.toJson(currency);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e){
            http500Errors(resp, e, "База данных недоступна");
        }
    }
}
