package JamKom.com.PassportSystem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VillageRepository extends JpaRepository<Village, Long>{
	
	public List<Village> findByParishId(Long parishId);

}
