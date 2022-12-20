package basic.fileloadapi.repository;

import basic.fileloadapi.domain.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AttachFileRepository extends JpaRepository<AttachFile, Long>, AttachFileRepositoryCustom,
        QuerydslPredicateExecutor {
}
