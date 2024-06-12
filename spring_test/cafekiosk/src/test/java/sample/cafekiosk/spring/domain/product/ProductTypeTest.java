package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
class ProductTypeTest {

//    @DisplayName("상품 타입이 재고 관련 타입인지를 확인한다.")
//    @CsvSource({"HANDMADE, false", "BOTTLE, true", "BAKERY, true"})
//    @ParameterizedTest
//    void containsStockType(ProductType productType, boolean expected) {
//        assertThat(ProductType.containsStockType(productType)).isEqualTo(expected);
//    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 확인한다.")
    @MethodSource("provideProductTypesForCheckingStockType")
    @ParameterizedTest
    void containsStockType(ProductType productType, boolean expected) {
        assertThat(ProductType.containsStockType(productType)).isEqualTo(expected);
    }

    public static Stream<Arguments> provideProductTypesForCheckingStockType() {
        return Stream.of(
                Arguments.of(HANDMADE, false),
                Arguments.of(BOTTLE, true),
                Arguments.of(BAKERY, true)
        );
    }
}