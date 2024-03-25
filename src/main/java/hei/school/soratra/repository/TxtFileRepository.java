package hei.school.soratra.repository;

import hei.school.soratra.repository.model.TxtFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TxtFileRepository extends JpaRepository<TxtFile, String> {
}
