package ru.zeker.task.repository.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.zeker.common.dto.task.Difficulty;
import ru.zeker.task.domain.model.entity.Task;

import java.util.List;

@UtilityClass
public class TaskSpecification {

    public static Specification<Task> hasTitle(String title) {
        return (root, query, builder) ->
                (title == null || title.isBlank())
                        ? builder.conjunction()
                        : builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Task> hasDifficulties(List<Difficulty> diffs) {
        return (root, query, builder) -> {
            if (diffs == null || diffs.isEmpty()) {
                return builder.conjunction();
            }
            return root.get("difficulty").in(diffs);
        };
    }


    public static Specification<Task> hasAllTags(List<String> tagNames) {
        return (root, query, builder) -> {
            if (tagNames == null || tagNames.isEmpty()) {
                return builder.conjunction();
            }

            // Создаем подзапрос
            var subquery = query.subquery(Long.class);
            var subRoot = subquery.from(Task.class);
            var join = subRoot.join("tags");

            // Подсчёт задач, у которых все теги из списка
            subquery.select(subRoot.get("id"));
            subquery.where(
                    builder.and(
                            builder.equal(subRoot.get("id"), root.get("id")),
                            join.get("name").in(tagNames)
                    )
            );
            subquery.groupBy(subRoot.get("id"));
            subquery.having(builder.equal(builder.countDistinct(join.get("name")), tagNames.size()));

            return builder.exists(subquery);
        };
    }

}