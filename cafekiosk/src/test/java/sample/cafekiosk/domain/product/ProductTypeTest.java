package sample.cafekiosk.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.domain.product.ProductType.*;

class ProductTypeTest {
    @DisplayName("상품 타입이 재고 관련 타입인지를 확인한다.")
    @Test
    void containsStockType() {
        assertThat(ProductType.containsStockType(HANDMADE)).isFalse();
        assertThat(ProductType.containsStockType(BAKERY)).isTrue();
    }
}