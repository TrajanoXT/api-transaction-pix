package api.transaction.pix.repository;

import api.transaction.pix.entity.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PixKeyRepository extends JpaRepository<PixKey, Long> {
}
