package JamKom.com.PassportSystem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParishRepository extends JpaRepository<Parish, Long>{
	
	public List<Parish> findByParishId(Long parishId);

	public List<Parish> findBySubcountyId(Long subcountyId);

}
