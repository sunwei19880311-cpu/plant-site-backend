package com.plantsite.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页响应 {records, pageNum, pageSize, total, totalPages}。
 * 与前端 PageResult 契约一致。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private List<T> records;
    private long pageNum;
    private long pageSize;
    private long total;
    private long totalPages;

    public static <E, T> PageResult<T> from(IPage<E> page, Function<E, T> converter) {
        long size = page.getSize() <= 0 ? 1 : page.getSize();
        long totalPages = (page.getTotal() + size - 1) / size;
        List<T> records = page.getRecords().stream().map(converter).collect(Collectors.toList());
        return new PageResult<>(records, page.getCurrent(), page.getSize(), page.getTotal(), totalPages);
    }
}
