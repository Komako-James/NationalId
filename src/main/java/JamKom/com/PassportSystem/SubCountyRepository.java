package JamKom.com.PassportSystem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCountyRepository extends JpaRepository<SubCounty, Long>{
	
	public List<SubCounty> findByDistrictId(Long districtId);
		
		
	

}
