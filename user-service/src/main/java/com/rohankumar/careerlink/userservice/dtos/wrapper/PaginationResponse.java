package com.rohankumar.careerlink.userservice.dtos.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {

    private Integer pageNo;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalItems;
    private Boolean firstPage;
    private Boolean lastPage;
    private List<T> content;

    public static <T, R> PaginationResponse<R> makeResponse(Page<T> page, Function<T, R> mapper) {

        List<R> content = page.getContent().stream()
                .map(mapper)
                .toList();

        return PaginationResponse.<R>builder()
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .firstPage(page.isFirst())
                .lastPage(page.isLast())
                .content(content)
                .build();
    }
}