package basic.chunkorientedtasklet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaItemListWriter<T> extends JpaItemWriter<List<T>> {
    private final JpaItemWriter<T> jpaListWriter;

    @Override
    public void write(List<? extends List<T>> items) {
        List<T> totalList = new ArrayList<>();

        items.forEach(totalList::addAll);

        jpaListWriter.write(totalList);
    }
}
