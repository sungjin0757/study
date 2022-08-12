package basic.fileloadapi.repository;

import basic.fileloadapi.domain.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {
}
