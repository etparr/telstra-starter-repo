package au.com.telstra.simcardactivator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SimCardRepository extends JpaRepository<SimCardTransaction, Long> {
}
