package ru.practicum.explorewithme.common;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.common.exceptions.PaginationException;

public class PageCreatorUtil {
    public static PageRequest createPage(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new PaginationException("Параметры пагинации заданы неверно");
        }
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}
