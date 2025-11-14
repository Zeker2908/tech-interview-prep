package ru.zeker.taskservice.repository;

import ru.zeker.taskservice.domain.model.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

    @Query("SELECT t FROM Task t ORDER BY function('RANDOM')")
    List<Task> findRandomTasks(Pageable pageable);
}
