package cn.devit.demo.cucumber.simple.currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.devit.demo.cucumber.biz.Amount;

import static org.hamcrest.MatcherAssert.assertThat;

import cucumber.api.DataTable;
import cucumber.api.java.en.*;
import static org.hamcrest.Matchers.*;

public class CurrencyStep {

    List<Amount> amounts = new ArrayList<>();

    @Given("^存入(.*?)$")
    public void 存入(String money){
      Amount am = new Amount(money);
      amounts.add(am);
    }

    @Then("^合计是(.*?)$")
    public void sum(String money){
      Amount expect = new Amount(money);
      Amount balance = balance(expect.getCurrency());
      assertThat(
        balance.getNumber(),
        is(expect.getNumber()) );
    }

    Map<Currency, BigDecimal> transfer = new HashMap<Currency, BigDecimal>();
    Currency baseCurrency;
    BigDecimal base;

    
    @Given("^(\\w+)外币换(\\w+)汇率表$")
    public void 汇率表(String number, String cu, DataTable table)
            throws Throwable {
        this.baseCurrency = Currency.getInstance(cu);
        this.base = new BigDecimal(number);

        List<List<String>> cells = table.cells(0);

        for (List<String> list : cells) {
            transfer.put(Currency.getInstance(list.get(0)),
                    new BigDecimal(list.get(1)));
        }
    }
    
    Amount balance(Currency currency) {
        Amount reduce = new Amount(currency, BigDecimal.ZERO);

        for (Amount item : amounts) {
            if (currency.equals(item.getCurrency())) {
                reduce = reduce.plus(item);
            } else {
                Amount temp = exchangeTo(item, currency);
                reduce = reduce.plus(temp);
            }
        }
        return reduce;
    }

    private Amount exchangeTo(Amount item, Currency currency) {
        if (currency.equals(item.getCurrency())) {
            return item;
        }
        //外币换本币
        if (currency.equals(baseCurrency)) {
            BigDecimal bigDecimal = transfer.get(item.getCurrency());
            BigDecimal transfered = item.getNumber().multiply(bigDecimal)
                    .divide(base, currency.getDefaultFractionDigits());
            return new Amount(currency, transfered);
        } else {
            //互相转换
            //先换成base货币
            Amount middle = exchangeTo(item, baseCurrency);
            //本币换成外币
            BigDecimal bigDecimal = transfer.get(currency);
            BigDecimal transfered = middle.getNumber().multiply(base)
                    .divide(bigDecimal, currency.getDefaultFractionDigits());
            return new Amount(currency, transfered);
        }
    }
}
