package ru.dragomirov.servlets;

import ru.dragomirov.dao.JdbcCurrencyDAO;
import ru.dragomirov.dto.CurrencyDTO;
import ru.dragomirov.entities.Currency;
import ru.dragomirov.exceptions.BaseServlet;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.utils.MappingUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @doGet: Получение списка валют.
 * @doPost: Добавление новой валюты в базу.
 */
@WebServlet(name = "CurrencyListAndCreateServlet", urlPatterns = "/currencies")
public class CurrencyListAndCreateServlet extends BaseServlet {
    private JdbcCurrencyDAO jdbcCurrencyDAO;

    @Override
    public void init() {
        this.jdbcCurrencyDAO = new JdbcCurrencyDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Currency> currencyList = jdbcCurrencyDAO.findAll();
            List<CurrencyDTO> currencyDTOList = currencyList.stream()
                    .map(MappingUtils::toDTO).collect(Collectors.toList());

            Gson gson = new Gson();
            String json = gson.toJson(currencyDTOList);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");

            if (name.isEmpty() || code.isEmpty() || sign.isEmpty()) {
                http400Errors(resp, "Отсутствует нужное поле формы");
                return;
            }

            Optional<Currency> existingCurrency = jdbcCurrencyDAO.findByCode(code);
            if (existingCurrency.isPresent()) {
                http409Errors(resp, "Валюта с таким кодом уже существует");
                return;
            }

            Currency newCurrency = new Currency(name, code, sign);
            jdbcCurrencyDAO.save(newCurrency);

            CurrencyDTO currencyDTO = MappingUtils.toDTO(newCurrency);

            Gson gson = new Gson();
            String json = gson.toJson(currencyDTO);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }
}
