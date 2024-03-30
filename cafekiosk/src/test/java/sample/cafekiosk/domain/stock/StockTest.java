package sample.cafekiosk.domain.stock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class StockTest {
    @DisplayName("재고의 수량이 제공된 수량보다 적은지 확인한다.")
    @Test
    void isQuantityLessThan() {
        Assertions.assertThat(Stock.create("001", 1).isQuantityLessThan(2)).isTrue();
    }
}