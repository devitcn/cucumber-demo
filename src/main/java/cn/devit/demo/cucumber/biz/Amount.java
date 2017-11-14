package cn.devit.demo.cucumber.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Amount {

    Pattern pattern = Pattern.compile("(\\w{3})(.*)");
    private Currency currency;
    private BigDecimal number;

    public Amount(String amount) {
        Matcher matcher = pattern.matcher(amount);
        if (matcher.find()) {
            String code = matcher.group(1);
            String number = matcher.group(2);
            this.currency = Currency.getInstance(code);
            this.number = new BigDecimal(number);
            this.number = this.number.setScale(this.currency.getDefaultFractionDigits());
        } else {
            throw new IllegalArgumentException("不是一个有效的货币数字");
        }
    }

    public Amount(Currency currency2, BigDecimal add) {
        Objects.requireNonNull(currency2);
        Objects.requireNonNull(add);
        this.currency = currency2;
        this.number = add.setScale(this.currency.getDefaultFractionDigits());
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public Amount plus(Amount amount) {
        if (amount.getCurrency().equals(getCurrency())) {
            return new Amount(currency, this.number.add(amount.number));
        }
        throw new IllegalArgumentException("只支持同币种相加");
    }

    @Override
    public String toString() {
        return currency + number.toString();
    }
}
