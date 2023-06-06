package im.fitdiary.server.common.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

public class QuerydslUtil {

    public static OrderSpecifier<?>[] getOrderSpecifiers(Path<?> Qentity, Pageable pageable) {
        return pageable.getSort().stream()
                .filter(sort -> Arrays.stream(Qentity.getType().getDeclaredFields())
                        .anyMatch(field -> field.getName().equals(sort.getProperty()))
                )
                .map(sort -> getOrderSpecifier(sort, Qentity))
                .toArray(OrderSpecifier[]::new);
    }

    private static OrderSpecifier<?> getOrderSpecifier(Sort.Order sort, Path<?> Qentity) {
        return new OrderSpecifier(
                sort.getDirection().isAscending() ? Order.ASC : Order.DESC,
                Expressions.path(Object.class, Qentity, sort.getProperty())
        );
    }
}
